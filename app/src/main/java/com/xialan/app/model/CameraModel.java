package com.xialan.app.model;

import com.xialan.app.contract.CameraContract;
import com.xialan.app.presenter.CameraPresenter;
/**
 * Created by Administrator on 2017/7/19.
 */
public class CameraModel implements CameraContract.Model {
    private CameraPresenter mPresenter;

    public CameraModel(CameraPresenter mPresenter) {
        this.mPresenter=mPresenter;
    }
}
