package com.tysci.cls.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/12/14.
 */
public class SharedPreferencesUtil {
    /**默认的文件名*/
    private static final String default_file="share_cache_data";

    public static void setStringValue(Context context,String key,String value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void setStringValue(Context context,String fileName,String key,String value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getStringValue(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    public static String getStringValue(Context context,String fileName,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    public static void setIntValue(Context context,String key,int value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setIntValue(Context context,String fileName,String key,int value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public static int getIntValue(Context context,String fileName,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static void setLongValue(Context context,String key,long value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void setLongValue(Context context,String fileName,String key,long value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLongValue(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0l);
    }

    public static long getLongValue(Context context,String fileName,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0l);
    }

    public static void setFloatValue(Context context,String key,float value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void setFloatValue(Context context,String fileName,String key,float value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloatValue(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public static float getFloatValue(Context context,String fileName,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public static void setBooleanValue(Context context,String key,boolean value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setBooleanValue(Context context,String fileName,String key,boolean value){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBooleanValue(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(default_file,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static boolean getBooleanValue(Context context,String fileName,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

}
