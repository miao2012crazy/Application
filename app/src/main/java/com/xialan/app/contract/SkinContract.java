package com.xialan.app.contract;

import com.xialan.app.base.BaseView;
import com.xialan.app.bean.SkinBean;
import com.xialan.app.bean.SkinKeepingBean;
import com.xialan.app.bean.SkupKeepingBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface SkinContract {
    interface Model {
        void getSkinSampleDataFromNet();

        void getSkinForUserFromNet(String user_id);

        //保存图片
        void upLoadUserPic(String user_id, String mac_id, String filepath);

    }

    interface View  extends BaseView{
        void onSkinSampleSuccess(List<SkinBean> skupBeenList);

        void onSkinSampleFailed();

        void onSkinUserDataSuccess(List<SkinKeepingBean> skinKeepingBeanList);

        void onSkinUserDataFailed();

        void savePicSuccess();

        void savePicFailed();
    }

    interface Presenter {
        void getSkinSampleData();

        void getSkinSampleDataSuccess(List<SkinBean> skupBeenList);

        void getSkinSampleDataFailed();

        void getSkinForUserdata(String user_id);

        void getSkinForUserdataSuccess(List<SkinKeepingBean> skinKeepingBeanList);

        void getSkinForUserdataFailed();


        void upLoadPicToNet(String userId, String adresseMAC, String pathname);

        void upLoadPicSuccess();

        void upLoadPicFailed();
        /**
         * 获取到的数据为空时调用
         */
        void getDataWithNull();
    }



}
