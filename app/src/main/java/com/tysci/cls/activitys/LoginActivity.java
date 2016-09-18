package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
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
public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_use_phone)
    protected EditText etUserPhone;
    @Bind(R.id.et_user_password)
    protected EditText etUserPassword;

    private String userPhone;
    private String userPassword;

    private LoadingProgressDialog loadingProgressDialog=null;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        setTitle("登录");
    }

    private void showProgressDialog(String msg){
        if(loadingProgressDialog==null){
            loadingProgressDialog=new LoadingProgressDialog(this);
        }
        loadingProgressDialog.setMessage(msg);
        if(!loadingProgressDialog.isShowing()) {
            loadingProgressDialog.show();
        }
    }

    private void dimssProgressDialog(){
        if(loadingProgressDialog!=null&&loadingProgressDialog.isShowing()){
            loadingProgressDialog.dismiss();
        }
    }

    @OnClick(R.id.bt_login)
    protected void login(View view){
        userPhone=etUserPhone.getText().toString();
        userPassword=etUserPassword.getText().toString();
        if(TextUtils.isEmpty(userPhone)){
            ToastUtil.show(this, "请输入手机号码");
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            ToastUtil.show(this, "请输入密码");
            return;
        }
        showProgressDialog("登录中...");
        String url= HttpUrls.HTTP_HOST_URL+"auth/login";
        Map<String,String> params=new HashMap<String,String>(2);
        params.put("account",userPhone);
        params.put("password",userPassword);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
                ToastUtil.show(LoginActivity.this,"请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject resultObj= JSONObject.parseObject(response);
                    if(resultObj!=null&&!resultObj.isEmpty()){
                        String message=resultObj.getString("message");
                        if(!TextUtils.isEmpty(message)){
                            ToastUtil.show(LoginActivity.this, message);
                        }
                        int statusCode=resultObj.getIntValue("code");
                        if(statusCode==200){
                            JSONObject dataObj=resultObj.getJSONObject("data");
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                String token=dataObj.getString("token");
                                String userId=dataObj.getString("userId");
                                String account=dataObj.getString("account");
                                if(!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(account)&&!TextUtils.isEmpty(userId)){
                                    UserInfoUtils.setUserToken(LoginActivity.this, token);
                                    UserInfoUtils.setUserId(LoginActivity.this, userId);
                                    UserInfoUtils.setUserAccount(LoginActivity.this, account);
                                    loginHuanXinServer(account, account);
                                    UserInfoUtils.requestUserInfo(LoginActivity.this, Tag, userPhone, true, loadingProgressDialog);
                                    return;
                                }
                            }
                        }else{
                            ToastUtil.show(LoginActivity.this, message);
                        }
                    }
                }
                dimssProgressDialog();
                ToastUtil.show(LoginActivity.this, "登录失败");
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void loginHuanXinServer(String uid,String password){
        KLog.e("userId:"+uid);
        KLog.e("password:"+password);
        EMClient.getInstance().login(uid,password,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                UserInfoUtils.setUserHuanxinLoginFlag(LoginActivity.this,true);
                Log.d("main", "登录聊天服务器成功！");
               // UserInfoUtils.requestUserInfo(LoginActivity.this, Tag, userPhone, true, loadingProgressDialog);
            }

            @Override
            public void onProgress(int progress, String status) {
                KLog.e("status:"+status);
            }

            @Override
            public void onError(int code, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(LoginActivity.this, "登录失败");
                        KLog.e("message:" + message);
                        Log.d("main", "登录聊天服务器失败！");
                        //UserInfoUtils.clearUserInfo(LoginActivity.this);
                        //dimssProgressDialog();
                    }
                });
            }
        });
    }


//    @Subscriber(tag="login")
//    private void onLoignResponse(EventObject eventObject){
//        progressDialog.dismiss();
//        if(eventObject!=null){
//            int responseCode=eventObject.getResponseCode();
//            if(responseCode==EventObject.RESPONSE_CODE_OK){
//                String result= (String) eventObject.getDatas();
//                KLog.e("登录结果:" + result);
//                if(!TextUtils.isEmpty(result)){
//                    JSONObject resultObj=JSONObject.parseObject(result);
//                    if(resultObj!=null&&!resultObj.isEmpty()){
//                        String message=resultObj.getString("message");
//                        if(!TextUtils.isEmpty(message)){
//                            ToastMsgUtil.toastMsg(this, message);
//                        }
//                        int statusCode=resultObj.getIntValue("statusCode");
//                        if(statusCode==200){
//                            JSONObject dataObj=resultObj.getJSONObject("dataMap");
//                            if(dataObj!=null&&!dataObj.isEmpty()){
//                                String token=dataObj.getString("token");
//                                String userId=dataObj.getString("userId");
//                                String account=dataObj.getString("account");
//                                if(!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(account)){
//                                    UserInfoUtils.setUserToken(this, token);
//                                    UserInfoUtils.setUserId(this, userId);
//                                    UserInfoUtils.setUserAccount(this, account);
//                                    EventBus.getDefault().post("refresh_user_info", AppConstantParams.EVENT_REFRESH_USER_INFO);
//                                    this.setResult(RESULT_OK);
//                                    this.finish();
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                }
//                ToastMsgUtil.toastMsg(this, "登录失败");
//            }
//
//        }
//    }


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
