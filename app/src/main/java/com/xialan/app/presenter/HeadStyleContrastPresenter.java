package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.HairStyleBABean;
import com.xialan.app.contract.HeadStyleContract;
import com.xialan.app.contract.HeadStyleContrastContract;
import com.xialan.app.listener.OnProgressListener;
import com.xialan.app.model.HeadStyleContrastModel;
import com.xialan.app.view.mainfragment.HeadStyleContrastFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
public class HeadStyleContrastPresenter extends BasePresenter implements HeadStyleContrastContract.Presenter, HeadStyleContract.Presenter ,OnProgressListener{
    private final HeadStyleContrastContract.View mView;
    private final HeadStyleContrastModel headStyleContrastModel;

    public HeadStyleContrastPresenter(HeadStyleContrastFragment mView) {
        this.mView = mView;
        headStyleContrastModel = new HeadStyleContrastModel(this);
    }

    @Override
    public void getDataForUserImage(String user_id) {
        showProgressBar();
        headStyleContrastModel.getDataForUserImageFromNet(user_id);
    }

    @Override
    public void getDataSuccess(List<HairStyleBABean> hairStyleBABeanList) {
        hideProgressBar();
        mView.getDataForUserImageSuccess(hairStyleBABeanList);
    }

    @Override
    public void getDataFailed() {
        hideProgressBar();
        mView.getDataForUserImageFailed();
    }

    @Override
    public void upLoadPicToNet(String user_id, String mac_id, String filepath) {
        showProgressBar();
        headStyleContrastModel.upLoadUserPic(user_id,mac_id,filepath);
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
