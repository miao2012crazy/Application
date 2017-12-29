package com.xialan.app.model;

import com.xialan.app.contract.HeadStyleContract;
import com.xialan.app.presenter.HeadStylePresenter;

/**
 * Created by Administrator on 2017/7/26.
 */
public class HeadStyleModel implements HeadStyleContract.Model {
    private final HeadStylePresenter headStylePresent;

    public HeadStyleModel(HeadStylePresenter headStylePresenter) {
        this.headStylePresent=headStylePresenter;
    }
}
