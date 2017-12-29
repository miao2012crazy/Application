package com.xialan.app.view.mainfragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awarmisland.android.popularrefreshlayout.RefreshLayout;
import com.awarmisland.android.popularrefreshlayout.RefreshListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xialan.app.R;
import com.xialan.app.adapter.UserCenterAdapter;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.UserCenterBean;
import com.xialan.app.contract.UserCenterContract;
import com.xialan.app.presenter.UserCenterPresenter;
import com.xialan.app.ui.Camera2Manager;
import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.BitmapUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.JSinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/18.
 */
public class UserCenterFragment extends BaseFragment implements UserCenterContract.View {

    @BindView(R.id.progress)
    TextView tv_progress;
    @BindView(R.id.rl_progressbar)
    RelativeLayout rl_progressbar;
    @BindView(R.id.rl_login)
    RelativeLayout rl_login;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.rl_webview)
    RelativeLayout rl_webview;
    @BindView(R.id.refresh)
    RefreshLayout refresh;
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.btn_concel_login)
    Button btnConcelLogin;
    @BindView(R.id.iv_user_header)
    ImageView ivUserHeader;
    @BindView(R.id.ib_wechat)
    ImageView ib_wechat;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.iv_user_head_img)
    ImageView ivUserHeadImg;
    private ImageView iv_camera_show;
    private TextureView cameraView;
    private RelativeLayout rlCapture;
    @BindView(R.id.recycler_user_center)
    RecyclerView recyclerUserCenter;
    private Camera2Manager camera2Manager;
    private Bitmap result;
    private String pathname;
    private Observable<String> user_center;
    private UserCenterPresenter userCenterPresenter;
    private CustomProgressBar customProgressBar;
    private String userId;
    private Button btn_camera;//重新拍照
    private Button btn_save;    //保存照片
    private Button still_button;//拍照
    private Button btn_close_camera;

    @Override
    protected void initVariables() {
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void loadData() {
        user_center = RxBus.get().register("user_center", String.class);
        user_center.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (s.equals("feedback")) {
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("about:blank");
                            rl_webview.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        initRecyclerView();
        Observable<String> close_webview = RxBus.get().register("close_webview", String.class);
        close_webview.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if (rl_webview!=null){
                    webView.loadUrl("about:blank");
                    rl_webview.setVisibility(View.GONE);
                }
            }
        });


    }

    /**
     * 初始化item
     */
    private void initRecyclerView() {
        Drawable[] drawableArr = {UIUtils.getDrawable(R.drawable.ic_shopping),
                UIUtils.getDrawable(R.drawable.ic_dingdan),
                UIUtils.getDrawable(R.drawable.ic_dizhi),
                UIUtils.getDrawable(R.drawable.ic_about)
        };
        String[] strArr = {"购物车", "我的订单", "地址管理", "关于beauty mall"};
        List<UserCenterBean> mList = new ArrayList<>();
        for (int i = 0; i < strArr.length; i++) {
            mList.add(new UserCenterBean(drawableArr[i], strArr[i]));
        }
        recyclerUserCenter.setLayoutManager(new LinearLayoutManager(getActivity()));
        UserCenterAdapter adapter = new UserCenterAdapter(mList);
        recyclerUserCenter.setAdapter(adapter);
        recyclerUserCenter.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (i) {
                    case 0:
                        if (userId.equals("") || userId == null) {
                            startLogin();
                            return;
                        }
                        initWebView(HttpUrl.setGetUrl("IBSync/view_cart.aspx?uid=" + userId));
                        break;
                    case 1:
                        if (userId.equals("") || userId == null) {
                            startLogin();
                            return;
                        }
                        initWebView(HttpUrl.setGetUrl("IBSync/center_dd.aspx?uid=" + userId));
                        break;
                    case 2:
                        if (userId.equals("") || userId == null) {
                            startLogin();
                            return;
                        }
                        initWebView(HttpUrl.setGetUrl("IBSync/center_dzgl.aspx?uid=" + userId));
                        break;

                    case 3:
                        initWebView("file:///android_asset/html/about_beauty_mall.html");
                        break;
                }
            }
        });
    }

    /**
     * 初始化登录数据
     */
    private void initLogin() throws Exception {
        userId = XLApplication.uid;
        if (userId.equals("")) {
            btnConcelLogin.setText("登录");
            rl_login.setVisibility(View.GONE);
        } else {
            rl_login.setVisibility(View.VISIBLE);
            btnConcelLogin.setText("退出登录");
        }
        String nick_name = XLApplication.uid_nick_name_2;
        String sex = XLApplication.uid_sex;
        tvNickName.setText(nick_name);
        getImageDrawable(sex);
        String img_path = XLApplication.uid_head_url;
        ImageLoaderManager.displayHeadIcon(HttpUrl.setGetUrl("IBSync/client_profile/" + img_path), ivUserHeader);
        String wechat_id = XLApplication.wechat_id;
        if (wechat_id.equals("")){
            ib_wechat.setEnabled(true);
        }else{
            ib_wechat.setEnabled(false);
            ib_wechat.setImageResource(R.drawable.appwx_logo);
        }
    }

    /**
     * 设置性别
     *
     * @param s
     */
    private void getImageDrawable(String s) {
        switch (s) {
            case "1":
                ivUserHeadImg.setImageDrawable(UIUtils.getDrawable(R.drawable.nv));
                break;
            case "0":
                ivUserHeadImg.setImageDrawable(UIUtils.getDrawable(R.drawable.boy_user));
                break;
        }
    }
    @Override
    protected BasePresenter createPresenter() {
        userCenterPresenter = new UserCenterPresenter(this);
        return userCenterPresenter;
    }

    @OnClick({R.id.iv_user_header, R.id.btn_concel_login, R.id.ib_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_header:
                initCameraDialog();
                break;
            case R.id.btn_concel_login:
                cancelLogin();
                break;
            case R.id.ib_wechat:
                XLApplication.isWechating=true;
                //认证微信 打开webview 扫码
                initWebView("https://open.weixin.qq.com/connect/qrconnect?appid=wx581250b3c74d3462&redirect_uri=http%3a%2f%2fwww.gou-mei.com/common/wechat_login.aspx?response_type=code&scope=snsapi_login&state=IB#wechat_redirect");
                break;
        }
    }


    /**
     * 退出登录
     */
    private void cancelLogin() {
        if (userId.equals("")) {
            startLogin();
            return;
        }
        //退出登录
        XLApplication.returnUserLoginData();
        try {
            initLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mainactivity中登录信息
        RxBus.get().post("is_login", "2");
        btnConcelLogin.setText("登录");
    }

    /**
     * 拍照
     */
    private void takePicture() {

        camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                Bitmap roundBitmap = UIUtils.getRoundBitmap(bitmap);
                result = roundBitmap;
                iv_camera_show.setVisibility(View.VISIBLE);
                iv_camera_show.setImageBitmap(roundBitmap);

            }

            @Override
            public void captureFailed() {
                UIUtils.showToast(getActivity(),"拍照失败!请尝试重启设备");
            }
        });
    }

    /**
     * 保存用户头像
     */
    private void saveUserHeadImg() {
        //保存照片
        rlCapture.setVisibility(View.VISIBLE);
        if (userId == null || userId == "") {
            UIUtils.showToast(getActivity(), "还没有登录,不能修改或上传头像");
            return;
        }
        ivUserHeader.setImageBitmap(result);
        //保存照片
        pathname = UIUtils.getInnerSDCardPath() + "/user_header.png";
        BitmapUtil.compressBmpToFile(result, new File(pathname));
        userCenterPresenter.upLoadUserheadImg(userId, pathname);
    }

    private void initWebView(String url) {
        webView.loadUrl(url);
        rl_webview.setVisibility(View.VISIBLE);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        btnConcelLogin.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSinterface(getActivity()),
                "android");
        webView.setWebChromeClient(new UserCenterFragment.WebChromeClientProgress() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                tv_progress.setText(progress + "");
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
                tv_progress.setText("0");
                rl_progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                rl_progressbar.setVisibility(View.GONE);
            }

        });
        rl_progressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_progressbar.setVisibility(View.GONE);
            }
        });
        refresh.setAllowLoadMore(true);//设置支持上拉加载
        refresh.setRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefresh();
                        //刷新数据
                        webView.reload();
                    }
                }, 1000);
            }

            @Override
            public void onRefreshLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefreshLoadMore();
                        //刷新数据
                        webView.reload();
                    }
                }, 1000);
            }
        });
    }

    /**
     * webView进度条
     */
    private class WebChromeClientProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            try {
                tv_progress.setText(progress + "");
                mProgressBarLoading.setProgress(progress);
                if (progress == 100) rl_progressbar.setVisibility(View.GONE);

            } catch (Exception e) {
                rl_progressbar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, progress);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (camera2Manager != null) {
                camera2Manager.stopCamera();
            }
            initLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();


        RxBus.get().unregister("user_center", user_center);

    }

    @Override
    public void OnUpLoadSuccessed() {
        UIUtils.showToast(getActivity(), "头像修改成功");
    }

    @Override
    public void OnUpLoadFailed() {
        UIUtils.showToast(getActivity(), "头像修改失败");
    }


    /**
     * 拍摄头像dialog
     */
    private void initCameraDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog_login);
        View inflate = UIUtils.inflate(R.layout.layout_camera_usercenter);
        dialog.setContentView(inflate);
        cameraView = (TextureView) inflate.findViewById(R.id.camera_view);
        dialog.setCanceledOnTouchOutside(false);
        iv_camera_show = (ImageView) inflate.findViewById(R.id.iv_camera_show);
        btn_camera = (Button) inflate.findViewById(R.id.btn_camera);
        btn_save = (Button) inflate.findViewById(R.id.btn_save);
        still_button = (Button) inflate.findViewById(R.id.still_button);
        btn_close_camera = (Button) inflate.findViewById(R.id.btn_close_camera);
        rlCapture = (RelativeLayout) inflate.findViewById(R.id.rl_capture);
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1000;
        params.height = 740;
        dialog.getWindow().setAttributes(params);
        camera2Manager = new Camera2Manager(cameraView, 1, getActivity());
        //拍照
        still_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlCapture.setVisibility(View.GONE);
                takePicture();
            }
        });
        //重新拍照
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlCapture.setVisibility(View.VISIBLE);
                iv_camera_show.setVisibility(View.GONE);
            }
        });
        //保存照片
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == null) {
                    return;
                }
                dialog.dismiss();
                saveUserHeadImg();
            }
        });
        //关闭dialog camera
        btn_close_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                camera2Manager.stopCamera();
                iv_camera_show.setImageBitmap(null);
                iv_camera_show.setVisibility(View.GONE);
            }
        });

    }


}
