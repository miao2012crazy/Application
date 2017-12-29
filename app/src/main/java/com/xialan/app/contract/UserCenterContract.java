package com.xialan.app.contract;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface UserCenterContract {
    interface Model {
        void upLoadUserheadImgModel(String user_id,String img_path);
    }

    interface View {
        void OnUpLoadSuccessed();
        void OnUpLoadFailed();
    }
    interface Presenter {
        void upLoadUserheadImg(String user_id,String img_path);
        void upLoadSuccessed();
        void upLoadFailed();
    }
}
