package com.tysci.cls.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.modles.UserInfoEntity;
import com.tysci.cls.modles.event.EventObject;
import com.tysci.cls.modles.event.EventType;
import com.tysci.cls.networks.HttpClientUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by HTT on 2016/5/28.
 */
public abstract class BaseFragment extends Fragment {
    protected final String Tag = this.getClass().getSimpleName();
    protected BaseActivity baseActivity;
    protected View contentView;

    protected String language;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = baseActivity.getLanguage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isCanceledEventBus()) {
            EventBus.getDefault().register(this);
        }
        View view = inflater.inflate(getViewLayoutId(), container, false);
        contentView = view;
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.baseActivity = (BaseActivity) context;
    }

    /**
     * 获取布局ID
     */
    protected abstract
    @LayoutRes
    int getViewLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initViews(View view, Bundle savedInstanceState);

    /**
     * 设置加载效果所在布局的目标视图
     */
    protected abstract View getLoadingTargetView();

    /**
     * 是否取消EventBus
     */
    protected abstract boolean isCanceledEventBus();

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
        } else {
            notifyEvent(action);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpClientUtil.getHttpClientUtil().cancelTag(Tag);
        ButterKnife.unbind(this);
        if (!isCanceledEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
