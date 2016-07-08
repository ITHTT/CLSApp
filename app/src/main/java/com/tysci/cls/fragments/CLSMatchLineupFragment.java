package com.tysci.cls.fragments;

import android.os.Bundle;
import android.view.View;

import com.tysci.cls.R;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchLineupFragment extends BaseFragment{
    private CLSMatchEntity matchEntity;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_lineup;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        Bundle data=getArguments();
        if(data!=null){
            matchEntity=data.getParcelable("match_info");
            if(matchEntity!=null){
                requestMatchLineupInfo(matchEntity.getId());
            }
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void requestMatchLineupInfo(int matchId){
        String url= HttpUrls.HTTP_HOST_URL+"match/detail/lineups/"+matchId;
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);

            }

            @Override
            public void onFinish(Call call) {

            }
        });
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
