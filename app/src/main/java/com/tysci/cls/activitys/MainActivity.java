package com.tysci.cls.activitys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.pgyersdk.update.PgyUpdateManager;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseActivity;
import com.tysci.cls.fragments.CLSMatchListFragment;
import com.tysci.cls.fragments.CLSWebViewFragment;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.StatusBarCompat;
import com.tysci.cls.views.widgets.MainMenuTab;
import com.tysci.cls.views.widgets.slidingmenu.SlidingMenu;
import com.tysci.cls.wxapi.WXEntryActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity{
    @Bind(R.id.sliding_menu)
    protected SlidingMenu slidingMenu;
    @Bind(R.id.tab_qiudui)
    protected MainMenuTab tabQiuDui;
    @Bind(R.id.tab_jifen)
    protected MainMenuTab tabJiFen;
    @Bind(R.id.tab_xinwen)
    protected MainMenuTab tabXinWen;
    @Bind(R.id.tab_yaguan)
    protected MainMenuTab tabYaGuan;
    @Bind(R.id.iv_game)
    protected ImageView ivGame;

    private CLSWebViewFragment teamFragment;
    private CLSWebViewFragment pointsFragment;
    private CLSMatchListFragment matchFragment;
    private CLSWebViewFragment newsFragment;
    private CLSWebViewFragment AFCFragment;

    private int currentTab;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void initViews() {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
            titleBar.setPaddingTop(StatusBarCompat.getStatusBarHeight(this));
        }
        initSlidingMenu();
        setTitleBarLeftIcon(R.mipmap.icon_main_left_menu);
        PgyUpdateManager.register(this);

        tabQiuDui.setOnClickListener(this);
        tabJiFen.setOnClickListener(this);
        tabXinWen.setOnClickListener(this);
        tabYaGuan.setOnClickListener(this);
        ivGame.setOnClickListener(this);
        //initWebView();
        setSelectedTab(R.id.iv_game);
    }

    private void initSlidingMenu(){
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // mainLeftMenu= LayoutInflater.from(this).inflate(R.layout.layout_main_left_menu,null);
        slidingMenu.setMenu(R.layout.layout_main_left_menu);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setFadeDegree(0.35f);
        //slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT,true);
        View leftMenuView=slidingMenu.getMenu();
        leftMenuView.findViewById(R.id.layout_user_default_info).setOnClickListener(this);
    }

    private void hideFragments(int tab,FragmentTransaction transaction){
        if(tab==R.id.tab_qiudui){
            if(teamFragment!=null){
                transaction.hide(teamFragment);
            }
        }else if(tab==R.id.tab_jifen){
            if(pointsFragment!=null){
                transaction.hide(pointsFragment);
            }
        }else if(tab==R.id.iv_game){
            if(matchFragment!=null){
                transaction.hide(matchFragment);
            }
        }else if(tab==R.id.tab_xinwen){
            if(newsFragment!=null){
                transaction.hide(newsFragment);
            }
        }else if(tab==R.id.tab_yaguan){
            if(AFCFragment!=null){
                transaction.hide(AFCFragment);
            }
        }
    }

    private void setTabPager(int tab,String url){
        FragmentTransaction transaction=null;
        if(tab!=currentTab){
            //setSelectedTab(tab);
            transaction=this.getSupportFragmentManager().beginTransaction();
            hideFragments(currentTab,transaction);
        }else{
            return;
        }
        switch(tab){
            case R.id.tab_qiudui:
                if(teamFragment==null){
                    teamFragment=new CLSWebViewFragment();
                    teamFragment.setUrl(url);
                    transaction.add(R.id.layout_container,teamFragment);
                }else{
                    transaction.show(teamFragment);
                }
                break;
            case R.id.tab_jifen:
                if(pointsFragment==null){
                    pointsFragment=new CLSWebViewFragment();
                    pointsFragment.setUrl(url);
                    transaction.add(R.id.layout_container,pointsFragment);
                }else{
                    transaction.show(pointsFragment);
                }
                break;
            case R.id.iv_game:
                if(matchFragment==null){
                    matchFragment=new CLSMatchListFragment();
                   // matchFragment.setUrl(url);
                    transaction.add(R.id.layout_container,matchFragment);
                }else{
                    transaction.show(matchFragment);
                }
                break;
            case R.id.tab_xinwen:
                if(newsFragment==null){
                    newsFragment=new CLSWebViewFragment();
                    newsFragment.setUrl(url);
                    transaction.add(R.id.layout_container,newsFragment);
                }else{
                    transaction.show(newsFragment);
                }
                break;
            case R.id.tab_yaguan:
                if(AFCFragment==null){
                    AFCFragment=new CLSWebViewFragment();
                    AFCFragment.setUrl(url);
                    transaction.add(R.id.layout_container,AFCFragment);
                }else{
                    transaction.show(AFCFragment);
                }
                break;
        }
        currentTab=tab;
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void back() {
        //super.back();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getIntentData(Intent intent) {

    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    @Override
    protected void handleInstanceState(Bundle outState) {

    }

    @Override
    protected void onViewClick(View view) {
        setSelectedTab(view.getId());
        switch (view.getId()){
            case R.id.layout_user_default_info:
                Intent intent=new Intent(this,WXEntryActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

//    private void initWebView(){
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setAppCacheEnabled(false);
//        webSettings.setSupportZoom(false);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                System.out.println("url:"+url);
//                return filterWebViewUrl(url)||super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient());
//    }

    public void setSelectedTab(int id){
        String tag=null;
        if(id==R.id.tab_qiudui){
            titleBar.setTitleBarTitle("中超赛场-球队");
            tabQiuDui.setTabChecked(true);
            tabJiFen.setTabChecked(false);
            tabXinWen.setTabChecked(false);
            tabYaGuan.setTabChecked(false);
            ivGame.setSelected(false);
            tag="team/list";
        }else if(id==R.id.tab_jifen){
            titleBar.setTitleBarTitle("中超赛场-积分榜");
            tabQiuDui.setTabChecked(false);
            tabJiFen.setTabChecked(true);
            tabXinWen.setTabChecked(false);
            tabYaGuan.setTabChecked(false);
            ivGame.setSelected(false);
            tag="score/group/list?uniqueTournamentId=649";
        }else if(id==R.id.tab_xinwen){
            titleBar.setTitleBarTitle("中超赛场-新闻");
            tabQiuDui.setTabChecked(false);
            tabJiFen.setTabChecked(false);
            tabXinWen.setTabChecked(true);
            tabYaGuan.setTabChecked(false);
            ivGame.setSelected(false);
            tag="news/list";
        }else if(id==R.id.tab_yaguan){
            titleBar.setTitleBarTitle("中超赛场-亚冠");
            tabQiuDui.setTabChecked(false);
            tabJiFen.setTabChecked(false);
            tabXinWen.setTabChecked(false);
            tabYaGuan.setTabChecked(true);
            ivGame.setSelected(false);
            tag="match/champions/all?uniqueTournamentId=463";
        }else if(id==R.id.iv_game){
            titleBar.setTitleBarTitle("中超赛场");
            tabQiuDui.setTabChecked(false);
            tabJiFen.setTabChecked(false);
            tabXinWen.setTabChecked(false);
            tabYaGuan.setTabChecked(false);
            ivGame.setSelected(true);
            tag="match/all?uniqueTournamentId=649";
        }
        if(!TextUtils.isEmpty(tag)) {
            String url = HttpUrls.HOST_URL + tag;
            setTabPager(id,url);
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

}
