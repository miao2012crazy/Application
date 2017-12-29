package com.xialan.app.adapter;

import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.HairStyleBABean;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */
public class SkupAdapter extends BaseQuickAdapter<SkupBean,BaseViewHolder> {

    public SkupAdapter(List<SkupBean> data) {
        super(R.layout.skup_item_1,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SkupBean skupBean) {
        baseViewHolder.setText(R.id.tv_title_skup_item,skupBean.getTitle())
        .setBackgroundRes(R.id.ll_skup_checkable,setSelectForPosition(skupBean));
        ImageView view = (ImageView) baseViewHolder.getView(R.id.iv_skup_item);
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/skin_sample/"+skupBean.getImagedrawage()),view);
    }
    /**
     * 设置是否选中
     * @return
     * @param
     */
    private int setSelectForPosition(SkupBean skupBean) {
        if (skupBean.is_select()){
            return R.drawable.btn_select;
        }else{
            return 0;
        }
    }
}
