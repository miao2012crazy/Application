package com.xialan.app.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.application.XLApplication;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.retrofit.RetrofitUtil;
import com.xialan.app.ui.Camera2Manager;
import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.BitmapUtil;
import com.xialan.app.utils.CheckLoginUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.FileUtils;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.HttpUtil;
import com.xialan.app.utils.QRCodeUtil;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.StringUtil;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.login.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/8/31.
 */

public class JSinterface {
    private final Activity activity;
    private CustomProgressBar customProgressBar;
    private Dialog dialog;
    private String mobile_uid;
    private Camera2Manager camera2Manager;
    private int pic_state;
    private Bitmap result;
    private String type = "99";
    private boolean isLoaded = false;

    public JSinterface(Activity activity) {
        this.activity = activity;
    }

    /**
     * js调用此方法 获取用户id  未登录时跳转到登录 并返回 ""
     */
    @JavascriptInterface
    public String getUserId() {
        String userId = XLApplication.uid;
        if (userId == null || userId.equals("")) {
            Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
            activity.startActivity(intent);
            return "";
        }
        return userId;
    }

    @JavascriptInterface
    public void MoveToCart() {

    }

    @JavascriptInterface
    public void MoveToHome() {

    }

    @JavascriptInterface
    public void MoveToLogin() {

    }

    /**
     * 个人中心 反馈页面处理结果
     */
    @JavascriptInterface
    public void feedbackComplete() {
        UIUtils.showToast(UIUtils.getContext(), "意见反馈成功!");
        RxBus.get().post("user_center", "feedback");
    }

    /**
     * 展示toast
     */
    @JavascriptInterface
    public void showToast(String msg) {
        UIUtils.showToast(UIUtils.getContext(), msg);
    }


    /**
     * 展示progressbar
     * msg 提示信息
     */
    @JavascriptInterface
    public void showProgressBar(String msg) {
        customProgressBar = new CustomProgressBar(activity);
        if (!msg.equals("")) {
            customProgressBar.setMessage(msg);
        }
        customProgressBar.show();
    }


    /**
     * 展示toast
     */
    @JavascriptInterface
    public void hideProgressBar() {
        if (customProgressBar != null) {
            customProgressBar.dismiss();
        }
    }

    /**
     * 展示 支付二维码dialog
     *
     * @param alipay_url    支付宝url
     * @param wechatpay_url wechat url
     */
    @JavascriptInterface
    public void showCodeDialog(String alipay_url, String wechatpay_url) {
        if (!wechatpay_url.equals("")) {
            showDialogForWchat(wechatpay_url);
            return;
        }
        if (!alipay_url.equals("")) {
            showDialogForAli(alipay_url);
            return;
        }

    }


    private void showDialogForAli(String alipay_url) {
        dialog = new Dialog(activity, R.style.dialog_login);
        dialog.setContentView(R.layout.scan_alipay);
        ImageView scan_alipay = (ImageView) dialog.findViewById(R.id.scan_alipay);
        boolean qrImage = QRCodeUtil.createQRImage(alipay_url, UIUtils.dip2px(300), UIUtils.dip2px(300), null, UIUtils.getInnerSDCardPath() + "/qrcode.jpg");
        if (qrImage) {
            Bitmap bitmap = BitmapFactory.decodeFile(UIUtils.getInnerSDCardPath() + "/qrcode.jpg");
            scan_alipay.setImageBitmap(bitmap);
        } else {
            UIUtils.showToast(activity, "二维码生成失败,请重新尝试");
        }
        dialog.show();
    }

    /**
     * 展示alipay支付对话框
     *
     * @param wechatpay_url
     */
    private void showDialogForWchat(String wechatpay_url) {
        dialog = new Dialog(activity, R.style.dialog_login);
        dialog.setContentView(R.layout.scan_wechat);
        ImageView iv_wechat = (ImageView) dialog.findViewById(R.id.iv_wechat);
        boolean qrImage1 = QRCodeUtil.createQRImage(wechatpay_url, UIUtils.dip2px(300), UIUtils.dip2px(300), null, UIUtils.getInnerSDCardPath() + "/wechatcode.jpg");
        if (qrImage1) {
            Bitmap bitmap = BitmapFactory.decodeFile(UIUtils.getInnerSDCardPath() + "/wechatcode.jpg");
            iv_wechat.setImageBitmap(bitmap);
        }
        dialog.show();
    }

    /**
     * 关闭二维码dialog
     */
    @JavascriptInterface
    public void closeQRCodeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 当前微信用户已存在
     *
     * @param mobile   手机号
     * @param password 密码
     */
    @JavascriptInterface
    public void setUserId(String mobile, String password) {
        this.mobile_uid = mobile;
        RetrofitUtil.setCallBack(RetrofitUtil.getApiservice().login(mobile, password), new RetrofitUtil.HttpCallBack() {
            @Override
            public void onstart() {
                showProgressBar("正在登录...");
            }

            @Override
            public void onSusscess(String data) {
                String[] dataForspild = CommonUtil.getDataForspild(data);
                CheckLoginUtil.parseLogin(dataForspild, mobile_uid);
            }

            @Override
            public void onError(String meg) {
                UIUtils.showToast(activity, "网络连接失败...");
            }

            @Override
            public void onCompleted() {
                hideProgressBar();
            }
        });


    }

