package com.tysci.cls.views.widgets.loadmorerecyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tysci.cls.R;


/**
 * Created by Administrator on 2016/1/29.
 */
public class LoadMoreFooterView extends LinearLayout {
    private Context context;
    private ProgressBar progressBar;
    private TextView tvLoadMoreTip;
    private LinearLayout layoutLoadMoreContent;
    private String loadingTip;
    private String loadFinishedTip;
    private String loadFailedTip;
    private boolean isVisibility=true;

    public LoadMoreFooterView(Context context) {
        super(context);
        initViews(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initViews(Context context){
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.layout_load_more_view,this,true);
        layoutLoadMoreContent=(LinearLayout)this.findViewById(R.id.layout_load_more_content);
        progressBar=(ProgressBar)this.findViewById(R.id.pbLoading);
        tvLoadMoreTip=(TextView)this.findViewById(R.id.tv_load_more_text);
        tvLoadMoreTip.setText(loadingTip);
    }

    public void setLoadFailedTip(String loadFailedTip) {
        this.loadFailedTip = loadFailedTip;
    }

    public void setLoadFailedTip(int loadFailedTip) {
        this.loadFailedTip = this.getContext().getResources().getString(loadFailedTip);
    }

    public void setLoadFinishedTip(String loadFinishedTip) {
        this.loadFinishedTip = loadFinishedTip;
    }

    public void setLoadFinishedTip(int loadFinishedTip){
        this.loadFinishedTip=this.getContext().getResources().getString(loadFinishedTip);
    }

    public void setLoadingMoreState(){
        if(!isVisibility){
            showFooter();
        }
        if(progressBar.getVisibility()!= View.VISIBLE){
            progressBar.setVisibility(View.VISIBLE);
        }

        if(tvLoadMoreTip.getVisibility()!=View.GONE){
            tvLoadMoreTip.setVisibility(View.GONE);
        }
    }

    public void setLoadMoreDataFinishedState(boolean isShow){
        if(isShow){
            if(!isVisibility){
                showFooter();
            }
            if(progressBar.getVisibility()!=View.GONE){
                progressBar.setVisibility(View.GONE);
            }
            if(tvLoadMoreTip.getVisibility()!=View.VISIBLE){
                tvLoadMoreTip.setVisibility(View.VISIBLE);
            }
            tvLoadMoreTip.setText(loadFinishedTip);
        }else{
            if(isVisibility){
                hideFooter();
            }
        }
    }

    public void setLoadMoreFailedState(){
        if(!isVisibility){
            showFooter();
        }
        if(progressBar.getVisibility()!=View.GONE){
            progressBar.setVisibility(View.GONE);
        }
        if(tvLoadMoreTip.getVisibility()!=View.VISIBLE){
            tvLoadMoreTip.setVisibility(View.VISIBLE);
        }
        tvLoadMoreTip.setText(loadFailedTip);
    }

    private void hideFooter(){
        isVisibility=false;
        LayoutParams layoutParams= (LayoutParams) layoutLoadMoreContent.getLayoutParams();
        layoutParams.height=0;
        layoutLoadMoreContent.setLayoutParams(layoutParams);
    }

    private void showFooter(){
        //KLog.e("显示加载更多提示。。。");
        isVisibility=true;
        LayoutParams layoutParams= (LayoutParams) layoutLoadMoreContent.getLayoutParams();
        layoutParams.height=LayoutParams.WRAP_CONTENT;
        layoutLoadMoreContent.setLayoutParams(layoutParams);
    }
}
