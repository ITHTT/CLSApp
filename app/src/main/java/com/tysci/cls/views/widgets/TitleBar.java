package com.tysci.cls.views.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tysci.cls.R;

/**
 * Created by HTT on 2016/5/30.
 */
public class TitleBar extends LinearLayout {
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivNextMenu01;
    private ImageView ivNextMenu02;
    private TextView tvNextMenu;

    public TitleBar(Context context) {
        super(context);
        initViews(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar_content, this, true);
        ivBack = (ImageView) this.findViewById(R.id.iv_titlebar_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_titlebar_title);
        ivNextMenu01 = (ImageView) this.findViewById(R.id.iv_titlebar_next_menu01);
        ivNextMenu02 = (ImageView) this.findViewById(R.id.iv_titlebar_next_menu02);
        tvNextMenu = (TextView) this.findViewById(R.id.tv_titlebar_next);
    }

    public void setPaddingTop(int paddingTop){
        this.setPadding(this.getPaddingLeft(),paddingTop,this.getPaddingRight(),this.getPaddingBottom());
    }

    public void setTitleBarLeftIcon(int res, OnClickListener onClickListener) {
        ivBack.setImageResource(res);
        ivBack.setOnClickListener(onClickListener);
    }

    public void setTitleBarTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitleBarTitle(int titleRes) {
        tvTitle.setText(titleRes);
    }

    public TextView getTitleBarTitle() {
        return tvTitle;
    }

    public void resetTitle() {
        tvTitle.setBackgroundResource(0);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvTitle.setTextSize(16f);
        //tvTitle.setTextColor(this.getResources().getColor(R.color.gold));
        tvTitle.setOnClickListener(null);
    }

    public void setRightMenuIcon(int res, OnClickListener onClickListener) {
        ivNextMenu01.setVisibility(View.VISIBLE);
        if (res > 0) {
            ivNextMenu01.setImageResource(res);
        }
        ivNextMenu01.setOnClickListener(onClickListener);
    }

    public ImageView getRightMenuImageView() {
        ivNextMenu01.setVisibility(View.VISIBLE);
        return ivNextMenu01;
    }

    public void setSecondRightMenuIcon(int res, OnClickListener onClickListener) {
        ivNextMenu02.setVisibility(View.VISIBLE);
        if (res > 0) {
            ivNextMenu02.setImageResource(res);
        }
        ivNextMenu02.setOnClickListener(onClickListener);
    }

    public void setRightMenuText(String title, OnClickListener onClickListener) {
        tvNextMenu.setVisibility(View.VISIBLE);
        tvNextMenu.setText(title);
        tvNextMenu.setOnClickListener(onClickListener);
    }

    public TextView getRightMenuTextView() {
        tvNextMenu.setVisibility(View.VISIBLE);
        return tvNextMenu;
    }


}
