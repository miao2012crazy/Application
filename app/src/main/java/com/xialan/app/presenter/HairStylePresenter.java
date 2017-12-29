package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.HairStyleContract;
import com.xialan.app.contract.HeadStyleContract;
import com.xialan.app.model.HairStyleModel;
import com.xialan.app.model.HeadStyleModel;

/**
 * Created by Administrator on 2017/7/26.
 */
public class HairStylePresenter extends BasePresenter implements HairStyleContract.Presenter {
    private final HairStyleContract.View mView;

    public HairStylePresenter(HairStyleContract.View view) {
        this.mView=view;
        HairStyleModel headStyleModel = new HairStyleModel(this);
    }
}
