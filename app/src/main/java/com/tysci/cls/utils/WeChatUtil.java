package com.tysci.cls.utils;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2016/5/31.
 */
public class WeChatUtil {
    public static final String APP_ID_WECHAT = "wx5704c7e4309ad048";
    public static final String APP_SECRET_WECHAT = "d6f97c142241b63667b6e7237077236b";
    public static final String APP_MCH_ID_WECHAT = "1235168302";

    public static final String OPEN_ID="open_id";

    public static IWXAPI wxApi;

    public static void registerWXApi(Context context){
        wxApi= WXAPIFactory.createWXAPI(context, APP_ID_WECHAT, true);
        wxApi.registerApp(APP_ID_WECHAT);
    }

    /**
     * 微信登录
     * @param context
     * @return
     */
    public static boolean weChatLogin(Context context) {
        if(wxApi!=null) {
            boolean isInstalled = wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI();
            if (!isInstalled) {
                Toast.makeText(context, "请先安装微信APP",Toast.LENGTH_SHORT);
                return false;
            }
            // send oauth request
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            wxApi.sendReq(req);
            return true;
        }
        return false;
    }

//    public static void setOpenId(Context context,String openId){
//        SharedPreferencesUtil.setStringValue(context,OPEN_ID,openId);
//    }
//
//    public static String getOpenId(Context context){
//        return SharedPreferencesUtil.getStringValue(context,OPEN_ID);
//    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

//    public static HashMap<String,String> getWeChatPayParams(Context context,String etype,float moneys){
//        KLog.e("moneys:"+moneys);
//        KLog.e("type:"+etype);
//        HashMap<String,String>params=new HashMap<>();
//        params.put("appid", APP_ID_WECHAT);
//        params.put("mch_id", APP_MCH_ID_WECHAT);
//        params.put("nonce_str", buildTransaction(APP_MCH_ID_WECHAT));
//        params.put("product_id", buildTransaction(APP_MCH_ID_WECHAT));
//        params.put("body", "Wechat");
//        params.put("total_fee", (int) (moneys * 100) + "");
//        params.put("openid",getOpenId(context));
//        params.put("bounty_type", etype /*"article" : "tip"*/);
//        return params;
//    }

//    public static void weChatPay(JSONObject data){
//        PayReq req = new PayReq();
//
//        req.appId = data.getString("appid");
//        req.partnerId = data.getString("partnerid");
//        req.prepayId = data.getString("prepayid");
//        req.packageValue = data.getString("package");
//        req.nonceStr = data.getString("noncestr");
//        req.timeStamp = data.getString("timestamp");
//        req.sign = data.getString("paySign");
//        wxApi.sendReq(req);
//    }
//
//    /**
//     * 微信分享
//     * @param context
//     * @param title
//     * @param excerpt
//     * @param shareUrl
//     * @param tag
//     * @param logo
//     * @return
//     */
//    public static boolean shareWebPage(Context context,int tag,String title, String excerpt, String shareUrl) {
//        if(wxApi!=null) {
//            boolean isInstalled = wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI();
//            if (!isInstalled) {
//                Toast.makeText(context, "请先安装微信APP", Toast.LENGTH_SHORT);
//                return false;
//            }
//            WXWebpageObject webPage = new WXWebpageObject();
//            webPage.webpageUrl = shareUrl;
//            WXMediaMessage msg = new WXMediaMessage(webPage);
//            if (title != null && !"".equals(title)) {
//                if (title.length() < 100) msg.title = title;
//                else msg.title = title.substring(0, 100);
//            } else {
//                msg.title = "球商";
//            }
//            if (excerpt != null && !"".equals(excerpt)) {
//                if (excerpt.length() < 100) {
//                    msg.description = excerpt;
//                } else {
//                    msg.description = excerpt.substring(0, 100);
//                }
//            } else {
//                msg.description = "竞猜";
//            }
//            Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_ballq_logo);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            thumb.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            msg.thumbData = baos.toByteArray();
//            try {
//                baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("webpage");
//            req.message = msg;
//            req.scene = tag;
//            req.openId = getOpenId(context);
//            wxApi.sendReq(req);
//            return true;
//        }
//        return false;
//    }
}
