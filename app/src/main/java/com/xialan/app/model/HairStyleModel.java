package com.xialan.app.model;

import com.xialan.app.contract.HairStyleContract;
import com.xialan.app.presenter.HairStylePresenter;
import com.xialan.app.presenter.HeadStylePresenter;

/**
 * Created by Administrator on 2017/7/26.
 */
public class HairStyleModel implements HairStyleContract.Model {
    private final HairStylePresenter headStylePresent;

    public HairStyleModel(HairStylePresenter headStylePresenter) {
        this.headStylePresent=headStylePresenter;
    }
}
