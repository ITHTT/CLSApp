package com.tysci.cls.fragments;

import android.os.Bundle;
import android.view.View;

import com.tysci.cls.R;
import com.tysci.cls.app.BaseFragment;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchEventFragment extends BaseFragment{
    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_event;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
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
}
