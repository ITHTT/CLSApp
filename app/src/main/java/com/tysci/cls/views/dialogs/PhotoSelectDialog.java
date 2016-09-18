package com.tysci.cls.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tysci.cls.R;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.utils.PhotoUtil;

import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2016/7/28.
 */
public class PhotoSelectDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private Window window=null;
    private PhotoUtil photoUtil=null;


    public PhotoSelectDialog(Context context) {
        super(context,R.style.PhotoSelectDialogStyle);
        photoUtil=new PhotoUtil(context);
        this.context=context;
        this.window=this.getWindow();
        this.window.setWindowAnimations(R.style.PhotoSelectAnimStyle);
        initViews(context);
    }

    private void initViews(Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_photo_select,null);
        setContentView(view);
        view.findViewById(R.id.tv_take_photo).setOnClickListener(this);
        view.findViewById(R.id.tv_select_photo).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    public void cropPhoto(Uri uri){
        photoUtil.cropPhoto(uri);
    }

    public Uri getTakePhotoUri(){
        return photoUtil.getTakePhotoUri();
    }

    public void uploadPhoto(String tag,Bitmap bitmap,HttpClientUtil.ProgressResponseCallBack progressResponseCallBack){
        photoUtil.uploadBitmap(tag, bitmap, progressResponseCallBack);

    }



    public Bitmap getPhoto(Uri uri) throws FileNotFoundException {
        return photoUtil.compressBitmap(uri,480,480);
    }

    public Bitmap getBitmapPhoto(Uri uri){
        return photoUtil.decodeUriAsBitmap(uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogWindowAttrs();
    }

    protected void setDialogWindowAttrs() {
        // TODO Auto-generated method stub

        Activity activity= (Activity) context;
        WindowManager windowManager=activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p=window.getAttributes();
        //p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.4
        p.width = d.getWidth();
        window.setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.tv_take_photo:
                photoUtil.takePhoto();
                break;
            case R.id.tv_select_photo:
                photoUtil.selectPhoto();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
}
