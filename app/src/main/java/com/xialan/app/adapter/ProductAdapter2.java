package com.xialan.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lsjwzh.widget.recyclerviewpager.TabLayoutSupport;
import com.xialan.app.R;
import com.xialan.app.bean.ProductBean;
import com.xialan.app.bean.ProductDetailBean;
import com.xialan.app.retrofit.RetrofitUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.HttpUtil;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.StringUtil;
import com.xialan.app.utils.UIUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/6.
 */

public class ProductAdapter2 extends BaseQuickAdapter<ProductBean, BaseViewHolder> implements TabLayoutSupport.ViewPagerTabLayoutAdapter {

    private final String[] split;
    private ImageView[] imageViewsArr;

    public ProductAdapter2(List<ProductBean> data, String[] split) {
        super(R.layout.product_item, data);
        this.split = split;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ProductBean productBean) {
        try{
            ImageView image2_view_0 = (ImageView) baseViewHolder.getView(R.id.image2_view_0);
            ImageView image2_view_1 = (ImageView) baseViewHolder.getView(R.id.image2_view_1);
            ImageView image2_view_2 = (ImageView) baseViewHolder.getView(R.id.image2_view_2);
            ImageView image2_view_3 = (ImageView) baseViewHolder.getView(R.id.image2_view_3);
            image2_view_0.setTag(productBean.getProduct_id_0());
            image2_view_1.setTag(productBean.getProduct_id_1());
            image2_view_2.setTag(productBean.getProduct_id_2());
            image2_view_3.setTag(productBean.getProduct_id_3());
            imageViewsArr = new ImageView[]{image2_view_0, image2_view_1, image2_view_2, image2_view_3};
            if (image2_view_0.getTag() == productBean.getProduct_id_0()) {
                ImageLoaderManager.displayImageList(HttpUrl.setGetUrl("IBSync/images/" + productBean.getImageUrl_0()), imageViewsArr[0], 10);
            }
            if (image2_view_1.getTag() == productBean.getProduct_id_1()) {
                ImageLoaderManager.displayImageList(HttpUrl.setGetUrl("IBSync/images/" + productBean.getImageUrl_1()), imageViewsArr[1], 10);
            }
            if (image2_view_2.getTag() == productBean.getProduct_id_2()) {
                ImageLoaderManager.displayImageList(HttpUrl.setGetUrl("IBSync/images/" + productBean.getImageUrl_2()), imageViewsArr[2], 10);
            }
            if (image2_view_3.getTag() == productBean.getProduct_id_3()) {
                ImageLoaderManager.displayImageList(HttpUrl.setGetUrl("IBSync/images/" + productBean.getImageUrl_3()), imageViewsArr[3], 10);
            }
            baseViewHolder.addOnClickListener(R.id.image2_view_0).addOnClickListener(R.id.image2_view_1).addOnClickListener(R.id.image2_view_2).addOnClickListener(R.id.image2_view_3);

        }catch (Exception e){

        }
        }

    @Override
    public String getPageTitle(int i) {
        return split[i];
    }

}
