package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tysci.cls.R;
import com.tysci.cls.views.widgets.TitleBar;

/**
 * Created by Administrator on 2016/6/30.
 */
public class WebViewActivity extends AppCompatActivity{
    private WebView webView;
    private ProgressBar progressBar;
    private TitleBar titleBar;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cls_webview);
        initViews();
        getIntentData();
    }

    private void initViews(){
        titleBar=(TitleBar)this.findViewById(R.id.title_bar);
        titleBar.setTitleBarLeftIcon(R.mipmap.arraw_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar=(ProgressBar)this.findViewById(R.id.pb_web);
        webView=(WebView)this.findViewById(R.id.webView);
        initWebView();
    }

    private void initWebView(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= 19){
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    private void getIntentData(){
        Intent intent=this.getIntent();
        if(intent!=null){
//            String title=intent.getStringExtra("title");
//            if(TextUtils.isEmpty(title)){
//                title="中超";
//            }
//            titleBar.setTitleBarTitle(title);
            url=intent.getStringExtra("url");
            webView.loadUrl(url);
        }
    }

    private boolean filterWebViewUrl(String url){
        if(url.contains("openWindow=1")){
            Intent intent=new Intent(this,WebViewActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
            return true;
        }
        return false;
    }


    private void back(){
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public class CustomWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("url:"+url);
            return filterWebViewUrl(url)||super.shouldOverrideUrlLoading(view, url);
        }
    }

    public class CustomWebChromeClient extends WebChromeClient{

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            titleBar.setTitleBarTitle(title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
