package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.SkinBean;
import com.xialan.app.bean.SkinKeepingBean;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;
import com.xialan.app.contract.SkinContract;
import com.xialan.app.listener.OnProgressListener;
import com.xialan.app.model.SkinModel;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public class SkinPresenter extends BasePresenter implements SkinContract.Presenter ,OnProgressListener {
    private SkinContract.View mView;
    private final SkinModel skinModel;

    public SkinPresenter(SkinContract.View view) {
        this.mView=view;
        skinModel = new SkinModel(this);
    }

    @Override
    public void getSkinSampleData() {
        showProgressBar();
        skinModel.getSkinSampleDataFromNet();
    }

    @Override
    public void getSkinSampleDataSuccess(List<SkinBean> skinBeanList) {
        hideProgressBar();
        mView.onSkinSampleSuccess(skinBeanList);
    }

    @Override
    public void getSkinSampleDataFailed() {
        hideProgressBar();
        mView.onSkinSampleFailed();
    }

    @Override
    public void getSkinForUserdata(String user_id) {
        showProgressBar();
        skinModel.getSkinForUserFromNet(user_id);
    }

    @Override
    public void getSkinForUserdataSuccess(List<SkinKeepingBean> skinKeepingBeenList) {
        hideProgressBar();
        mView.onSkinUserDataSuccess(skinKeepingBeenList);
    }

    @Override
    public void getSkinForUserdataFailed() {
        hideProgressBar();
        mView.onSkinUserDataFailed();
    }

    @Override
    public void upLoadPicToNet(String userId, String adresseMAC, String pathname) {
        hideProgressBar();
        skinModel.upLoadUserPic(userId,adresseMAC,pathname);
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
