package com.xialan.app.view.login;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.RegeditContract;
import com.xialan.app.presenter.RegeditPresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.utils.CheckLoginUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/7/26.
 */
public class RegeditFragment extends BaseFragment implements RegeditContract.View {
    @BindView(R.id.et_user_number)
    EditText etUserNumber;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;
    @BindView(R.id.btn_regedit)
    Button btnRegedit;
    @BindView(R.id.cb_box)
    CheckBox checkBox;
    @BindView(R.id.ll_regedit)
    LinearLayout llRegedit;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.et_age)
    EditText et_age;
    @BindView(R.id.ll_wechat_psd)
    LinearLayout llWechatPsd;
    private RegeditPresenter regeditPresenter;
    private CountDownTimer countDownTimer;
    private String str;
    private ProgressBar mProgressBarLoading;
    private String nick_name;
    private String sex;
    private String open_id;
    private boolean wechat_state = false;
    private String etpsd;
    private String etconpsd;
    private String str1;
    private String etAge;

    @Override
    protected int getContentId() {
        return R.layout.regedit;
    }

    @Override
    protected void loadData() {
         open_id = XLApplication.user_open_id;
        if (!open_id.equals("")){
            nick_name = XLApplication.user_nick_name;
            sex = XLApplication.user_sex;
            llWechatPsd.setVisibility(View.VISIBLE);
            wechat_state=true;
        }

    }

    @Override
    protected BasePresenter createPresenter() {
        regeditPresenter = new RegeditPresenter(this);
        return regeditPresenter;
    }

    @OnClick({R.id.btn_get_code, R.id.tv_user_agreement, R.id.btn_regedit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                //获取验证码
                str = etUserNumber.getText().toString();
                if (TextUtils.isEmpty(str) || !UIUtils.isTelPhoneNumber(str)) {
                    //非空并且 匹配正则
                    UIUtils.showToast(getActivity(), "当前手机号码无效,请核实!");
                    return;
                }
                regeditPresenter.getVerifyCode(str);
                //启动计时器
                countDownTimer = new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        if(btnGetCode!=null){
                            btnGetCode.setClickable(false);
                            btnGetCode.setText(millisUntilFinished / 1000 + "s");
                        }
                    }
                    public void onFinish() {
                        if(btnGetCode!=null) {
                            btnGetCode.setClickable(true);
                            btnGetCode.setText("重新获取");
                        }
                    }
                };
                countDownTimer.start();

                break;
            case R.id.tv_user_agreement:
                //用户协议
                final Dialog dialog = new Dialog(getActivity(), R.style.dialog_notice);
                dialog.setContentView(R.layout.ib_layout_agreement);
                WebView webView = (WebView) dialog.findViewById(R.id.webView_notice);
                mProgressBarLoading = (ProgressBar) dialog.findViewById(R.id.progressbar_mainaty);
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
                webView.loadUrl("file:///android_asset/html/index.html");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        //设置加载进度条
                        view.setWebChromeClient(new WebChromeClientProgress());
                        return true;
                    }
                });


                break;
            case R.id.btn_regedit:
                str = etUserNumber.getText().toString();
                //注册按钮
                if (!checkBox.isChecked()) {
                    UIUtils.showToast(getActivity(), "请先阅读并确认相关协议!");
                    return;
                }
                if (TextUtils.isEmpty(str) || !UIUtils.isTelPhoneNumber(str)) {
                    //非空并且 不匹配正则
                    UIUtils.showToast(getActivity(), "当前手机号码无效,请核实!");
                    return;
                }
                if (wechat_state) {
                    //打开布局 输入密码和确认密码
                    //不进行下一步 直接提交数据
                    etpsd = etPassword.getText().toString();
                    etconpsd = etConfirmPassword.getText().toString();
                    etAge = et_age.getText().toString();
                    if (TextUtils.isEmpty(etpsd) || !etpsd.matches("^[a-zA-Z0-9]{6,12}$")) {
                        UIUtils.showToast(getActivity(), "未输入密码或非法字符!");
                        return;
                    }
                    if (TextUtils.isEmpty(etconpsd) || !etconpsd.matches("^[a-zA-Z0-9]{6,12}$")) {
                        UIUtils.showToast(getActivity(), "未输入确认密码或非法字符!");
                        return;
                    }
                    if (!etconpsd.equals(etpsd)) {
                        UIUtils.showToast(getActivity(), "两次输入密码不一致!");
                        return;
                    }
                    if (TextUtils.isEmpty(etAge)) {
                        UIUtils.showToast(getActivity(), "年龄不能为空!");
                        return;
                    }

                }
                str1 = etVerifyCode.getText().toString();
                if (TextUtils.isEmpty(str1)) {
                    UIUtils.showToast(getActivity(), "需要填写验证码!");
                    return;
                }
                XLApplication.uid=str;
                XLApplication.uid_verify_code=str1;
                //请求服务器 验证码是否正确
                regeditPresenter.checkVerifyCode(str1);
                break;
        }
    }

    @Override
    public void onGetVerifyCodeSuccess() {
        UIUtils.showToast(getActivity(), "验证码已发送,请注意查收短信!");
    }

    @Override
    public void onGetVerifyCodeFailed() {
        //获取验证码失败
        UIUtils.showToast(getActivity(), "获取验证码失败,请确认手机是否可用!");
    }

    @Override
    public void OnCheckVerifyCodeSuccess() {
        if (wechat_state) {
            //打开布局 输入密码和确认密码
            //不进行下一步 直接提交数据
            UIUtils.showToast(UIUtils.getContext(), "已完成手机号校验!");
            commitUserData(etAge,nick_name, sex, open_id, str, str1, UIUtils.getAdresseMAC(getActivity()), etpsd);
            return;
        }
        //启动下一步
        UIUtils.showToast(getActivity(), "已完成注册第一步!");
        RxBus.get().post("replace", "");
    }

    /**
     * @param nickName
     * @param sex
     * @param open_id
     * @param str
     * @param adresseMAC
     * @param etpsd
     */
    private void commitUserData(String etAge,String nickName, String sex, String open_id, final String str, String verify_code, String adresseMAC, final String etpsd) {
        NovateUtil.getInstance().call(NovateUtil.getApiService().upLoadUserWechatRegeditData(adresseMAC, verify_code, str, etpsd, nickName, etAge, sex, open_id), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                UIUtils.showToast(getActivity(),"连接服务器失败");
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String string = responseBody.string();
                    if (responseBody.string().equals("NG:NG:MOBILE")){
                        UIUtils.showToast(getActivity(),"已存在的手机号,请使用手机号码登录!在进行微信验证");
                        getActivity().finish();
                    }
                    if (string.equals("OK")) {
                        autoLogin(str, etpsd);
                    }else{
                        UIUtils.showToast(getActivity(),"注册失败,请联系管理员");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void OnCheckVerifyCodeFailed() {
        UIUtils.showToast(getActivity(), "验证码错误，请确认！");
    }

    /**
     * webView进度条
     */
    private class WebChromeClientProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgressBarLoading != null) {
                mProgressBarLoading.setProgress(progress);
                if (progress == 100) mProgressBarLoading.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, progress);
        }
    }

    /**
     * 自动登录
     *
     * @param user_tel
     * @param user_pwd
     */
    public void autoLogin(final String user_tel, final String user_pwd) {
        //TODO 自动登录逻辑
        NovateUtil.getInstance().call(NovateUtil.getApiService().login(user_tel, user_pwd), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                UIUtils.showToast(getActivity(), "联网失败,请重试");
            }


            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String[] dataForspild = CommonUtil.getDataForspild(responseBody.string());
                    CheckLoginUtil.parseLogin(dataForspild,user_tel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
