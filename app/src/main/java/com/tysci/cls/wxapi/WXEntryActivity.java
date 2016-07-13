package com.tysci.cls.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tysci.cls.R;
import com.tysci.cls.activitys.LoginActivity;
import com.tysci.cls.activitys.RegisterActivity;
import com.tysci.cls.app.BaseActivity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.ToastUtil;
import com.tysci.cls.utils.UserInfoUtils;
import com.tysci.cls.utils.WeChatUtil;
import com.tysci.cls.views.dialogs.LoadingProgressDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/1/13.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI iwxApi=null;

    private LoadingProgressDialog loadingProgressDialog=null;
    /**登录标记*/
    private  String loginTag=null;

    private final int REQUEST_CODE_USER_LOGIN=0x0001;
    private final int REQUEST_CODE_USER_REGISTER=0x0002;
    private final int REQUEST_CODE_USER_WECHAT_LOGIN=0x0003;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login_register;
    }

    @Override
    protected void initViews() {
        setTitle("登录/注册");
        iwxApi= WeChatUtil.wxApi;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        if(iwxApi != null) {
            KLog.e("注册回调方法...");
            iwxApi.handleIntent(intent, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dimssProgressDialog();
    }

    @OnClick({R.id.tv_phone_login,R.id.tv_phone_register})
    protected void onUserLoginOrRegister(View view){
        int id=view.getId();
        Intent intent=null;
        switch (id){
            case R.id.tv_phone_register:
                intent=new Intent(this,RegisterActivity.class);
                this.startActivityForResult(intent,REQUEST_CODE_USER_REGISTER);
                break;
            case R.id.tv_phone_login:
                intent=new Intent(this,LoginActivity.class);
                startActivityForResult(intent,REQUEST_CODE_USER_LOGIN);
                break;
        }
    }

    @OnClick(R.id.tv_wechat_login)
    protected void onWechatLogin(View view){
        if(iwxApi!=null){
            boolean isInstalled=iwxApi.isWXAppInstalled()&&iwxApi.isWXAppSupportAPI();
            if(isInstalled){
                final com.tencent.mm.sdk.modelmsg.SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "none";
                showProgressDialog("加载中...");
                KLog.e("发送微信登录请求...");
                boolean isSuccess=iwxApi.sendReq(req);
                KLog.e("是否成功:"+isSuccess);
            }else {
                ToastUtil.show(this, "请安装微信客户端");
            }
        }
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

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        KLog.e("错误码:" + baseResp.errCode);
        switch(baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                //KLog.e("授权成功");
                //if(!TextUtils.isEmpty(loginTag)){
                    SendAuth.Resp resp= (SendAuth.Resp) baseResp;
                    getWxToken(WeChatUtil.APP_ID_WECHAT, WeChatUtil.APP_SECRET_WECHAT, resp.code);
               // }else{
                    //EventBus.getDefault().post("Share_successed","WECHAT_SHARE");
               // }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastUtil.show(this, "微信拒绝授权");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastUtil.show(this, "用户取消授权");
                break;
        }
    }

    private void getWxToken(String app_id,String app_secret,String code){
        showProgressDialog("加载中...");
        String url= HttpUrls.GET_WECHAT_TOKEN_URL+"?appid="+app_id
                +"&secret="+app_secret
                +"&code="+code
                +"&grant_type=authorization_code";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
                ToastUtil.show(WXEntryActivity.this,"请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject object=JSONObject.parseObject(response);
                    if(object!=null&&!object.isEmpty()){
                        String token=object.getString("access_token");
                        if(!TextUtils.isEmpty(token)){
                            UserInfoUtils.setUserWechatInfo(WXEntryActivity.this, response);
                            getWxUserInfo(object);
                            //this.finish();
                            return;
                        }
                    }
                }
                dimssProgressDialog();
                ToastUtil.show(WXEntryActivity.this, "登录失败");
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

    private void getWxUserInfo(JSONObject userInfoObj){
        if(userInfoObj!=null&&!userInfoObj.isEmpty()){
           String token= userInfoObj.getString("access_token");
            String openId=userInfoObj.getString("openid");
            String url=HttpUrls.GET_WECHAT_USER_IFNO_URL+"?access_token="+token+"&openid="+openId;
            HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
                @Override
                public void onBefore(Request request) {

                }

                @Override
                public void onError(Call call, Exception error) {
                    dimssProgressDialog();
                    ToastUtil.show(WXEntryActivity.this,"请求失败");
                }

                @Override
                public void onSuccess(Call call, String response) {
                    KLog.json(response);
                    if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        Map<String,String>params=new HashMap<>(10);
                        params.put("openid",obj.getString("openid"));
                        params.put("unionid",obj.getString("unionid"));
                        params.put("nickname",obj.getString("nickname"));
                        params.put("sex",obj.getString("sex"));
                        params.put("headimgurl",obj.getString("headimgurl"));
                        params.put("province",obj.getString("province"));
                        params.put("city",obj.getString("city"));
                        params.put("country",obj.getString("country"));
                        params.put("privilege",obj.getString("privilege"));
                        userWechatLogin(params);
                        return;
                    }
                   }
                    dimssProgressDialog();
                    ToastUtil.show(WXEntryActivity.this, "登录失败");
                }

                @Override
                public void onFinish(Call call) {

                }
            });
        }
    }

    private void userWechatLogin(Map<String,String>params){
        String url=HttpUrls.HTTP_HOST_URL+"auth/login4wx";
        //String url="http://tsi-pc-001:8080/app/api/v1/auth/login4wx";
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendPostRequest(Tag, url, params, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                dimssProgressDialog();
                ToastUtil.show(WXEntryActivity.this,"请求失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject resultObj=JSONObject.parseObject(response);
                    if(resultObj!=null&&!resultObj.isEmpty()){
                        String message=resultObj.getString("message");
                        if(!TextUtils.isEmpty(message)){
                            ToastUtil.show(WXEntryActivity.this, message);
                        }
                        int statusCode=resultObj.getIntValue("code");
                        if(statusCode==200){
                            JSONObject dataObj=resultObj.getJSONObject("data");
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                String token=dataObj.getString("token");
                                String userId=dataObj.getString("userId");
                                String account=dataObj.getString("account");
                                if(!TextUtils.isEmpty(token)&&!TextUtils.isEmpty(account)){
                                    UserInfoUtils.setUserToken(WXEntryActivity.this, token);
                                    UserInfoUtils.setUserId(WXEntryActivity.this, userId);
                                    UserInfoUtils.setUserAccount(WXEntryActivity.this, account);
                                    UserInfoUtils.requestUserInfo(WXEntryActivity.this, Tag, account, true, loadingProgressDialog);
                                    //finish();
                                    return;
                                }
                            }
                        }
                    }
                }
                dimssProgressDialog();
                ToastUtil.show(WXEntryActivity.this,"登录失败");
            }

            @Override
            public void onFinish(Call call) {

            }
        });
    }

