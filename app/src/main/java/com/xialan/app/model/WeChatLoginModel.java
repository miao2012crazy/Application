package com.xialan.app.model;

import com.xialan.app.contract.WeChatLoginContract;

/**
 * Created by Administrator on 2017/10/25.
 */

public class WeChatLoginModel implements WeChatLoginContract.Model {

    private  WeChatLoginContract.Presenter mPresenter;

    public WeChatLoginModel(WeChatLoginContract.Presenter mPresenter) {
        this.mPresenter=mPresenter;
    }
}
