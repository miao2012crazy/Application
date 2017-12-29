package com.xialan.app.view.mainfragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.UpdateAppBean;
import com.xialan.app.contract.NoticeContract;
import com.xialan.app.presenter.NoticePresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.utils.BaseTimer;
import com.xialan.app.utils.BitmapUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.QRCodeUtil;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.JSinterface;
import com.xialan.app.webClient.WebChromeClientProgress;

import org.w3c.dom.Text;

import butterknife.BindView;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/10/12.
 */

public class NoticeFragment extends BaseFragment implements NoticeContract.View {

    @BindView(R.id.up_view)
    ViewFlipper upView;
    @BindView(R.id.iv_mobile_app_android)
    ImageView iv_mobile_app_android;
    @BindView(R.id.iv_mobile_app_ios)
    ImageView iv_mobile_app_ios;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    private NoticePresenter noticePresenter;
    private ProgressBar mProgressBarLoading;
    private TextView tv_login_time;
    private CountDownTimer countDownTimer;
    private Dialog dialog;
    private CountDownTimer countDownTimer_2;
    private BaseTimer baseTimer;
    private BaseTimer baseTimer1;
    private boolean exit_state=false;
    private Dialog dialog1;
    private Observable<String> netstate;

    @Override
    protected int getContentId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void loadData() {
        initBtnLogin();
        initTimerLogin();

        iv_mobile_app_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity(), R.style.dialog_login);
                dialog.setContentView(R.layout.download_app_android);
                ImageView scan_alipay = (ImageView) dialog.findViewById(R.id.scan_alipay);
                boolean qrImage = QRCodeUtil.createQRImage(XLApplication.app_version_android, UIUtils.dip2px(300), UIUtils.dip2px(300), BitmapUtil.drawable2Bitmap(UIUtils.getDrawable(R.mipmap.ic_logo)), UIUtils.getInnerSDCardPath() + "/qrcode.jpg");
                if (qrImage) {
                    Bitmap bitmap = BitmapFactory.decodeFile(UIUtils.getInnerSDCardPath() + "/qrcode.jpg");
                    scan_alipay.setImageBitmap(bitmap);
                } else {
                    UIUtils.showToast(getActivity(), "二维码生成失败,请重新尝试");
                }
                dialog.show();
            }
        });


  iv_mobile_app_ios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity(), R.style.dialog_login);
                dialog.setContentView(R.layout.download_app_ios);
                ImageView scan_alipay = (ImageView) dialog.findViewById(R.id.scan_alipay);
                TextView tv_ios = (TextView) dialog.findViewById(R.id.tv_ios);
                if (XLApplication.app_version_ios.equals("")){
                    tv_ios.setVisibility(View.VISIBLE);
                }else{
                    boolean qrImage = QRCodeUtil.createQRImage(XLApplication.app_version_ios, UIUtils.dip2px(300), UIUtils.dip2px(300), BitmapUtil.drawable2Bitmap(UIUtils.getDrawable(R.mipmap.ic_logo)), UIUtils.getInnerSDCardPath() + "/qrcode.jpg");
                    if (qrImage) {
                        Bitmap bitmap = BitmapFactory.decodeFile(UIUtils.getInnerSDCardPath() + "/qrcode.jpg");
                        scan_alipay.setImageBitmap(bitmap);
                    } else {
                        UIUtils.showToast(getActivity(), "二维码生成失败,请重新尝试");
                    }
                }
                dialog.show();
            }
        });








    }

    private void getAppVersion() {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getmobileversion(), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Gson gson = new Gson();
                    UpdateAppBean updateAppBean = gson.fromJson(responseBody.string(), UpdateAppBean.class);
                    if (updateAppBean.getCode().equals("S")){
                        XLApplication.app_version_ios = updateAppBean.getData().getIos();
                        XLApplication.app_version_android = HttpUrl.baseUrl()+"APPSync/app_release/"+updateAppBean.getData().getFile_path();
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    @Override
    protected BasePresenter createPresenter() {
        noticePresenter = new NoticePresenter(this);
        return noticePresenter;
    }

    @Override
    public void onGetNoticeSuccessed(String[] notice_data) {
        startViewFlipper(notice_data);

    }

    @Override
    public void onGetNoticeFailed() {
        UIUtils.showToast(UIUtils.getContext(), "请求失败!");
    }


    /**
     * 开启走马灯广告
     *
     * @param mNotice
     */
    private void startViewFlipper(String[] mNotice) {
        for (int i = 0; i < mNotice.length; i++) {
            String[] dataForspild = CommonUtil.getDataForspild(mNotice[i]);
            TextView textView = new TextView(getActivity());
            textView.setTextColor(Color.parseColor("#e4e4e4"));
            textView.setTextSize(UIUtils.px2dip(20));
            textView.setText(dataForspild[0]);
            textView.setTag(dataForspild[1]);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //构建dialog 显示webview
                    showInDialog(view.getTag().toString());
                }
            });
            upView.addView(textView);
        }
        upView.startFlipping();
    }

    /**
     * 键接跳转到dialog中展示webview
     *
     * @param tag
     */
    private void showInDialog(String tag) {
        dialog1 = new Dialog(getActivity(), R.style.dialog_notice);
        dialog1.setContentView(R.layout.ib_notice_layout);
        WebView webView = (WebView) dialog1.findViewById(R.id.webView_notice);
        mProgressBarLoading = (ProgressBar) dialog1.findViewById(R.id.progressbar_mainaty);
        Window dialogWindow = dialog1.getWindow();
        dialogWindow.setWindowAnimations(R.style.dialog_animaction);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 800; // 宽度
        lp.height = 500; // 高度
        dialog1.show();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSinterface(getActivity()),
                "android");
        webView.loadUrl(tag);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //设置加载进度条
                view.setWebChromeClient(new WebChromeClientProgress(mProgressBarLoading));
                return true;
            }
        });
    }

    /**
     * 登录和退出登录监听
     */
    private void initBtnLogin() {
        Observable<String> is_login = RxBus.get().register("is_login", String.class);
        is_login.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        switch (s) {
                            case "1":
                                inittime();
                                tvLogin.setTextColor(Color.parseColor("#ffffff"));
                                tvLogin.setText("已登录");
                                //开启定时器 15分钟弹出
                                startTaskTime();
                                break;
                            case "2":
                                XLApplication.returnUserLoginData();
                                RxBus.get().post("auto_exit",0);
                                if (baseTimer.isRunning()){
                                    baseTimer.killTimer();
                                }
                                if (countDownTimer!=null){
                                    countDownTimer.cancel();
                                }
                                if (countDownTimer!=null){
                                    countDownTimer.cancel();
                                }
                                if (dialog!=null){
                                    dialog.dismiss();
                                }
                                tvLogin.setTextColor(Color.parseColor("#888888"));
                                tvLogin.setText("未登录");
                                break;
                        }
                    }
                });
    }

    /**
     * 开启定时器 15分钟弹出
     */
    public void startTaskTime() {
        Log.e("miao","定时器初始化");
        if (baseTimer==null){
            inittime();
        }
        baseTimer.startTimer(60*1000*10, new BaseTimer.TimerCallBack() {
            @Override
            public void callback() {
                //这里的代码 10s后执行
                showdialog();
            }
        });
    }

    private void showdialog() {
        if(baseTimer!=null){
            baseTimer.killTimer();
        }
        dialog.show();
        countDownTimer.start();
        Window window = dialog.getWindow();
        try {
            window.setWindowAnimations(R.style.dialog_animaction);
        } catch (Exception e) {
        }
        countDownTimer.start();
    }

    /**
     * 15分钟自动弹出dialog
     */
    private void initDialog() {
        dialog = new Dialog(getActivity(), R.style.dialog_login);
        dialog.setContentView(R.layout.login_dialog);
        Button btn_login_keep = (Button) dialog.findViewById(R.id.btn_login_keep);
        Button btn_login_cancel = (Button) dialog.findViewById(R.id.btn_login_cancel);
        tv_login_time = (TextView) dialog.findViewById(R.id.tv_login_time);
        //点击了 保持登录
        btn_login_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // 点击了 退出登录
        btn_login_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit_state=true;
                autoExit();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //判断是否为外部点击退出
                if (exit_state){
                    exit_state=false;
                }else{
                    keepLogin();
                }
            }
        });

    }

    /**
     * 保持登录
     */
    private void keepLogin() {
        Log.e("miao","保持登录");
        if (dialog!=null){
            dialog.dismiss();
        }
        if (baseTimer!=null){
            baseTimer.killTimer();
        }
        startTaskTime();//重置15分钟自动弹出
    }

    /**
     * 自动退出
     */
    private void autoExit() {
        Log.e("miao","自动退出");
        XLApplication.returnUserLoginData();
        RxBus.get().post("is_login", "2");
        RxBus.get().post("auto_exit",0);
        if (baseTimer.isRunning()){
            baseTimer.killTimer();
        }
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    /**
     * 定义接收用户操作事件
     */
    private void initTimerLogin() {
        Observable<String> onUserInteraction = RxBus.get().register("onUserInteraction", String.class);
        onUserInteraction.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(java.lang.Throwable e) {
                UIUtils.showToast(UIUtils.getContext(),"登录时间检测程序出现异常,即将退出登录!");
                autoExit();
            }
            @Override
            public void onNext(String s) {
                keepLogin();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        noticePresenter.getNotice();
        getAppVersion();
        Observable<String> close_webview = RxBus.get().register("close_webview", String.class);
        close_webview.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (dialog1!=null){
                    dialog1.dismiss();
                }
            }
        });
        netstate = RxBus.get().register("NETSTATE", String.class);
        netstate.subscribe(new BaseSubscriber<String>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //网络已连接
                noticePresenter.getNotice();
            }
        });

    }

    public void inittime(){
        initDialog();
        baseTimer = new BaseTimer();
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_login_time.setText("为保障您的账户安全，" + millisUntilFinished / 1000 + "s无操作将自动退出。");
            }
            public void onFinish() {
                exit_state=true;
                //无操作自动退出
                autoExit();
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        try {

            if (dialog1 != null) {
                dialog1.dismiss();
            }
        }catch (Exception ex){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("NETSTATE",netstate);
    }
}
