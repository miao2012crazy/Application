package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.contract.LoginContract;
import com.xialan.app.presenter.LoginPresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.retrofit.RetrofitUtil;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/7/17.
 */
public class LoginModel implements LoginContract.Model {
    private LoginPresenter mPresenter;

    public LoginModel(LoginPresenter mPresenter) {
        this.mPresenter=mPresenter;
    }

    @Override
    public void login(String user_name, String User_psd) {
        NovateUtil.getInstance().call(RetrofitUtil.getApiservice().login(user_name, User_psd), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                mPresenter.onLoginFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    mPresenter.onLoginSuccessed(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
