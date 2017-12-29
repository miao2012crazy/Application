package com.xialan.app.presenter;


import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.RegeditContract;
import com.xialan.app.model.RegeditModel;

/**
 * 登录p层
 * Created by Administrator on 2017/7/17.
 */
public class RegeditPresenter extends BasePresenter implements RegeditContract.Presenter {

    private final RegeditModel mRegeditModel;
    private RegeditContract.View mView;

    public RegeditPresenter(RegeditContract.View mView) {
        this.mView = mView;
        mRegeditModel = new RegeditModel(this);
    }

    @Override
    public void getVerifyCode(String user_number) {
        mView.showProgress();
        mRegeditModel.getVerifyCodeToNet(user_number);
    }

    @Override
    public void getVerifyCodeSuccess() {
        mView.hideProgress();
        mView.onGetVerifyCodeSuccess();
    }

    @Override
    public void getVerifyCodeFailed() {
        mView.hideProgress();
        mView.onGetVerifyCodeFailed();
    }

    @Override
    public void checkVerifyCode(String verify_code) {
        mView.showProgress();
        mRegeditModel.CheckVerifyCodeToNet(verify_code);
    }

    @Override
    public void checkVerifyCodeSuccess() {
        mView.hideProgress();
        mView.OnCheckVerifyCodeSuccess();
    }

    @Override
    public void checkVerifyCodeFailed() {
        mView.hideProgress();
        mView.OnCheckVerifyCodeFailed();
    }
}
