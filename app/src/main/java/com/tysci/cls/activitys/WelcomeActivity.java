package com.tysci.cls.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.tysci.cls.R;
import com.tysci.cls.utils.UserInfoUtils;

/**
 * Created by Administrator on 2016/6/30.
 */
public class WelcomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if(UserInfoUtils.checkLogin(this)){
            UserInfoUtils.getUserVisitInfo(this);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
