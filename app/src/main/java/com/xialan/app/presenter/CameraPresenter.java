package com.xialan.app.presenter;

import com.xialan.app.contract.CameraContract;
import com.xialan.app.contract.LoginContract;
import com.xialan.app.model.CameraModel;
import com.xialan.app.model.LoginModel;

/**
 * Created by Administrator on 2017/7/19.
 */
public class CameraPresenter implements CameraContract.Presenter {
    private CameraContract.View mView;
    private final CameraModel cameraModel;

    public CameraPresenter(CameraContract.View mView) {
        this.mView = mView;
        cameraModel = new CameraModel(this);
    }

}
