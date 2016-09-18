package com.tysci.cls.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tysci.cls.modles.UserInfoEntity;
import com.tysci.cls.modles.event.EventObject;
import com.tysci.cls.modles.event.EventType;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.views.dialogs.LoadingProgressDialog;
import com.tysci.cls.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/1/13.
 */
public class UserInfoUtils {
    private static final String userFileName="user_info";

    private static final String USER_TOKEN="user_token";
    private static final String USER_ACCOUNT="user_account";
    private static final String USER_ID="usr_id";

    private static final String USER_SELECTED_GAME_TYPE="user_selected_game_type";
    private static final String USER_INFO="user_info";

    private static final String USER_WECHAT_INFO="user_wechat_info";

    private static final String USER_HUANXIN_LOGIN_FLAG="user_huanxin_loign_flag";

    public static void setUserToken(Context context,String token){
        SharedPreferencesUtil.setStringValue(context,userFileName,USER_TOKEN,token);
    }

    public static String getUserToken(Context context){
        return SharedPreferencesUtil.getStringValue(context,userFileName,USER_TOKEN);
    }

    public static void setUserAccount(Context context ,String phone){
        SharedPreferencesUtil.setStringValue(context,userFileName,USER_ACCOUNT,phone);
    }

    public static String getUserAccount(Context context){
        return SharedPreferencesUtil.getStringValue(context,userFileName,USER_ACCOUNT);
    }

    public static void setUserId(Context context,String id){
        SharedPreferencesUtil.setStringValue(context,userFileName,USER_ID,id);
    }

    public static String getUserId(Context context){
        return SharedPreferencesUtil.getStringValue(context,userFileName,USER_ID);
    }

    public static void setUserSelectedGameType(Context context,List<String> types){
        String str=JSONObject.toJSONString(types);
        SharedPreferencesUtil.setStringValue(context,userFileName,USER_SELECTED_GAME_TYPE,str);
    }

    public static List<String> getUserSelectedGameType(Context context){
        String str=SharedPreferencesUtil.getStringValue(context, userFileName, USER_SELECTED_GAME_TYPE);
        if(!TextUtils.isEmpty(str)){
            return JSONObject.parseArray(str,String.class);
        }
        return null;
    }

    public static void setUserInfo(Context context,UserInfoEntity userInfo){
        String info=JSONObject.toJSONString(userInfo);
        if(!TextUtils.isEmpty(info)){
            SharedPreferencesUtil.setStringValue(context,userFileName,USER_INFO,info);
        }
    }

    public static void setUserInfo(Context context,String userInfo){
        if(!TextUtils.isEmpty(userInfo)){
            SharedPreferencesUtil.setStringValue(context,userFileName,USER_INFO,userInfo);
        }
    }

    public static UserInfoEntity getUserInfo(Context context){
        String userInfo=SharedPreferencesUtil.getStringValue(context, userFileName, USER_INFO);
        if(!TextUtils.isEmpty(userInfo)){
            return JSONObject.parseObject(userInfo, UserInfoEntity.class);
        }
        return null;
    }

    public static void setUserWechatInfo(Context context,String info){
        SharedPreferencesUtil.setStringValue(context,userFileName,USER_WECHAT_INFO,info);
    }

    public static JSONObject getUserWechatInfo(Context context){
       String userInfo= SharedPreferencesUtil.getStringValue(context,userFileName,USER_WECHAT_INFO);
        if(!TextUtils.isEmpty(userInfo)){
            return JSONObject.parseObject(userInfo);
        }
        return null;
    }

    public static void setUserHuanxinLoginFlag(Context context,boolean flag){
        SharedPreferencesUtil.setBooleanValue(context,userFileName,USER_HUANXIN_LOGIN_FLAG,flag);
    }

    public static boolean getUserHuanxinLoginFlag(Context context){
        return SharedPreferencesUtil.getBooleanValue(context,userFileName,USER_HUANXIN_LOGIN_FLAG);
    }

    public static boolean checkLogin(Context context){
        String token=getUserToken(context);
        String id=getUserId(context);
        if(TextUtils.isEmpty(token)||TextUtils.isEmpty(id)){
            return false;
        }
        return true;
    }

    public static void startLogin(Context context){
        Intent intent=new Intent(context, WXEntryActivity.class);
        context.startActivity(intent);
    }

    public static void clearUserInfo(Context context){
        setUserId(context, "");
        setUserToken(context, "");
        setUserWechatInfo(context, "");
        setUserAccount(context, "");
        setUserHuanxinLoginFlag(context,false);
        SharedPreferencesUtil.setStringValue(context, userFileName, USER_INFO, "");
    }

    public static void exitLogin(final Activity context){
        //setUserAccount(context,"");
        final LoadingProgressDialog progressDialog=new LoadingProgressDialog(context);
        progressDialog.setMessage("退出中...");
        progressDialog.show();
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                setUserId(context, "");
                setUserToken(context, "");
                setUserWechatInfo(context, "");
                setUserAccount(context, "");
                SharedPreferencesUtil.setStringValue(context, userFileName, USER_INFO, "");
                EventBus.getDefault().post(EventType.EVENT_USER_EXIT);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(context, "退出成功");
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(context, "退出登录失败");
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    public static void requestUserInfo(final Activity context,String tag,String phone, final boolean isLogin, final LoadingProgressDialog progressDialog){
        String url= HttpUrls.HTTP_HOST_URL+"user/info?account="+phone;
        HashMap<String,String>headers=new HashMap<>(1);
        headers.put("token", UserInfoUtils.getUserToken(context));
        HttpClientUtil.getHttpClientUtil().sendGetRequest(tag, url, 0, headers, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if (isLogin) {
                    exitLogin(context);
                }
                ToastUtil.show(context, "登录失败");
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if (!TextUtils.isEmpty(response)) {
                    JSONObject obj = JSONObject.parseObject(response);
                    if (obj != null && !obj.isEmpty()) {
                        int code = obj.getIntValue("code");
                        if (code == 200) {
                            JSONObject dataObj = obj.getJSONObject("data");
                            if (dataObj != null && !dataObj.isEmpty()) {
                                String userInfo = dataObj.getString("user");
                                if (!TextUtils.isEmpty(userInfo)) {
                                    UserInfoUtils.setUserInfo(context, userInfo);
                                    EventObject eventObject = new EventObject();
                                    eventObject.getData().putString("user_info", userInfo);
                                    if (isLogin) {
                                        ToastUtil.show(context, "登录成功");
                                        EventObject.postEventObject(eventObject, EventType.EVENT_USER_LOGIN);
                                        context.setResult(Activity.RESULT_OK);
                                        context.finish();
                                    } else {
                                        EventObject.postEventObject(eventObject, EventType.EVENT_REFRESH_USER_INFO);
                                    }
                                }
                            }
                        }else{
                            if(!isLogin){
                                KLog.e("用户信息不符,退出操作。。。");
                                UserInfoUtils.exitLogin(context);
                                UserInfoUtils.clearUserInfo(context);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }


    public static void getUserVisitInfo(Context context){
        String phone=UserInfoUtils.getUserAccount(context);
        String url=HttpUrls.HTTP_HOST_URL+ "user/log?account="+phone;
        HashMap<String,String>headers=new HashMap<>(1);
        headers.put("token", UserInfoUtils.getUserToken(context));
        HttpClientUtil.getHttpClientUtil().sendGetRequest("user_log", url,0,headers, new HttpClientUtil.StringResponseCallBack() {
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







}
