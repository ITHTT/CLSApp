package com.tysci.cls.networks;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tysci.cls.networks.cookie.CookieJarImpl;
import com.tysci.cls.networks.cookie.store.PersistentCookieStore;
import com.tysci.cls.utils.KLog;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/2.
 */
public class HttpClientUtil {
    private OkHttpClient okHttpClient;

    private static HttpClientUtil httpClientUtil;

    /**
     * 连接超时时间
     */
    private static final long CONNECTED_TIEM_OUT = 60;
    private static final long READ_TIME_OUT = 60;
    private static final long WRITE_TIME_OUT = 60;

    private Handler devidlerHandler = new Handler(Looper.getMainLooper());


    private HttpClientUtil(Context context, String cachePath) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(CONNECTED_TIEM_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        /**取消重试*/
        okHttpClientBuilder.retryOnConnectionFailure(false);
        /**添加cookie存储*/
        okHttpClientBuilder.cookieJar(new CookieJarImpl(new PersistentCookieStore(context)));

        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        /**设置缓存目录*/
        okHttpClientBuilder.cache(new Cache(file, 100 * 1024 * 1024));

        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        okHttpClient = okHttpClientBuilder.build();

    }

    public static void initHttpClientUtil(Context context, String cachePath) {
        if (httpClientUtil == null) {
            httpClientUtil = new HttpClientUtil(context, cachePath);
        }
    }

    public static HttpClientUtil getHttpClientUtil() {
        if (httpClientUtil == null) {
            throw new RuntimeException("HttpClientUtil没有初始化");
        }
        return httpClientUtil;
    }

    /**
     * 取消Tag标记的请求
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        KLog.e("取消请求Tag:" + tag);
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            KLog.e("Tag:" + call.request().tag());
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            KLog.e("Tag:" + call.request().tag());
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private Request.Builder createRequestBuilder(String tag, String url, int maxAge, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        builder.tag(tag)
                .url(url)
                .cacheControl(createCacheControl(maxAge));
        builder.headers(createHeaders(headers));
        return builder;
    }

    /**
     * 创建请求头部参数
     *
     * @param headers
     * @return
     */
    protected Headers createHeaders(Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
//        headerBuilder.add("Charset", "UTF-8");
//        headerBuilder.add("Accept-Encoding", "gzip,deflate");
        if (headers != null && !headers.isEmpty()) {

            for (String key : headers.keySet()) {
                headerBuilder.add(key, headers.get(key));
            }
        }
        return headerBuilder.build();
    }

    /**
     * 创建请求缓存策略
     *
     * @param maxAge
     * @return
     */
    protected CacheControl createCacheControl(int maxAge) {
        if (maxAge > 0) {
            CacheControl.Builder builder = new CacheControl.Builder();
            builder.maxAge(maxAge, TimeUnit.SECONDS);
            builder.onlyIfCached();
            return builder.build();
        } else {
            return CacheControl.FORCE_NETWORK;
        }
    }

