package com.tysci.cls.activitys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseActivity;
import com.tysci.cls.fragments.CLSMatchListFragment;
import com.tysci.cls.fragments.CLSWebViewFragment;
import com.tysci.cls.modles.UserInfoEntity;
import com.tysci.cls.networks.GlideImageLoader;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.StatusBarCompat;
import com.tysci.cls.utils.UserInfoUtils;
import com.tysci.cls.views.widgets.CircleImageView;
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
        titleBar.setRightMenuIcon(R.mipmap.icon_main_titlebar_refresh, this);
        PgyUpdateManager.register(this);

        tabQiuDui.setOnClickListener(this);
        tabJiFen.setOnClickListener(this);
        tabXinWen.setOnClickListener(this);
        tabYaGuan.setOnClickListener(this);
        ivGame.setOnClickListener(this);
        //initWebView();
        setSelectedTab(R.id.iv_game);

        if(UserInfoUtils.checkLogin(this)){
            setUserInfo(UserInfoUtils.getUserInfo(this));
        }
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
        leftMenuView.findViewById(R.id.bt_exit_login).setOnClickListener(this);
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
        titleBar.getRightMenuImageView().setVisibility(View.GONE);
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
                titleBar.getRightMenuImageView().setVisibility(View.VISIBLE);
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
                if(!UserInfoUtils.checkLogin(this)) {
                    Intent intent = new Intent(this, WXEntryActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_titlebar_next_menu01:
                if(!matchFragment.isRefreshing()){
                    matchFragment.refreshMatchList();
                    titleBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            matchFragment.requestMatchListInfos();
                        }
                    },2000);
                }
                break;
            case R.id.bt_exit_login:
                UserInfoUtils.exitLogin(this);
                slidingMenu.toggle();
                break;
        }
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        KLog.e("用户登录成功");
        setUserInfo(userInfoEntity);
    }

    @Override
    protected void userExit() {
        super.userExit();
        View view=slidingMenu.getMenu();
        Button btExitLogin= (Button) view.findViewById(R.id.bt_exit_login);
        btExitLogin.setVisibility(View.GONE);
        CircleImageView ivUserHeader= (CircleImageView) view.findViewById(R.id.iv_user_header);
        TextView tvUserName=(TextView)view.findViewById(R.id.tv_user_name);
        ivUserHeader.setImageResource(R.mipmap.icon_camera);
        tvUserName.setText("登录后才能竞猜哦");

    }

    private void setUserInfo(UserInfoEntity userInfoEntity){
        if(userInfoEntity!=null){
            View view=slidingMenu.getMenu();
            Button btExitLogin= (Button) view.findViewById(R.id.bt_exit_login);
            btExitLogin.setVisibility(View.VISIBLE);
            CircleImageView ivUserHeader= (CircleImageView) view.findViewById(R.id.iv_user_header);
            TextView tvUserName=(TextView)view.findViewById(R.id.tv_user_name);
            GlideImageLoader.loadImage(this,userInfoEntity.getProfile().getPortrait(),R.mipmap.icon_user_default,ivUserHeader);
            tvUserName.setText(userInfoEntity.getProfile().getNickname());
        }
    }

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
            titleBar.setTitleBarTitle("中超赛场-数据");
            tabQiuDui.setTabChecked(false);
            tabJiFen.setTabChecked(true);
            tabXinWen.setTabChecked(false);
            tabYaGuan.setTabChecked(false);
            ivGame.setSelected(false);
            tag="score/database";
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
