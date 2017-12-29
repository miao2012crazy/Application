package com.xialan.app.presenter;


import android.app.Activity;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.LoginContract;
import com.xialan.app.model.LoginModel;

/**
 * 登录p层
 * Created by Administrator on 2017/7/17.
 */
public class LoginPresenter extends BasePresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private  LoginModel mLoginModel;

    public LoginPresenter(LoginContract.View view) {
        this.mView=view;
        mLoginModel = new LoginModel(this);
    }

    @Override
    public void onLoginSuccessed(String data) {
        mView.hideProgress();
        mView.loginSuccessed(data);
    }

    @Override
    public void onLoginFailed() {
        mView.hideProgress();
        mView.loginFailed();
    }

    @Override
    public void setLoginInfo(String user_name, String user_psd) {
        mView.showProgress();
        mLoginModel.login(user_name,user_psd);
    }
}
