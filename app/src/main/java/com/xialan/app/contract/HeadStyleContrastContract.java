package com.xialan.app.contract;

import com.xialan.app.base.BaseView;
import com.xialan.app.bean.HairStyleBABean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
public interface HeadStyleContrastContract {
    interface Model {
        /**
         * 获取用户拍照历史数据
         *
         * @param userId
         */
        void getDataForUserImageFromNet(String userId);

        //保存图片
        void upLoadUserPic(String user_id, String mac_id, String filepath);

    }

    interface View extends BaseView {
        /**
         * 获取用户拍照历史数据成功
         *
         * @param hairStyleBABeanList
         */
        void getDataForUserImageSuccess(List<HairStyleBABean> hairStyleBABeanList);

        //获取失败
        void getDataForUserImageFailed();

        void savePicSuccess();

        void savePicFailed();
    }

    interface Presenter {
        void getDataForUserImage(String user_id);

        void getDataSuccess(List<HairStyleBABean> hairStyleBABeanList);

        void getDataFailed();

        void upLoadPicToNet(String user_id, String mac_id, String filepath);

        void upLoadPicSuccess();

        void upLoadPicFailed();
        /**
         * 获取到的数据为空时调用
         */
        void getDataWithNull();
    }
}
