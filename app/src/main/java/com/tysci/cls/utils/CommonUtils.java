package com.tysci.cls.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/5/31.
 */
public class CommonUtils {

    public static void getJSONListObject(JSONArray jsonArray,List list,Class cls){
        if(jsonArray!=null&&!jsonArray.isEmpty()&&list!=null){
            int size=jsonArray.size();
            for(int i=0;i<size;i++){
                list.add(jsonArray.getObject(i,cls));
            }
        }
    }

    public static void getListDatas(List listDatas,List sourceDatas){
        if(listDatas!=null&&sourceDatas!=null){
            if(listDatas.size()>0){
                listDatas.clear();
            }
            int size=sourceDatas.size();
            for(int i=0;i<size;i++){
                listDatas.add(sourceDatas.get(i));
            }
        }
    }

    public static void addListDatas(List listDatas,List sourceDatas){
        if(listDatas != null && sourceDatas!=null&&sourceDatas.size()>0){
            int size=sourceDatas.size();
            for(int i=0;i<size;i++){
                listDatas.add(sourceDatas.get(i));
            }

        }
    }

    public static DisplayMetrics getScreenDisplayMetrics(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String getDateAndTimeFormatString(long times){
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(times));
    }

    public static String getDateAndTimeFormatString(Date times){
        return new SimpleDateFormat("MM-dd HH:mm").format(times);
    }

    public static String getDayOfMonth(Date date){
        return new SimpleDateFormat("dd").format(date);
    }

    public static String getMMddString(Date date){
        return new SimpleDateFormat("MM/dd HH:mm").format(date);
    }

    public static String getMMdd(Date date){
        return new SimpleDateFormat("MM/dd").format(date);
    }

    public static String getMM_ddString(Date date){
        return new SimpleDateFormat("MM-dd").format(date);
    }

    public static String getMM_ddString(long date){
        return new SimpleDateFormat("MM-dd").format(new Date(date));
    }


    public static String getTimeOfDay(long times){
        return new SimpleDateFormat("HH:mm").format(new Date(times));
    }


    public static String getTimeOfDay(Date date){
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static Date getDifferenceDaysDate(long times, int diffDay){
        Calendar now = Calendar.getInstance();
        Date d = new Date();
        d.setTime(times);
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE)+diffDay);
        return now.getTime();
    }

    public static String getYYMMdd(Date date){
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }

    public static Date getDateAndTimeFromGMT(String GMTDate){
        if (GMTDate.length() > 20)
        {
            GMTDate = GMTDate.substring(0, 19) + "Z";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        try {
            final Date d = sdf.parse(GMTDate);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeFormatName(long times){
        String timeName=null;
        //KLog.e("当前日期:" +getFormatDateString(times));
        long currentTimes=System.currentTimeMillis()/1000;
        times/=1000;
        long offsetTimes=currentTimes-times;
        if(offsetTimes<60){
            timeName="刚刚";
        }else if(offsetTimes<60*60){
            timeName=offsetTimes/60+"分钟前";
        }else if(offsetTimes<=60*60*8){
            timeName=offsetTimes/(60*60)+"小时前";
        }else if(offsetTimes<60*60*24*2){
            timeName="1天前";
        }else if(offsetTimes<60*60*24*4){
            //KLog.e("offsetTimes:"+offsetTimes);
            timeName=offsetTimes/(60*60*24)+"天前";
        }else{
            timeName=getMM_ddString(times*1000);
        }
        return timeName;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void setTextViewFormatString(TextView textView, String text, String item, int color, float size){
        if(TextUtils.isEmpty(text)){
            text=textView.getText().toString();
        }else {
            textView.setText(text);
        }
        if(TextUtils.isEmpty(item)){
            return;
        }
        int start=0;
        int end=-1;
        start=text.indexOf(item);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        int itemLength=item.length();
        while(start>=0){
            end=start+itemLength;
            style.setSpan( new RelativeSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start=text.indexOf(item,end);
        }
        textView.setText(style);
    }


}
