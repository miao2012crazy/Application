package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.HeadStyleContract;
import com.xialan.app.model.HeadStyleModel;

/**
 * Created by Administrator on 2017/7/26.
 */
public class HeadStylePresenter extends BasePresenter implements HeadStyleContract.Presenter {
    private final HeadStyleContract.View mView;

    public HeadStylePresenter(HeadStyleContract.View view) {
        this.mView=view;
        HeadStyleModel headStyleModel = new HeadStyleModel(this);
    }
}
