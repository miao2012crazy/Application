package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;
import com.xialan.app.contract.SkulpContract;
import com.xialan.app.listener.OnProgressListener;
import com.xialan.app.model.SkulpModel;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public class SkulpPresenter extends BasePresenter implements SkulpContract.Presenter ,OnProgressListener{
    private final SkulpContract.View mView;
    private final SkulpModel skulpModel;

    public SkulpPresenter(SkulpContract.View view) {
        this.mView=view;
        skulpModel = new SkulpModel(this);
    }

    @Override
    public void getSkupSampleData() {
        showProgressBar();
        skulpModel.getSkupSampleDataFromNet();
    }

    @Override
    public void getSkupSampleDataSuccess(List<SkupBean> skupBeenList) {
        hideProgressBar();
        mView.onSkupSampleSuccess(skupBeenList);
    }

    @Override
    public void getSkupSampleDataFailed() {
        hideProgressBar();
        mView.onSkupSampleFailed();
    }

    @Override
    public void getSkupForUserdata(String user_id) {
        showProgressBar();
        skulpModel.getSkupForUserFromNet(user_id);
    }

    @Override
    public void getSkupForUserdataSuccess(List<SkupKeepingBean> skupKeepingBeanList) {
        hideProgressBar();
        mView.onSkupUserDataSuccess(skupKeepingBeanList);
    }

    @Override
    public void getSkupForUserdataFailed() {
        hideProgressBar();
        mView.onSkupUserDataFailed();
    }

    @Override
    public void upLoadPicToNet(String user_id, String mac_id, String filepath) {
        showProgressBar();
        skulpModel.upLoadUserPic(user_id,mac_id,filepath);
    }

    @Override
    public void upLoadPicSuccess() {
        hideProgressBar();
        mView.savePicSuccess();
    }

    @Override
    public void upLoadPicFailed() {
        hideProgressBar();
        mView.savePicFailed();
    }

    @Override
    public void getDataWithNull() {
        mView.hideProgress();
    }

    @Override
    public void showProgressBar() {
        mView.showProgress();
    }

    @Override
    public void hideProgressBar() {
        mView.hideProgress();
    }
}