//    @Subscriber(tag="event_get_wechat_token")
//    private void onResponse(EventObject eventObject){
//        if(eventObject!=null){
//            int responseCode=eventObject.getResponseCode();
//            if(responseCode==EventObject.RESPONSE_CODE_OK){
//                String result=(String)eventObject.getDatas();
//                KLog.e("RESULT:"+result);
//                if(!TextUtils.isEmpty(result)){
//                    JSONObject object=JSONObject.parseObject(result);
//                    if(object!=null&&!object.isEmpty()){
//                        String token=object.getString("access_token");
//                        if(!TextUtils.isEmpty(token)){
//                            UserInfoUtils.setUserWechatInfo(this,result);
//                            getWxUserInfo(object);
//                            //this.finish();
//                            return;
//                        }
//                    }
//                }
//            }else if(responseCode==EventObject.RESPONSE_CODE_FAIL){
//                dimssProgressDialog();
//                ToastMsgUtil.toastMsg(this, "登录失败");
//            }
//        }
//    }

//    @Subscriber(tag="event_get_wechat_user_info")
//    private void onGetWechatUserInfo(EventObject eventObject){
//        if(eventObject!=null){
//            int responseCode=eventObject.getResponseCode();
//            if(responseCode==EventObject.RESPONSE_CODE_OK){
//                String response= (String) eventObject.getDatas();
//                KLog.e("加载的用户信息数据:" + response);
//                if(!TextUtils.isEmpty(response)){
//                    JSONObject obj=JSONObject.parseObject(response);
//                    if(obj!=null&&!obj.isEmpty()){
//                        Map<String,String>params=new HashMap<>(10);
//                        params.put("openid",obj.getString("openid"));
//                        params.put("unionid",obj.getString("unionid"));
//                        params.put("nickname",obj.getString("nickname"));
//                        params.put("sex",obj.getString("sex"));
//                        params.put("headimgurl",obj.getString("headimgurl"));
////                        params.put("province",obj.getString("province"));
////                        params.put("city",obj.getString("city"));
////                        params.put("country",obj.getString("country"));
////                        params.put("privilege",obj.getString("privilege"));
//                        userWechatLogin(params);
//                        return;
//                    }
//                }
//            }else if(responseCode==EventObject.RESPONSE_CODE_FAIL){
//                dimssProgressDialog();
//                ToastMsgUtil.toastMsg(this,"登录失败");
//            }
//        }
//    }

//    @Subscriber(tag="event_user_wechat_login")
//    private void onUserWechatLoginResponse(EventObject eventObject){
//        dimssProgressDialog();
//        KLog.e("登录响应...");
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
//                                    EventBus.getDefault().post(loginTag, AppConstantParams.EVENT_USER_LOGIN_SUCCESS);
//                                    PullService.startPullService(this);
//                                    this.finish();
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            KLog.e("登录失败");
//            if(eventObject.getError()!=null){
//                KLog.e("异常信息:"+eventObject.getError().getMessage());
//            }
//            ToastMsgUtil.toastMsg(this, "登录失败");
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            KLog.e("用户登录成功。。。");
//            EventBus.getDefault().post(loginTag, AppConstantParams.EVENT_USER_LOGIN_SUCCESS);
//            PullService.startPullService(this);
            this.setResult(RESULT_OK);
            this.finish();
        }
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
