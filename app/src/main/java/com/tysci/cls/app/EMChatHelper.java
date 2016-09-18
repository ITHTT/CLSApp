package com.tysci.cls.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.tysci.cls.utils.KLog;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class EMChatHelper {
    private static final String TAG=EMChatHelper.class.getSimpleName();
    private static EMChatHelper chatHelper;


    public synchronized static EMChatHelper getChatHelper(){
        if(chatHelper==null){
            chatHelper=new EMChatHelper();
        }
        return chatHelper;
    }

    public void initEMChatHelper(Context context){
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(context,pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase(context.getPackageName())) {
            KLog.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options=getChatOptions();
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        EMClient.getInstance().addConnectionListener(new ChatConnectionListener());

    }


    private EMOptions getChatOptions(){
        KLog.e(TAG,"init HuanXin Options");

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

//        //you need apply & set your own id if you want to use google cloud messaging.
//        options.setGCMNumber("324169311137");
//        //you need apply & set your own id if you want to use Mi push notification
//        options.setMipushConfig("2882303761517426801", "5381742660801");
//        //you need apply & set your own id if you want to use Huawei push notification
//        options.setHuaweiPushAppId("10492024");
//
//        options.allowChatroomOwnerLeave(true);
//        options.setDeleteMessagesAsExitGroup(true);
//        options.setAutoAcceptGroupInvitation(true);
        return options;
    }

    private String getAppName(Context context,int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static final class ChatConnectionListener implements EMConnectionListener{

        @Override
        public void onConnected() {
            KLog.e("连接成功...");
        }

        @Override
        public void onDisconnected(int error) {

            if(error == EMError.USER_REMOVED){
                // 显示帐号已经被移除
                KLog.e("账号被移除。。。");
            }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登录
                KLog.e("账号在其他设备登录...");

            } else if(error==EMError.GENERAL_ERROR){

            }
        }
    }


}
