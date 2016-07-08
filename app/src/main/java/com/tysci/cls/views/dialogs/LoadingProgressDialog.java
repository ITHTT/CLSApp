package com.tysci.cls.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tysci.cls.R;


/**
 * Created by HTT on 2016/1/15.
 */
public class LoadingProgressDialog extends Dialog {
    private Context context;
    private View view;
    //private ImageView ivProgress;
    private TextView tvMessage;

    public LoadingProgressDialog(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.context = context;
        view = getLayoutInflater().inflate(R.layout.dialog_loading_progress, null);
//        ivProgress=(ImageView)view.findViewById(R.id.iv_progress_spinner);
//        ivProgress.setImageResource(R.anim.round_spinner);
        tvMessage=(TextView)view.findViewById(R.id.tv_message);
        this.setContentView(view);
    }

    public void setMessage(String message){
        tvMessage.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.3f;
        window.setAttributes(lp);
    }


}
