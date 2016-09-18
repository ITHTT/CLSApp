package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tysci.cls.R;
import com.tysci.cls.app.BaseActivity;
import com.tysci.cls.utils.KLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CLSUserAgreementActivity extends BaseActivity{
    @Bind(R.id.tv_agreement_info)
    protected TextView tvAgreementInfo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initViews() {
        setTitle("用户协议");

        String agreementInfo=getUserAgreementInfo();
        KLog.e(agreementInfo);
        tvAgreementInfo.setText(Html.fromHtml(agreementInfo));
    }

    private String getUserAgreementInfo(){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("user_agreement_info.txt") );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            StringBuilder Result=new StringBuilder("");
            while((line = bufReader.readLine()) != null)
                Result.append(line);
            inputReader.close();
            return Result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
