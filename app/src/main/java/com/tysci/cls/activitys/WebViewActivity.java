package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tysci.cls.R;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.ToastUtil;
import com.tysci.cls.views.widgets.TitleBar;

import java.net.URLDecoder;
import java.util.Map;

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
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setSupportZoom(false);
        if (Build.VERSION.SDK_INT >= 19){
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
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
        if(url.contains("match://detail")){
            Map<String,String> params= HttpUrls.URLRequest(url);
            KLog.e("matchId:"+params.get("matchid"));
            KLog.e("matchTime:"+params.get("matchtime"));
            KLog.e("params_size:"+params.size());

            for(Map.Entry<String, String> entry:params.entrySet()){
                System.out.println(entry.getKey()+"--->"+entry.getValue());
            }
            try {
                CLSMatchEntity matchEntity = new CLSMatchEntity();
                matchEntity.setId(Integer.parseInt(params.get("matchid")));
                matchEntity.setMatchTime(params.get("matchtime"));
                matchEntity.setHomeTeamId(Integer.parseInt(params.get("hometeamid")));
                matchEntity.setHomeTeamName(URLDecoder.decode(params.get("hometeamname"), "UTF-8"));
                int homescore=0;
                if(!TextUtils.isEmpty(params.get("hometeamscore"))){
                    homescore=Integer.parseInt(params.get("hometeamscore"));
                }
                matchEntity.setHomeTeamScore(homescore);
                matchEntity.setHomeTeamFlag(params.get("hometeamflag"));

                matchEntity.setAwayTeamId(Integer.parseInt(params.get("awayteamid")));
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

                Intent intent = new Intent(this, CLSMatchDetailActivity.class);
                intent.putExtra(CLSMatchDetailActivity.class.getSimpleName(), matchEntity);
                startActivity(intent);
            }catch(Exception e){
                ToastUtil.show(this,"数据异常");
            }
            return true;
        }else if(url.contains("openWindow=1")){
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
