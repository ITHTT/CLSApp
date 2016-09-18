package com.tysci.cls.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.R;
import com.tysci.cls.modles.UserInfoEntity;
import com.tysci.cls.modles.event.EventObject;
import com.tysci.cls.modles.event.EventType;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.views.widgets.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by LinDe on 2016/6/16.
 *
 * @see AppCompatActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected final String Tag = this.getClass().getSimpleName();
    protected String language;
    protected TitleBar titleBar;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentViewId());
        titleBar = (TitleBar) this.findViewById(R.id.title_bar);
        setTitleBarLeftIcon(R.mipmap.arraw_left);
        if (!isCanceledEventBus()) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        initViews();
        if (getIntent() != null) {
            getIntentData(this.getIntent());
        }
        handleInstanceState(savedInstanceState);
    }

    protected void setTitle(String title) {
        if (titleBar != null) {
            titleBar.setTitleBarTitle(title);
        }
    }

    protected void setTitleRes(int res) {
        titleBar.setTitleBarTitle(res);
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    protected void setTitleBarLeftIcon(int res) {
        if (titleBar != null) {
            titleBar.setTitleBarLeftIcon(res, this);
        }
    }

    public String getLanguage() {
        return language;
    }

    /**
     * 获取界面布局文件的ID
     */
    protected abstract
    @LayoutRes
    int getContentViewId();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 设置加载效果所在布局的目标视图
     */
    protected abstract View getLoadingTargetView();

    /**
     * 获取Intent中的数据
     */
    protected abstract void getIntentData(Intent intent);

    /**
     * 是否取消EventBus
     */
    protected abstract boolean isCanceledEventBus();

    /**
     * 保存异常时的数据
     */
    protected abstract void saveInstanceState(Bundle outState);

    /**
     * 处理异常时的情况
     */
    protected abstract void handleInstanceState(Bundle outState);

    /**
     * 控件点击事件
     */
    protected abstract void onViewClick(View view);

    protected abstract void notifyEvent(String action);

    protected abstract void notifyEvent(String action, Bundle data);

    /**
     * 用户登录
     */
    protected void userLogin(UserInfoEntity userInfoEntity) {

    }

    /**
     * 用户退出
     */
    protected void userExit() {

    }

    protected void back() {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_titlebar_left) {
            back();
        }
        onViewClick(v);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceState(outState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventObject eventObject) {
        if (eventObject != null) {
            String action = eventObject.getEventAction();
            if (action.equals(EventType.EVENT_USER_LOGIN)) {
                String data = eventObject.getData().getString("user_info");
                if (!TextUtils.isEmpty(data)) {
                    UserInfoEntity userInfoEntity = JSONObject.parseObject(data, UserInfoEntity.class);
                    if (userInfoEntity != null) {
                        userLogin(userInfoEntity);
                    }
                }
            } else {
                SparseArray<Class> receivers = eventObject.getReceivers();
                if (receivers.size() > 0) {
                    int size = receivers.size();
                    for (int i = 0; i < size; i++) {
                        if (receivers.valueAt(i) == this.getClass()) {
                            notifyEvent(action, eventObject.getData());
                        }
                    }
                } else {
                    notifyEvent(action, eventObject.getData());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(String action) {
        if (action.equals(EventType.EVENT_USER_EXIT)) {
            userExit();
        }
        if (action.equals(EventType.EVENT_SWITCH_APP_LANGUAGE)) {
            finish();
        } else {
            notifyEvent(action);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**取消网络请求*/
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        ButterKnife.unbind(this);
        if (!isCanceledEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
