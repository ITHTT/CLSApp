package com.tysci.cls.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tysci.cls.R;


/**
 * Created by Administrator on 2015/11/26.
 */
public class MainMenuTab extends LinearLayout {
    private CheckBox tvMenuTitle;
    private ImageView ivMenuIcon;

    public MainMenuTab(Context context) {
        super(context);
        initViews(context,null);
    }

    public MainMenuTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MainMenuTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainMenuTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs);
    }

    private void initViews(Context context,AttributeSet attrs){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view=LayoutInflater.from(context).inflate(R.layout.layout_menu_item,this,true);
        //view.setBackgroundResource(R.drawable.item_click_style);
        tvMenuTitle=(CheckBox)this.findViewById(R.id.menu_title);
       // tabTitle.setClickable(false);
        ivMenuIcon=(ImageView)this.findViewById(R.id.menu_icon);

        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainMenuTab);
            Drawable icon=ta.getDrawable(R.styleable.MainMenuTab_tab_icon);
            String title=ta.getString(R.styleable.MainMenuTab_tab_title);
            ta.recycle();

            if(icon!=null){
                ivMenuIcon.setImageDrawable(icon);
            }

            if(!TextUtils.isEmpty(title)){
                tvMenuTitle.setText(title);
            }
        }
    }

//    //public void setTabMsgVisibility(int visibility){
//        tabMsg.setVisibility(visibility);
//    }
//
//    public void setTagMsgText(String count){
//        if(tabMsg.getVisibility()!= View.VISIBLE){
//            tabMsg.setVisibility(View.VISIBLE);
//        }
//        tabMsg.setText(count);
//    }

//    public void setTabSelected(boolean selected){
//        tabTitle.setSelected(selected);
//    }

    public void setTabChecked(boolean checked){
        ivMenuIcon.setSelected(checked);
        tvMenuTitle.setChecked(checked);
    }
}
