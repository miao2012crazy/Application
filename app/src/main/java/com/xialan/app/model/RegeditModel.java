package com.xialan.app.model;

import com.google.gson.Gson;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.bean.CheckSmsVerifyCodeBean;
import com.xialan.app.bean.SmsMsgBean;
import com.xialan.app.contract.RegeditContract;
import com.xialan.app.presenter.RegeditPresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.utils.UIUtils;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/7/17.
 */
public class RegeditModel implements RegeditContract.Model {

    private final RegeditPresenter regeditPresenter;
    private String msg_id;
    private Gson gson = new Gson();

    public RegeditModel(RegeditPresenter mPresenter) {
        regeditPresenter = mPresenter;
    }

    @Override
    public void getVerifyCodeToNet(String user_number) {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getVerifyCode(user_number), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                regeditPresenter.getVerifyCodeFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    if (responseBody != null) {
                        SmsMsgBean smsMsgBean = gson.fromJson(responseBody.string(), SmsMsgBean.class);
                        msg_id = smsMsgBean.getMsg_id();
                        regeditPresenter.getVerifyCodeSuccess();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    regeditPresenter.getVerifyCodeFailed();
                }

            }
        });


    }

    @Override
    public void CheckVerifyCodeToNet(String verify_code) {
        if (msg_id == ""||msg_id==null) {
            UIUtils.showToast(UIUtils.getContext(), "还没有发送验证码!");
            return;
        }
        NovateUtil.getInstance().call(NovateUtil.getApiService().checkVerifyCode(msg_id, verify_code), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                regeditPresenter.checkVerifyCodeFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    CheckSmsVerifyCodeBean checkSmsVerifyCodeBean = gson.fromJson(responseBody.string(), CheckSmsVerifyCodeBean.class);
                    Boolean is_valid = checkSmsVerifyCodeBean.getIs_valid();
                    if (is_valid) {
                        regeditPresenter.checkVerifyCodeSuccess();
                    }else{
                        UIUtils.showToast(UIUtils.getContext(),"验证码过期!");
                        regeditPresenter.checkVerifyCodeFailed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    regeditPresenter.checkVerifyCodeFailed();
                }
            }
        });


    }
}
