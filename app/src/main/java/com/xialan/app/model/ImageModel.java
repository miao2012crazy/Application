package com.xialan.app.model;

import com.xialan.app.contract.ImageContract;

/**
 * Created by Administrator on 2017/7/31.
 */
public class ImageModel implements ImageContract.Model {
    private final ImageContract.Presenter presenter;

    public ImageModel(ImageContract.Presenter presenter) {
        this.presenter=presenter;

    }
}
