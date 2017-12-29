package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.base.BaseModel;
import com.xialan.app.bean.ProductBean;
import com.xialan.app.bean.ProductDetailBean;
import com.xialan.app.contract.ProductContract;
import com.xialan.app.presenter.ProductPresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.retrofit.RetrofitUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.HttpUtil;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.SharePreUtils;
import com.xialan.app.utils.UIUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/19.
 */
public class ProductModel extends BaseModel implements ProductContract.Model {
    private ProductPresenter mPresenter;
    private  List<ProductBean> productBeanList = new ArrayList<>();
    public ProductModel(ProductPresenter mPresenter) {
        this.mPresenter=mPresenter;
    }

    @Override
    public void getDataTabNameFromNet() {
        RetrofitUtil.setCallBack(RetrofitUtil.getApiservice().getData(), new RetrofitUtil.HttpCallBack() {
            @Override
            public void onstart() {

            }

            @Override
            public void onSusscess(String data) {
                SharePreUtils.put(UIUtils.getContext(),"tab_name",data);
                mPresenter.tabNameSuccess(data);
            }

            @Override
            public void onError(String meg) {
                String tab_name = (String) SharePreUtils.get(UIUtils.getContext(), "tab_name", "");
                if (tab_name.equals("")){
                    mPresenter.tabNameFailed();
                }else{
                    mPresenter.tabNameSuccess(tab_name);
                }

            }

            @Override
            public void onCompleted() {

            }
        });
    }
    @Override
    public void getDataProductDataForPositionFromNet(final int position) {
        RetrofitUtil.setCallBack(RetrofitUtil.getApiservice().getDataForProductDetail(position + ""), new RetrofitUtil.HttpCallBack() {
            @Override
            public void onstart() {

            }

            @Override
            public void onSusscess(String data) {
                SharePreUtils.put(UIUtils.getContext(),position+"",data);
                productBeanList.clear();
                 ProductBean productBean = initSuccessData(position, data);
                productBeanList.add(productBean);
                mPresenter.productDataSuccess(productBeanList,position);
            }

            @Override
            public void onError(String meg) {
                String data = (String) SharePreUtils.get(UIUtils.getContext(), position + "", "");
                if (data.equals("")){
                    mPresenter.productDataFailed();
                }else{
                    productBeanList.clear();
                    ProductBean productBean = initSuccessData(position, data);
                    productBeanList.add(productBean);
                    mPresenter.productDataSuccess(productBeanList,position);
                }
            }
            @Override
            public void onCompleted() {

            }
        });
    }

    /**
     * 初始化请求成功数据
     *  @param start_position
     * @param responseBody
     */
    private ProductBean initSuccessData(int start_position, String responseBody)  {
        //获取数据 处理数据
        List<ProductDetailBean> productDetailBeenList = CommonUtil.parseData(responseBody);
        ProductBean productBean = null;
        switch (productDetailBeenList.size()) {
            case 1:
                productBean = new ProductBean();
                productBean.setPosition(start_position + "");
                productBean.setImageUrl_0(productDetailBeenList.get(0).getProduct_image_url());
                productBean.setProduct_id_0(productDetailBeenList.get(0).getProduct_id());
                productBean.setProduct_video_url_0(productDetailBeenList.get(0).getVideo_url());
                break;
            case 2:
                productBean = new ProductBean();
                productBean.setPosition(start_position + "");
                productBean.setImageUrl_0(productDetailBeenList.get(0).getProduct_image_url());
                productBean.setImageUrl_1(productDetailBeenList.get(1).getProduct_image_url());
                productBean.setProduct_id_0(productDetailBeenList.get(0).getProduct_id());
                productBean.setProduct_id_1(productDetailBeenList.get(1).getProduct_id());
                productBean.setProduct_video_url_0(productDetailBeenList.get(0).getVideo_url());
                productBean.setProduct_video_url_1(productDetailBeenList.get(1).getVideo_url());
                break;
            case 3:
                productBean = new ProductBean();
                productBean.setPosition(start_position + "");
                productBean.setImageUrl_0(productDetailBeenList.get(0).getProduct_image_url());
                productBean.setImageUrl_1(productDetailBeenList.get(1).getProduct_image_url());
                productBean.setImageUrl_2(productDetailBeenList.get(2).getProduct_image_url());
                productBean.setProduct_id_0(productDetailBeenList.get(0).getProduct_id());
                productBean.setProduct_id_1(productDetailBeenList.get(1).getProduct_id());
                productBean.setProduct_id_2(productDetailBeenList.get(2).getProduct_id());
                productBean.setProduct_video_url_0(productDetailBeenList.get(0).getVideo_url());
                productBean.setProduct_video_url_1(productDetailBeenList.get(1).getVideo_url());
                productBean.setProduct_video_url_2(productDetailBeenList.get(2).getVideo_url());
                break;
            case 4:
                productBean = new ProductBean();
                productBean.setPosition(start_position + "");
                productBean.setImageUrl_0(productDetailBeenList.get(0).getProduct_image_url());
                productBean.setImageUrl_1(productDetailBeenList.get(1).getProduct_image_url());
                productBean.setImageUrl_2(productDetailBeenList.get(2).getProduct_image_url());
                productBean.setImageUrl_3(productDetailBeenList.get(3).getProduct_image_url());
                productBean.setProduct_id_0(productDetailBeenList.get(0).getProduct_id());
                productBean.setProduct_id_1(productDetailBeenList.get(1).getProduct_id());
                productBean.setProduct_id_2(productDetailBeenList.get(2).getProduct_id());
                productBean.setProduct_id_3(productDetailBeenList.get(3).getProduct_id());
                productBean.setProduct_video_url_0(productDetailBeenList.get(0).getVideo_url());
                productBean.setProduct_video_url_1(productDetailBeenList.get(1).getVideo_url());
                productBean.setProduct_video_url_2(productDetailBeenList.get(2).getVideo_url());
                productBean.setProduct_video_url_3(productDetailBeenList.get(3).getVideo_url());
                break;

        }
        return productBean;
    }

}
