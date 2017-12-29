package com.xialan.app.contract;

import com.xialan.app.base.BaseView;

/**
 * Created by Administrator on 2017/7/17.
 */
public interface LoginContract {
    interface Model {
        /**登录*/
        void login(String user_name,String User_psd);
    }

    interface View extends BaseView{
        void loginSuccessed(String data);
        void loginFailed();
    }

    interface Presenter {

        void onLoginSuccessed(String data);
        void onLoginFailed();
        /**
         *  用户点击登录时传递
         * @param user_name  用户名
         * @param user_psd      密码
         */
        void  setLoginInfo(String user_name,String user_psd);
    }
}