    /**
     * 当前微信用户不存在
     *
     * @param nick_name    昵称
     * @param sex          性别 1:男  2:女
     * @param head_img_url 用户头像url
     * @param open_id      唯一性标识
     */
    @JavascriptInterface
    public void userNotExist(String nick_name, String sex, String head_img_url, String open_id) {
        String uid = XLApplication.uid;
        if (!uid.equals("")) {
            authenticationWechatId(uid, open_id);
            XLApplication.wechat_id = open_id;
            return;
        }
        UIUtils.showToast(activity, "首次登录需要验证您的手机号和密码!");
        String sex2 = String.valueOf(Integer.parseInt(sex) - 1);
        String[] str = {nick_name, sex2, head_img_url, open_id};
        XLApplication.setWechatUserInfo(str);
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                RxBus.get().post("replace_to_fragment", "1");
            }
        });
    }

    /**
     * 用户认证wechatid
     *
     * @param uid
     * @param open_id
     */
    private void authenticationWechatId(String uid, String open_id) {
        NovateUtil.getInstance().call(RetrofitUtil.getApiservice().getAuthentication(open_id, uid), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                UIUtils.showToast(activity, "连接服务器失败! 请确认网络");
                RxBus.get().post("auto_exit", 5);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    if (responseBody.string().contains("OK")) {
                        UIUtils.showToast(activity, "认证成功,您可以使用微信相关功能");
                        XLApplication.isWechating = true;
                    } else {
                        UIUtils.showToast(activity, "认证失败,您可以联系管理员!");
                        XLApplication.isWechating = false;
                    }
                    RxBus.get().post("auto_exit", 6);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 开启摄像头 上传身份证照片和信息
     */
    @JavascriptInterface
    public void openCamera() {
        if (XLApplication.uid.equals("")) {
            Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
            activity.startActivity(intent);
            activity.startActivity(intent);
        } else {
            isLoaded = false;
            customProgressBar = new CustomProgressBar(activity);
            final String pathname_front = UIUtils.getInnerSDCardPath() + "/front.png";
            final String pathname_bg = UIUtils.getInnerSDCardPath() + "/bg.png";
            final Dialog dialog = new Dialog(activity, R.style.dialog_login);
            View inflate = UIUtils.inflate(R.layout.layout_camera_upload_id);
            dialog.setContentView(inflate);
            dialog.setCanceledOnTouchOutside(true);
            TextureView textureView = (TextureView) inflate.findViewById(R.id.textureview);
            final ImageView iv_show_id = (ImageView) inflate.findViewById(R.id.iv_show_id);
            final ImageView iv_id_front = (ImageView) inflate.findViewById(R.id.iv_id_front);
            final ImageView iv_id_bg = (ImageView) inflate.findViewById(R.id.iv_id_bg);
            final EditText et_user_name = (EditText) inflate.findViewById(R.id.et_user_name);
            final EditText et_user_id_num = (EditText) inflate.findViewById(R.id.et_user_id_num);
            Button btn_commit = (Button) inflate.findViewById(R.id.btn_commit);
            Button still_button = (Button) inflate.findViewById(R.id.still_button);
            Button btn_close = (Button) inflate.findViewById(R.id.btn_close);
            Button restart_still_button = (Button) inflate.findViewById(R.id.restart_still_button);
            Button btn_save_photo = (Button) inflate.findViewById(R.id.btn_save_photo);
            final RelativeLayout rl_capture = (RelativeLayout) inflate.findViewById(R.id.rl_capture);
            final RelativeLayout rl_front = (RelativeLayout) inflate.findViewById(R.id.rl_front);
            final RelativeLayout rl_bg = (RelativeLayout) inflate.findViewById(R.id.rl_bg);
            final LinearLayout ll_reset_btn = (LinearLayout) inflate.findViewById(R.id.ll_reset_btn);
            RadioGroup rg_type = (RadioGroup) inflate.findViewById(R.id.rg_type);
            dialog.show();
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = 1000;
            params.height = 1100;
            dialog.getWindow().setAttributes(params);
            rl_front.setBackgroundResource(R.drawable.ic_focu);
            camera2Manager = new Camera2Manager(textureView, 1, activity);
            rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                    switch (i) {
                        case R.id.rb_iden_type:
                            //身份证
                            type = "0";
                            break;
                        case R.id.rb_passport_type:
                            //护照
                            type = "1";
                            break;
                    }
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (isLoaded) {
                        //清除用户的身份证照片
                        boolean b = FileUtils.deleteFile(new File(pathname_front));
                        boolean b1 = FileUtils.deleteFile(new File(pathname_bg));
                        if (b & b1) {
                            UIUtils.showToast(UIUtils.getContext(), "身份信息图像清理完毕!");
                        } else {
                            UIUtils.showToast(UIUtils.getContext(), "身份信息图像清理失败!请联系管理员!");
                        }
                    }
                }
            });

            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    camera2Manager.stopCamera();
                    dialog.dismiss();
                }
            });

            iv_id_front.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pic_state = 0;
                    rl_front.setBackgroundResource(R.drawable.ic_focu);
                    rl_bg.setBackgroundResource(0);
                    iv_id_front.setFocusable(true);
                }
            });
            iv_id_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pic_state = 1;
                    rl_bg.setBackgroundResource(R.drawable.ic_focu);
                    rl_front.setBackgroundResource(0);
                    iv_id_bg.setFocusable(true);
                }
            });
            still_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
                        @Override
                        public void captureSuccess(Bitmap bitmap) {
                            result = bitmap;
                            iv_show_id.setVisibility(View.VISIBLE);
                            iv_show_id.setImageBitmap(bitmap);
                            rl_capture.setVisibility(View.GONE);
                            ll_reset_btn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void captureFailed() {
                            UIUtils.showToast(UIUtils.getContext(), "获取图像失败 请重启设备!");
                        }
                    });
                }
            });

            restart_still_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_show_id.setVisibility(View.GONE);
                    rl_capture.setVisibility(View.VISIBLE);
                    ll_reset_btn.setVisibility(View.GONE);
                }
            });

            btn_save_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pic_state == 0) {
                        iv_id_front.setImageBitmap(result);
                        BitmapUtil.compressBmpToFile(result, new File(pathname_front));
                        pic_state = 1;
                        rl_bg.setBackgroundResource(R.drawable.ic_focu);
                        rl_front.setBackgroundResource(0);
                    } else {
                        iv_id_bg.setImageBitmap(result);
                        BitmapUtil.compressBmpToFile(result, new File(pathname_bg));
                    }
                    iv_show_id.setVisibility(View.GONE);
                    rl_capture.setVisibility(View.VISIBLE);
                    ll_reset_btn.setVisibility(View.GONE);
                }
            });
            btn_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user_name = et_user_name.getText().toString();
                    String user_id_num = et_user_id_num.getText().toString();
                    if (type.equals("99")) {
                        UIUtils.showToast(UIUtils.getContext(), "还没有选择上传的证件类型");
                        return;
                    }
                    if (StringUtil.isEmpty(user_name)) {
                        UIUtils.showToast(UIUtils.getContext(), "还没有输入真时姓名");
                        return;
                    }
                    if (StringUtil.isEmpty(user_id_num)) {
                        UIUtils.showToast(UIUtils.getContext(), "还没有输入身份证号");
                        return;
                    }
                    if (type.equals("0")) {
                        if (!UIUtils.isLegalId(user_id_num)) {
                            UIUtils.showToast(UIUtils.getContext(), "身份证号码有误,请检查,如果问题请联系管理员");
                            return;
                        }
                    }

                    if (iv_id_front.getDrawable() == null) {
                        UIUtils.showToast(UIUtils.getContext(), "还没有上传身份证正面照!");
                        return;
                    }

                    if (iv_id_bg.getDrawable() == null) {
                        UIUtils.showToast(UIUtils.getContext(), "还没有上传身份证背面照!");
                        return;
                    }
                    //核查无误 准备上传
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", XLApplication.uid);
                    map.put("name", user_name);
                    map.put("iden_no", user_id_num);
                    map.put("iden_type", type);
                    customProgressBar.show();
                    new HttpUtil().postFile_2(HttpUrl.setGetUrl("/IBSync/update_iden.aspx"), map, new File(pathname_front), new File(pathname_bg), new HttpUtil.HttpCallBack() {
                        @Override
                        public void onSusscess(String data) {
                            customProgressBar.dismiss();
                            isLoaded = true;
                            if (data == "NG") {
                                //失败
                                UIUtils.showToast(UIUtils.getContext(), "上传失败,请重新尝试或联系管理员!");
                            } else {
                                //成功
                                UIUtils.showToast(UIUtils.getContext(), "证件上传成功!");
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onError(String meg) {
                            customProgressBar.dismiss();
                            UIUtils.showToast(UIUtils.getContext(), "网络连接失败!");
                        }
                    });
                }
            });

        }
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                showMessage();
            }
        });

    }

    public void showMessage() {

        //用户协议
        final Dialog dialog = new Dialog(activity, R.style.dialog_notice);
        dialog.setContentView(R.layout.ib_layout_agreement);
        WebView webView = (WebView) dialog.findViewById(R.id.webView_notice);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 1000; // 宽度
        lp.height = 800; // 高度
        dialog.show();
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setPadding(10, 10, 10, 10);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl("file:///android_asset/html/Notice.html");
        webView.setWebViewClient(new WebViewClient());

    }


    @JavascriptInterface
    public void notice_to_login() {
        String userId = XLApplication.uid;
        if (!StringUtil.isEmpty(userId)) {
            RxBus.get().post("close_webview", "");
            RxBus.get().post("auto_exit", 6);
        } else {
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
    }
}
