package com.tysci.cls.app;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.utils.WeChatUtil;

/**
 * Created by Administrator on 2016/7/1.
 */
public class CLSApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
        AppConfigInfo.initAppConfigInfo(this);
        HttpClientUtil.initHttpClientUtil(this, AppConfigInfo.APP_HTTP_CACHE_PATH);
        WeChatUtil.registerWXApi(this);
    }
}
