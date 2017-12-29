package com.xialan.app.presenter;

import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.ProductBean;
import com.xialan.app.contract.ProductContract;
import com.xialan.app.model.ProductModel;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public class ProductPresenter extends BasePresenter implements ProductContract.Presenter {
    private ProductContract.View mView;
    private final ProductModel productModel;

    public ProductPresenter(ProductContract.View view) {
        this.mView = view;
        productModel = new ProductModel(this);
    }

    @Override
    public void getDataForTabName() {
        mView.showProgress();
        productModel.getDataTabNameFromNet();
    }

    @Override
    public void tabNameSuccess(String data) {
        mView.hideProgress();
        mView.tabSuccess(data);

    }

    @Override
    public void tabNameFailed() {
        mView.hideProgress();
        mView.tabFail();
    }

    @Override
    public void getDataProductDataForPosition(int position) {
        mView.showProgress();
        productModel.getDataProductDataForPositionFromNet(position);
    }

    @Override
    public void productDataSuccess(List<ProductBean> data,int positon) {
        mView.hideProgress();
        mView.productSuccess(data,positon);
    }

    @Override
    public void productDataFailed() {
        mView.hideProgress();
        mView.productFail();
    }





}
