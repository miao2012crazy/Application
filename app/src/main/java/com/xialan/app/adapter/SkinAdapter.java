package com.xialan.app.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.SkinBean;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
public class SkinAdapter extends BaseQuickAdapter<SkinBean,BaseViewHolder> {

    public SkinAdapter(List<SkinBean> data) {
        super(R.layout.skup_item_1,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SkinBean skinBean) {
        baseViewHolder.setText(R.id.tv_title_skup_item,skinBean.getTitle())
        .setBackgroundRes(R.id.ll_skup_checkable,setSelectForPosition(skinBean));
        ImageView view = (ImageView) baseViewHolder.getView(R.id.iv_skup_item);
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/skin_sample/"+skinBean.getImagedrawage()),view);
    }
    /**
     * 设置是否选中
     * @return
     * @param
     * @param skinBean
     */
    private int setSelectForPosition(SkinBean skinBean) {
        if (skinBean.is_select()){
            return R.drawable.btn_select;
        }else{
            return 0;
        }
    }
}
