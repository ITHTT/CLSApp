package com.tysci.cls.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tysci.cls.R;
import com.tysci.cls.activitys.WebViewActivity;
import com.tysci.cls.app.BaseFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/29.
 */
public class CLSWebViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.webView)
    protected WebView webView;

    private String url;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        swipeRefresh.setOnRefreshListener(this);
        initWebView();
        setRefreshing();
        webView.loadUrl(url);
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

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void setRefreshing() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete() {
        if(swipeRefresh!=null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }


    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    private boolean filterWebViewUrl(String url){
        if(url.contains("openWindow=1")){
            Intent intent=new Intent(baseActivity,WebViewActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        webView.reload();
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("url:"+url);
            return filterWebViewUrl(url)||super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onRefreshCompelete();
        }
    }

    public class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }
}
