package com.tysci.cls.networks;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/6/2.
 */
public class GlideImageLoader {

    public static final void loadImage(Context context,String url,int defaultRes,ImageView imageView){
        Glide.with(context)
             .load(url)
             .asBitmap()
             .placeholder(defaultRes)
             .into(imageView);
    }

    public static final void loadImage(Fragment fragment,String url,int defaultRes,ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .asBitmap()
                .placeholder(defaultRes)
                .into(imageView);
    }

}
