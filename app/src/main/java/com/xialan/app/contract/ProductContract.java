package com.xialan.app.contract;

import com.xialan.app.base.BaseView;
import com.xialan.app.bean.ProductBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */
public interface ProductContract {


    interface View extends BaseView{
        //获取tab成功
        void tabSuccess(String data);
        //获取tab 失败
        void tabFail();
        //获取产品成功
        void productSuccess(List<ProductBean> data ,int position);
        //获取产品失败
        void productFail();
    }

    interface Presenter {
        /**
         * 获取产品tab名称
         */
        void getDataForTabName();
        //获取tab成功
        void tabNameSuccess(String data);
        //获取tab 失败
        void tabNameFailed();
        /**
         * 获取当前所在索引的产品信息
         * @param position
         */
       void getDataProductDataForPosition(int position);

        //获取productData成功
        void productDataSuccess(List<ProductBean> data,int position);
        //获取productData 失败
        void productDataFailed();
    }
    interface Model {
        /**
         *获取tab名称
         */
        void getDataTabNameFromNet();
        /**
         * 获取当前所在索引的产品信息
         * @param position
         */
        void getDataProductDataForPositionFromNet(int position);


    }
}
