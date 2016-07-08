package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseActivity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.ToastUtil;
import com.tysci.cls.utils.UserInfoUtils;
import com.tysci.cls.views.dialogs.LoadingProgressDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/1/6.
 */
public class RegisterActivity extends BaseActivity {
    @Bind(R.id.et_user_phone)
    protected EditText etUserPhone;
    @Bind(R.id.et_user_password)
    protected EditText etUserPassword;
    @Bind(R.id.et_user_nick_name)
    protected EditText etUserNickName;
    @Bind(R.id.et_register_vcode)
    protected EditText etVcode;
    @Bind(R.id.tv_get_vcode)
    protected TextView tvGetVCode;

    private String userPhone;
    private String userPassword;
    private String userNickName;

    private LoadingProgressDialog loadingProgressDialog=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        setTitle("手机注册");
    }

    private void showProgressDialog(String msg){
        if(loadingProgressDialog==null){
            loadingProgressDialog=new LoadingProgressDialog(this);
        }
        loadingProgressDialog.setMessage(msg);
        loadingProgressDialog.show();
    }

    private void dimssProgressDialog(){
        if(loadingProgressDialog!=null&&loadingProgressDialog.isShowing()){
            loadingProgressDialog.dismiss();
        }
    }

    @OnClick(R.id.bt_register)
    protected void onRegister(View view){
         userPhone=etUserPhone.getText().toString();
         userPassword=etUserPassword.getText().toString();
         userNickName=etUserNickName.getText().toString();

        if(TextUtils.isEmpty(userPhone)){
            ToastUtil.show(this, "请输入手机号码");
            etUserPhone.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            ToastUtil.show(this, "请输入密码");
            etUserPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userNickName)){
            ToastUtil.show(this, "请输入用户昵称");
            etUserNickName.requestFocus();
            return;
        }

        if(userNickName.length()<2){
           ToastUtil.show(this, "昵称至少为2个字符组成");
            etUserNickName.requestFocus();
            return;
        }

        showProgressDialog("注册请求中...");
        String url= HttpUrls.HTTP_HOST_URL+"user/register";
        Map<String,String> params=new HashMap<>(2);
        params.put("account",userPhone);
        params.put("password",userPassword);
        params.put("nickname",userNickName);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                ToastUtil.show(RegisterActivity.this,"请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject resultObj= JSONObject.parseObject(response);
                    if(resultObj!=null&&!resultObj.isEmpty()){
                        String message=resultObj.getString("message");
                        if(!TextUtils.isEmpty(message)){
                            ToastUtil.show(RegisterActivity.this, message);
                        }
                        if(resultObj.getIntValue("code")==200){
                            JSONObject dataObj=resultObj.getJSONObject("data");
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                String token=dataObj.getString("token");
                                //String userId=dataObj.getString("userId");
                                String account=dataObj.getString("account");
                                if(!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(account)){
                                    UserInfoUtils.setUserToken(RegisterActivity.this, token);
                                    // UserInfoUtils.setUserId(RegisterActivity.this, userId);
                                    UserInfoUtils.setUserAccount(RegisterActivity.this, account);
                                    setResult(RESULT_OK);
                                    finish();
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                dimssProgressDialog();
            }
        });
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
