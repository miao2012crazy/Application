package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.DetailContract;
import com.xialan.app.model.DetailModel;

/**
 * Created by Administrator on 2017/7/31.
 */
public class DetailPresenter extends BasePresenter implements DetailContract.Presenter {
    private final DetailContract.View mView;

    public DetailPresenter(DetailContract.View view) {
        this.mView=view;
        DetailModel detailModel = new DetailModel(this);
    }
}
