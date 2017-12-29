package com.xialan.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.xialan.app.R;

/**
 * Created by Administrator on 2017/9/27.
 */

public class CustomProgressBar  extends Dialog {
    private TextView tv_text;
    public CustomProgressBar(Context context) {
        super(context);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_text.setText("正在加载中...");
        setCanceledOnTouchOutside(false);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public CustomProgressBar setMessage(String message) {
        tv_text.setText(message);
        return this;
    }



}
