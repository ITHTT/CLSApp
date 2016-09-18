package com.tysci.cls.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tysci.cls.R;
import com.tysci.cls.activitys.CLSMatchDetailActivity;
import com.tysci.cls.activitys.WebViewActivity;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.ToastUtil;

import java.net.URLDecoder;
import java.util.Map;

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
        setToutchListener();
        initWebView();
        setRefreshing();
        webView.loadUrl(url);
    }

    private void initWebView(){
        //webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setSupportZoom(false);
        webSettings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= 19){
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    private void setToutchListener(){
        swipeRefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (webView != null && webView.getScrollY() > 0) {
                    swipeRefresh.setEnabled(false);
                    return true;
                }
                    // 让SwipeRefreshLayout处理本次事件
                return swipeRefresh.onTouchEvent(event);
            }
        });

        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (webView.getScrollY() <= 0) {
                    swipeRefresh.setEnabled(true);
                }
                return false;
            }
        });
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
            }, 500);
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
        if(url.contains("match://detail")){
            Map<String,String> params= HttpUrls.URLRequest(url);
            KLog.e("matchId:" + params.get("matchid"));
            KLog.e("matchTime:"+params.get("matchtime"));
            KLog.e("params_size:"+params.size());

            for(Map.Entry<String, String> entry:params.entrySet()){
                System.out.println(entry.getKey()+"--->"+entry.getValue());
            }
            try {
                CLSMatchEntity matchEntity = new CLSMatchEntity();
                matchEntity.setId(Integer.parseInt(params.get("matchid")));
                matchEntity.setMatchTime(params.get("matchtime"));
                matchEntity.setHomeTeamId(TextUtils.isEmpty(params.get("hometeamid"))?0:Integer.parseInt(params.get("hometeamid")));
                matchEntity.setHomeTeamName(URLDecoder.decode(params.get("hometeamname"), "UTF-8"));
                int homescore=0;
                if(!TextUtils.isEmpty(params.get("hometeamscore"))){
                    homescore=Integer.parseInt(params.get("hometeamscore"));
                }
                matchEntity.setHomeTeamScore(homescore);
                matchEntity.setHomeTeamFlag(params.get("hometeamflag"));

                matchEntity.setAwayTeamId(TextUtils.isEmpty(params.get("awayteamid"))?0:Integer.parseInt(params.get("awayteamid")));
                matchEntity.setAwayTeamName(URLDecoder.decode(params.get("awayteamname"), "UTF-8"));
                int awayscore=0;
                if(!TextUtils.isEmpty(params.get("awayteamscore"))){
                    awayscore=Integer.parseInt(params.get("awayteamscore"));
                }
                matchEntity.setAwayTeamScore(awayscore);
                matchEntity.setAwayTeamFlag(params.get("awayteamflag"));
                matchEntity.setStatus(Integer.parseInt(params.get("status")));
                matchEntity.setChatRoomStatus(Integer.parseInt(params.get("chatroomstatus")));
                matchEntity.setChatRoomId(params.get("chatroomid"));

                Intent intent = new Intent(baseActivity, CLSMatchDetailActivity.class);
                intent.putExtra(CLSMatchDetailActivity.class.getSimpleName(), matchEntity);
                startActivity(intent);
            }catch(Exception e){
                ToastUtil.show(baseActivity, "数据异常");
            }
            return true;
        }else if(url.contains("openWindow=1")){
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
