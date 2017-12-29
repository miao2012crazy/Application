package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.bean.TrainingVideoBean;
import com.xialan.app.contract.TrainingVideoContract;
import com.xialan.app.presenter.TrainingVideoPresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/9/14.
 */

public class TrainingVideoModel implements TrainingVideoContract.Model {

    private TrainingVideoPresenter mTrainingVideoPresenter;

    public TrainingVideoModel(TrainingVideoPresenter trainingVideoPresenter) {
        this.mTrainingVideoPresenter = trainingVideoPresenter;
    }

    @Override
    public void getDataForVideoFromNet(String sv) {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getTrainingDataForSerch("0",sv), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                mTrainingVideoPresenter.getDataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {

                    String string = responseBody.string();
                    if (string.equals("")){
                        UIUtils.showToast(UIUtils.getContext(),"暂无数据!");
                        mTrainingVideoPresenter.getDataWithNull();
                        return;
                    }
                    String[] trainingVideoData = CommonUtil.getTrainingVideoData(string);
                    List<TrainingVideoBean> mlist = new ArrayList<>();
                    for (int i = 0; i < trainingVideoData.length; i++) {
                        String[] dataForVideo = CommonUtil.getDataForVideo(trainingVideoData[i]);
                        TrainingVideoBean trainingVideoBean = new TrainingVideoBean(dataForVideo[0], dataForVideo[1], dataForVideo[2], dataForVideo[3], dataForVideo[4], dataForVideo[5], dataForVideo[6], dataForVideo[7], dataForVideo[8], "0");
                        mlist.add(trainingVideoBean);
                    }
                    mTrainingVideoPresenter.getDataSuccess(mlist);
                } catch (Exception e) {
                    mTrainingVideoPresenter.getDataFailed();
                }


            }
        });
    }

    @Override
    public void getDataForVideoFromNetNoParamas() {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getTrainingData(), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                mTrainingVideoPresenter.getDataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String[] trainingVideoData = CommonUtil.getTrainingVideoData(responseBody.string());
                    List<TrainingVideoBean> mlist = new ArrayList<>();
                    for (int i = 0; i < trainingVideoData.length; i++) {
                        String[] dataForVideo = CommonUtil.getDataForVideo(trainingVideoData[i]);
                        TrainingVideoBean trainingVideoBean = new TrainingVideoBean(dataForVideo[0], dataForVideo[1], dataForVideo[2], dataForVideo[3], dataForVideo[4], dataForVideo[5], dataForVideo[6], dataForVideo[7], dataForVideo[8], "0");
                        mlist.add(trainingVideoBean);
                    }
                    mTrainingVideoPresenter.getDataSuccess(mlist);
                } catch (Exception e) {
                    mTrainingVideoPresenter.getDataFailed();
                }


            }
        });

    }
}
