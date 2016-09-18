package com.tysci.cls.app;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2015/11/26.
 */
public class AppConfigInfo {
    public static final String APP_DIR_NAME="CLSApp";
    public static String APP_PATH=null;
    public static final String APP_HTTP_CACHE_DIR_NAME="httpcache";
    public static String APP_HTTP_CACHE_PATH=null;
    public static final String APP_DATABASE_DIR_NAME="database";
    //public static final String APP_DATABASE_NAME="chat.db";
    public static String APP_DATABASE_PATH=null;
    public static final String APP_LOG_DIR_NAME="log";
    public static String APP_LOG_PATH=null;
    public static final String APP_IMAGE_DIR_NAME="imagecache";
    public static String APP_IMAGE_PATH=null;
    public static final String APP_VIDEO_DIR_NAME="videos";
    public static String APP_VIDEO_PATH=null;


    private static boolean isInit=false;

    public static void initAppConfigInfo(Context context){
        if(!isInit) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            File dir = null;
            if (sdCardExist) {
                dir = new File(Environment.getExternalStorageDirectory() + File.separator + APP_DIR_NAME);
            } else {
                dir = new File(context.getFilesDir() + File.separator + APP_DIR_NAME);
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            APP_PATH = dir.getAbsolutePath();

            File DBDir = new File(APP_PATH + File.separator + APP_DATABASE_DIR_NAME);
            if (!DBDir.exists()) {
                DBDir.mkdirs();
            }
            APP_DATABASE_PATH = DBDir.getAbsolutePath();

            File logDir = new File(APP_PATH + File.separator + APP_LOG_DIR_NAME);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            APP_LOG_PATH = logDir.getAbsolutePath();

            File httpCacheDir = new File(APP_PATH + File.separator + APP_HTTP_CACHE_DIR_NAME);
            if (!httpCacheDir.exists()) {
                httpCacheDir.mkdirs();
            }
            APP_HTTP_CACHE_PATH = httpCacheDir.getAbsolutePath();

            File imageDir = new File(APP_PATH + File.separator + APP_IMAGE_DIR_NAME);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            APP_IMAGE_PATH = imageDir.getAbsolutePath();

            File videoDir=new File(APP_PATH+File.separator+APP_VIDEO_DIR_NAME);
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }
            APP_VIDEO_PATH = videoDir.getAbsolutePath();
            isInit=true;
        }

//        File videoDir=new File(APP_PATH+File.separator+APP_VIDEO_DIR_NAME);
//        if(!videoDir.exists()){
//            videoDir.mkdirs();
//        }
//        APP_VIDEO_PATH=videoDir.getAbsolutePath();
    }

    public static String getAppException(){
        return APP_LOG_PATH+File.separator+System.currentTimeMillis()+".log";
    }
}
