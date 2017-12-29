package com.xialan.app.contract;

import com.xialan.app.base.BaseView;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface SkulpContract {
    interface Model {
        void getSkupSampleDataFromNet();

        void getSkupForUserFromNet(String user_id);
        //保存图片
        void upLoadUserPic(String user_id, String mac_id, String filepath);
    }

    interface View extends BaseView{
        void onSkupSampleSuccess(List<SkupBean> skupBeenList);

        void onSkupSampleFailed();

        void onSkupUserDataSuccess(List<SkupKeepingBean> skupKeepingBeenList);

        void onSkupUserDataFailed();


        void savePicSuccess();

        void savePicFailed();
    }

    interface Presenter {
        void getSkupSampleData();

        void getSkupSampleDataSuccess(List<SkupBean> skupBeenList);

        void getSkupSampleDataFailed();

        void getSkupForUserdata(String user_id);

        void getSkupForUserdataSuccess(List<SkupKeepingBean> skupKeepingBeanList);

        void getSkupForUserdataFailed();

        void upLoadPicToNet(String user_id, String mac_id, String filepath);

        void upLoadPicSuccess();

        void upLoadPicFailed();
        /**
         * 获取到的数据为空时调用
         */
        void getDataWithNull();
    }
}
