package com.xialan.app.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.SkupKeepingBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public class RecyclerAdapter extends BaseQuickAdapter<SkupKeepingBean,BaseViewHolder> {


    public RecyclerAdapter(int skup_view1_item, List<SkupKeepingBean> skupKeepingBeen) {
        super(skup_view1_item, skupKeepingBeen);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SkupKeepingBean skupKeepingsBean) {
        baseViewHolder.setText(R.id.tv_skup_title, skupKeepingsBean.getTitle()).linkify(R.id.tv_skup_title).setBackgroundRes(R.id.card_view_skup,getDrawable(skupKeepingsBean));
        initLv(skupKeepingsBean, baseViewHolder);
    }

    /**
     * 获取drawable 设置选中
     * @return
     * @param skupKeepingsBean
     */
    private int getDrawable(SkupKeepingBean skupKeepingsBean) {
        if (skupKeepingsBean.is_Select()){
            return R.drawable.btn_select;
        }else{
            return 0;
        }
    }


    private void initLv(SkupKeepingBean skupKeepingsBean, BaseViewHolder baseViewHolder) {
        switch (skupKeepingsBean.getIv_keep_lv()) {
            case "1":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_1);
                break;
            case "2":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_2);
                break;
            case "3":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_3);
                break;
            case "4":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_4);
                break;
            case "5":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_5);
                break;
            case "6":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_6);
                break;
            case "7":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_good_1);
                break;
            case "8":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_good_2);
                break;
            case "9":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_good_3);
                break;

            case "10":
                baseViewHolder.setImageResource(R.id.iv_keep_skup_lv, R.drawable.keep_good_4);
                break;

        }
    }
}