package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tysci.cls.R;
import com.tysci.cls.app.BaseActivity;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LoginOrRegisterActivity extends BaseActivity{
    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_register;
    }

    @Override
    protected void initViews() {
        setTitle("登录/注册");
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

    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }
}
