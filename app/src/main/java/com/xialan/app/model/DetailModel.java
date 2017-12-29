package com.xialan.app.model;

import com.xialan.app.contract.DetailContract;
import com.xialan.app.presenter.DetailPresenter;

/**
 * Created by Administrator on 2017/7/31.
 */
public class DetailModel implements DetailContract.Model {
    private final DetailContract.Presenter mDetilPresenter;

    public DetailModel(DetailContract.Presenter detailPresenter) {
        this.mDetilPresenter= detailPresenter;
    }

}
