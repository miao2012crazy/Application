package com.xialan.app.webClient;

/**
 * Created by Administrator on 2017/9/26.
 */

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * webView进度条
 */
public class WebChromeClientProgress extends WebChromeClient {
    private ProgressBar mProgressBarLoading;

    public WebChromeClientProgress(ProgressBar mProgressBarLoading) {
        this.mProgressBarLoading=mProgressBarLoading;
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
        if (mProgressBarLoading != null) {
            mProgressBarLoading.setProgress(progress);
            if (progress == 100) mProgressBarLoading.setVisibility(View.GONE);
        }
        super.onProgressChanged(view, progress);
    }
}