    protected RequestBody createRequestBody(Map<String, String> params, File... files) {
        if (files == null || files.length == 0) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(params, builder);
            return builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(params, builder);

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                builder.addFormDataPart(file.getName(), file.getName(), fileBody);
            }
            return builder.build();
        }
    }

    protected void addParams(Map<String, String> params, MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void addParams(Map<String, String> params, FormBody.Builder builder) {
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public void sendGetRequest(String tag, String url, int maxAge, final StringResponseCallBack responseCallBack) {
        sendGetRequest(tag, url, maxAge, null, responseCallBack);
    }

    public void sendGetRequest(String tag, String url, int maxAge, Map<String, String> headers, final StringResponseCallBack responseCallBack) {
        Request.Builder builder = createRequestBuilder(tag, url, maxAge, headers);
        Request request = builder.get().build();
        handlerReqeust(request, responseCallBack);
    }

    public void sendPostRequest(String tag, String url, Map<String, String> header, Map<String, String> params, final StringResponseCallBack responseCallBack) {
        Request.Builder builder = createRequestBuilder(tag, url, 0, header);
        Request request = builder.post(createRequestBody(params)).build();
        handlerReqeust(request, responseCallBack);
    }

    public void sendPostRequest(String tag, String url, Map<String, String> params, final StringResponseCallBack responseCallBack) {
        sendPostRequest(tag, url, null, params, responseCallBack);
    }

    public void uploadFile(String tag, String url, String[] keys, File[] files, HashMap<String, String> map, final StringResponseCallBack callBack) {
        MultipartBody.Builder builder = new MultipartBody.Builder("AaB03x").setType(MultipartBody.FORM);
        RequestBody fileBody;
        File aFile;
        String aKey;
        String fileName;
        for (int i = 0, length = files.length; i < length; i++) {
            aFile = files[i];
            aKey = keys[i];
            fileName = aFile.getName();
            fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), aFile);
//            builder.addFormDataPart("files",
//                    null,
//                    new MultipartBody.Builder("BbC04y").addPart(Headers.of("Content-Disposition",
//                            "form-data; name=\"" + aKey + "\"; filename=\"" + aFile.getName() + "\""),
//                            RequestBody.create(MediaType.parse("image/png"), aFile)).build());
            builder.addPart(Headers.of("Content-Disposition",
                            "form-data; name=\"" + aKey + "\"; filename=\"" + fileName + "\""),
                    fileBody);
        }
        for (String key : map.keySet()) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                    RequestBody.create(null, map.get(key)));
        }
        MultipartBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        handlerReqeust(request, callBack);
    }

    private String getResponseResult(Response response) throws IOException {
        String contentType = response.headers().get("Content-Encoding");
        String result = null;
        if (!TextUtils.isEmpty(contentType) && contentType.equalsIgnoreCase("gzip")) {
            // 以GZIP形式压缩后的字符串，需要解压缩

            GZIPInputStream gzip = new GZIPInputStream(new BufferedInputStream(new ByteArrayInputStream(response.body().bytes())));
            //noinspection SpellCheckingInspection
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            int count;
            while ((count = gzip.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            byte[] bytes = baos.toByteArray();
            result = new String(bytes, "UTF-8");
        } else {
            // 正常数据，不需要解压缩
            result = new String(response.body().bytes(), "UTF-8");
        }
        return result;
    }

    private void handlerReqeust(Request request, final StringResponseCallBack responseCallBack) {
        responseCallBack.onBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                KLog.e("Error:" + e.getMessage());
                if (e != null && "Canceled".equals(e.getMessage())) {
                    return;
                }
                final Call resultCall = call;
                final Exception error = e;

                devidlerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallBack.onError(resultCall, error);
                        responseCallBack.onFinish(resultCall);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                KLog.e("获取响应成功...");
                if (call.isCanceled()) {
                    return;
                }

                if(!response.isSuccessful()||response.code()!=200){
                    devidlerHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            responseCallBack.onError(call, new Exception("请求响应失败，响应码:"+response.code()));
                            responseCallBack.onFinish(call);
                        }
                    });
                    return;
                }
                final Call resultCall = call;
                String result = null;
                Exception error = null;
                try {
                    result = getResponseResult(response);
                } catch (IOException exception) {
                    error = exception;
                }
                final String finalResult = result;
                final Exception finalError = error;
                devidlerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.isEmpty(finalResult) || finalError != null) {
                            responseCallBack.onError(call, finalError);
                        } else {
                            try {
                                responseCallBack.onSuccess(call, finalResult);
                            } catch (Exception e) {
                                KLog.e(e.getMessage());
                            }
                        }
                        responseCallBack.onFinish(resultCall);
                    }
                });
            }
        });
    }


    /**
     * 上传文件
     *
     * @param tag
     * @param url
     * @param header
     * @param params
     * @param responseCallBack
     * @param files
     */
    public void uploadFiles(String tag, String url, Map<String, String> header, Map<String, String> params, final ProgressResponseCallBack responseCallBack, File... files) {
        Request.Builder builder = createRequestBuilder(tag, url, 0, header);
        RequestBody requestBody = createRequestBody(params, files);
        builder.post(new ProgressRequestBody(requestBody, new ProgressRequestBody.ProgressListener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                final int progress = (int) ((int) (bytesWritten * 100) / contentLength);
                devidlerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallBack.loadingProgress(progress);
                    }
                });
            }
        }));
        Request request = builder.build();
        handlerReqeust(request, responseCallBack);
    }

    /**
     * 下载文件
     *
     * @param tag
     * @param url
     * @param header
     * @param saveFile
     * @param responseCallBack
     */
    public void downloadFile(String tag, String url, Map<String, String> header, final String saveFile, final ProgressResponseCallBack responseCallBack) {
        Request.Builder builder = createRequestBuilder(tag, url, 0, header);

        Request request = builder.build();
        responseCallBack.onBefore(request);
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                devidlerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCallBack.onError(call, e);
                        responseCallBack.onFinish(call);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                try {
                    final String filePath = saveDownloadFile(response, saveFile, responseCallBack);
                    devidlerHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            responseCallBack.onSuccess(call, filePath);
                            responseCallBack.onFinish(call);
                        }
                    });
                } catch (final IOException exception) {
                    devidlerHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            responseCallBack.onError(call, exception);
                            responseCallBack.onFinish(call);
                        }
                    });
                }
            }

        });
    }

    private String saveDownloadFile(Response response, String saveFile, final ProgressResponseCallBack httpResultCallback) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[1024];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            File file = new File(saveFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                if (httpResultCallback != null) {
                    final long finalSum = sum;
                    devidlerHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpResultCallback.loadingProgress((int) (finalSum * 100 / total));
                        }
                    });
                }
            }
            fos.flush();
            return file.getAbsolutePath();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public interface StringResponseCallBack {
        void onBefore(Request request);

        void onError(Call call, Exception error);

        void onSuccess(Call call, String response);

        void onFinish(Call call);
    }

    public interface ProgressResponseCallBack extends StringResponseCallBack {
        void loadingProgress(int progress);
    }
}
