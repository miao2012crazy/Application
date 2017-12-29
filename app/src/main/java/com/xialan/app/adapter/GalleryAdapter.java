package com.xialan.app.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.HairStyleBABean;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class GalleryAdapter extends BaseQuickAdapter<HairStyleBABean, BaseViewHolder> {

    public GalleryAdapter(List<HairStyleBABean> data) {
        super(R.layout.gallery_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HairStyleBABean hairStyleBABean) {

        baseViewHolder.setText(R.id.tv_time, hairStyleBABean.getTitle())
                .setBackgroundRes(R.id.iv_history, setSelectForPosition(hairStyleBABean))
                .addOnClickListener(R.id.iv_history).linkify(R.id.tv_time);

        ImageView view = (ImageView) baseViewHolder.getView(R.id.iv_history);
        initImage(hairStyleBABean,view);
    }

    private void initImage(HairStyleBABean hairStyleBABean, ImageView view) {
        ImageLoaderManager.displayImageList(HttpUrl.setGetUrl("IBSync/bna/" + hairStyleBABean.getImage()), view, UIUtils.dip2px(4));
    }

    /**
     * 设置是否选中
     *
     * @param hairStyleBABean
     * @return     */
    private int setSelectForPosition(HairStyleBABean hairStyleBABean) {
        if (hairStyleBABean.isSelect()) {
            return R.drawable.ic_b_a_select;
        } else {
            return 0;
        }
    }
}
