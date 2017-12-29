package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.ImageContract;
import com.xialan.app.model.ImageModel;

/**
 * Created by Administrator on 2017/7/31.
 */
public class ImagePresenter extends BasePresenter implements ImageContract.Presenter {
    private final ImageContract.View mView;

    public ImagePresenter(ImageContract.View view) {
        this.mView=view;
        ImageModel imageModel = new ImageModel(this);
    }
}
