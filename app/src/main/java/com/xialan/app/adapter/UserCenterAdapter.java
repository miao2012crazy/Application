package com.xialan.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.UserCenterBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/25.
 */

public class UserCenterAdapter extends BaseQuickAdapter<UserCenterBean, BaseViewHolder>{

    public UserCenterAdapter(List<UserCenterBean> data) {
        super(R.layout.layout_usercenter_item,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserCenterBean userCenterBean) {
        baseViewHolder.setImageDrawable(R.id.iv_icon,userCenterBean.getImage_icon())
                .setText(R.id.tv_title,userCenterBean.getTitle());
    }
}
