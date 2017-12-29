package com.xialan.app.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.HairStyleBABean;
import com.xialan.app.bean.HairStyleBean;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HairStyleAdapter extends BaseQuickAdapter<HairStyleBean, BaseViewHolder> {
    public HairStyleAdapter(int layoutResId, List<HairStyleBean> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, HairStyleBean styleBean) {
        baseViewHolder.setImageDrawable(R.id.iv_hair,styleBean.getImage());
    }
}
