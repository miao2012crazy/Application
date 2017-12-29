package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.WeChatLoginContract;
import com.xialan.app.model.WeChatLoginModel;

/**
 * Created by Administrator on 2017/10/25.
 */

public class WeChatLoginPresenter extends BasePresenter implements WeChatLoginContract.Presenter {
    private WeChatLoginContract.View mView;
    private WeChatLoginModel weChatLoginModel;

    public WeChatLoginPresenter(WeChatLoginContract.View mView) {
        this.mView = mView;
        weChatLoginModel = new WeChatLoginModel(this);
    }



}
