package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.PerfectInfoContract;
import com.xialan.app.model.PerfectInfoModel;

/**
 * Created by Administrator on 2017/9/19.
 */

public class PerfectInfoPresenter extends BasePresenter implements PerfectInfoContract.Presenter {
    private  PerfectInfoContract.View mView;
    private final PerfectInfoModel perfectInfoModel;

    public PerfectInfoPresenter(PerfectInfoContract.View view) {
        this.mView=view;
        perfectInfoModel = new PerfectInfoModel(this);
    }

    @Override
    public void submitUserData(String mac_id, String smscode,String tel, String psd, String nick_name, String age, String sex,String recommender,String header_img_path) {
        mView.showProgress();
        perfectInfoModel.submitUserDataToNet(mac_id,smscode,tel,psd,nick_name,age,sex,recommender,header_img_path);
    }

    @Override
    public void submitSuccess() {
        mView.hideProgress();
        mView.onSubmitSuccess();
    }

    @Override
    public void submitFailed() {
        mView.hideProgress();
        mView.onSubmitFailed();
    }

    @Override
    public void upLoadPicFailed() {
        mView.hideProgress();
        mView.onLoadImgFailed();
    }

    @Override
    public void upLoadPicSuccess() {
        mView.hideProgress();
        mView.onLoadImgSuccess();
    }
}
