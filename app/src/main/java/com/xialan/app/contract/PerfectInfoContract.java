package com.xialan.app.contract;

import com.xialan.app.base.BaseView;

/**
 * Created by Administrator on 2017/9/19.
 */

public interface PerfectInfoContract {
    interface Model {
        void submitUserDataToNet(String mac_id, String smscode,String tel, String psd, String nick_name, String age, String sex,String recommender,String header_img_path);


    }

    interface View extends BaseView{

        void onSubmitSuccess();

        void onSubmitFailed();

        void onLoadImgSuccess();
        void onLoadImgFailed();

    }

    interface Presenter {
        void submitUserData(String mac_id, String smscode,String tel, String psd, String nick_name, String age, String sex,String recommender,String header_img_path);

        void submitSuccess();

        void submitFailed();

        void upLoadPicFailed();

        void upLoadPicSuccess();

    }
}
