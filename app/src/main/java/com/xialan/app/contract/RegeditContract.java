package com.xialan.app.contract;

import com.xialan.app.base.BaseView;

/**
 * Created by Administrator on 2017/7/17.
 */
public interface RegeditContract {
    interface Model {
        //        通知服务器发送验证码
        void getVerifyCodeToNet(String user_number);

        //        检测验证码是否正确
        void CheckVerifyCodeToNet(String verify_code);


    }

    interface View extends BaseView{
        //        获取验证码成功
        void onGetVerifyCodeSuccess();

        //        获取验证码失败
        void onGetVerifyCodeFailed();

        //验证码正确
        void OnCheckVerifyCodeSuccess();

        //验证码错误
        void OnCheckVerifyCodeFailed();


    }

    interface Presenter {
        //        获取验证码
        void getVerifyCode(String user_number);

        void getVerifyCodeSuccess();

        void getVerifyCodeFailed();

        void checkVerifyCode(String verify_code);

        //验证码正确
        void checkVerifyCodeSuccess();

        //验证码错误
        void checkVerifyCodeFailed();

    }
}
