package com.xialan.app.model;

import com.xialan.app.contract.UserCenterContract;
import com.xialan.app.presenter.UserCenterPresenter;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.HttpUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/19.
 */
public class UserCenterModel implements UserCenterContract.Model {
    private UserCenterPresenter userCenterPresenter;

    public UserCenterModel(UserCenterPresenter userCenterPresenter) {
        this.userCenterPresenter = userCenterPresenter;
    }

    @Override
    public void upLoadUserheadImgModel(String user_id, String img_path) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", user_id);
        new HttpUtil().postFile(HttpUrl.setGetUrl("IBSync/join_member_picture.aspx"), map, new File(img_path), new HttpUtil.HttpCallBack() {
            private String str;
            @Override
            public void onSusscess(String data) {
                try{
                    if (data!=""||data!=null){
                        str= data.substring(0,2);
                    }
                }catch (Exception e){

                }
                if (str == "NG") {
                   userCenterPresenter.upLoadFailed();
                } else {
                    userCenterPresenter.upLoadSuccessed();
                }
            }
            @Override
            public void onError(String meg) {
                userCenterPresenter.upLoadFailed();
            }
        });

    }
}
