package com.tysci.cls.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.modles.UserInfoEntity;
import com.tysci.cls.wxapi.WXEntryActivity;

import java.util.List;

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

    public static boolean checkLogin(Context context){
        String token=getUserToken(context);
        if(TextUtils.isEmpty(token)){
            return false;
        }
        return true;
    }

    public static void startLogin(Context context){
        Intent intent=new Intent(context, WXEntryActivity.class);
        context.startActivity(intent);
    }

    public static void exitLogin(Context context){
        //setUserAccount(context,"");
        setUserId(context,"");
        setUserToken(context, "");
        setUserWechatInfo(context, "");
        setUserAccount(context, "");
        SharedPreferencesUtil.setStringValue(context, userFileName, USER_INFO, "");
    }




}
