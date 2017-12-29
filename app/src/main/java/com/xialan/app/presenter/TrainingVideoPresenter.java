package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.TrainingVideoBean;
import com.xialan.app.contract.TrainingVideoContract;
import com.xialan.app.listener.OnProgressListener;
import com.xialan.app.model.TrainingVideoModel;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class TrainingVideoPresenter extends BasePresenter implements TrainingVideoContract.Presenter,OnProgressListener {
    private final TrainingVideoContract.View mView;
    private final TrainingVideoModel trainingVideoModel;

    public TrainingVideoPresenter(TrainingVideoContract.View view) {
        this.mView=view;
        trainingVideoModel = new TrainingVideoModel(this);
    }

    @Override
    public void getDataForVideo(String sv) {
        showProgressBar();
        if (sv==null||sv==""){
            trainingVideoModel.getDataForVideoFromNetNoParamas();
        }
        trainingVideoModel.getDataForVideoFromNet(sv);
    }

    @Override
    public void getDataSuccess(List<TrainingVideoBean> trainingVideoBeanList) {
        hideProgressBar();
        mView.onVideoDataSuccess(trainingVideoBeanList);
    }

    @Override
    public void getDataFailed() {
        hideProgressBar();
        mView.onVideoDataFailed();
    }

    @Override
    public void getDataWithNull() {
        hideProgressBar();
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
