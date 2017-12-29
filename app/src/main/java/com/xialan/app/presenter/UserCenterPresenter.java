package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.UserCenterContract;
import com.xialan.app.model.UserCenterModel;

/**
 * Created by Administrator on 2017/7/19.
 */
public class UserCenterPresenter extends BasePresenter implements UserCenterContract.Presenter {

    private UserCenterContract.View view;
    private final UserCenterModel userCenterModel;

    public UserCenterPresenter(UserCenterContract.View view) {
            this.view=view;
        userCenterModel = new UserCenterModel(this);
    }

    @Override
    public void upLoadUserheadImg(String user_id, String img_path) {
        userCenterModel.upLoadUserheadImgModel(user_id,img_path);
    }

    @Override
    public void upLoadSuccessed() {
        view.OnUpLoadSuccessed();
    }

    @Override
    public void upLoadFailed() {
        view.OnUpLoadFailed();
    }
}
