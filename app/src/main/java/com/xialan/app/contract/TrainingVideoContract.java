package com.xialan.app.contract;

import com.xialan.app.base.BaseView;
import com.xialan.app.bean.TrainingVideoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public interface TrainingVideoContract {
    interface Model {
        void getDataForVideoFromNet(String sv);
        void getDataForVideoFromNetNoParamas();
    }

    interface View extends BaseView{
      void  onVideoDataSuccess(List<TrainingVideoBean> trainingVideoBeanList);
      void  onVideoDataFailed();
    }

    interface Presenter {
        void getDataForVideo(String sv);
        void getDataSuccess(List<TrainingVideoBean> trainingVideoBeanList);
        void getDataFailed();
        void  getDataWithNull();
    }
}
