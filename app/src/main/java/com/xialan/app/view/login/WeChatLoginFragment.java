package com.xialan.app.view.login;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xialan.app.R;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.WeChatLoginContract;
import com.xialan.app.presenter.WeChatLoginPresenter;
import com.xialan.app.view.JSinterface;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/25.
 */

public class WeChatLoginFragment extends BaseFragment implements WeChatLoginContract.View {

    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.progress)
    TextView tv_progress;
    @BindView(R.id.rl_progressbar)
    RelativeLayout rl_progressbar;
    private WeChatLoginPresenter weChatLoginPresenter;


    @Override
    protected int getContentId() {
        return R.layout.layout_wechat_login;
    }

    @Override
    protected void loadData() {
        initWebView("https://open.weixin.qq.com/connect/qrconnect?appid=wx581250b3c74d3462&redirect_uri=http%3a%2f%2fwww.gou-mei.com/common/wechat_login.aspx?response_type=code&scope=snsapi_login&state=IB#wechat_redirect");
    }

    @Override
    protected BasePresenter createPresenter() {
        weChatLoginPresenter = new WeChatLoginPresenter(this);
        return weChatLoginPresenter;
    }

    private void initWebView(String url) {
        webView.loadUrl(url);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSinterface(getActivity()),
                "android");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WeChatLoginFragment.WebChromeClientProgress(){
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (tv_progress!=null){
                    tv_progress.setText(progress+"");
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //设置加载进度条
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (tv_progress!=null){
                    tv_progress.setText("0");
                    rl_progressbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (rl_progressbar!=null){
                    rl_progressbar.setVisibility(View.GONE);
                }
            }

        });
        rl_progressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rl_progressbar!=null){
                    rl_progressbar.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * webView进度条
     */
    private class WebChromeClientProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgressBarLoading != null) {
                tv_progress.setText(progress+"");
                mProgressBarLoading.setProgress(progress);
                if (progress == 100) rl_progressbar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, progress);
        }
    }

}
