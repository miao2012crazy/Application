package com.xialan.app.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xialan.app.R;
import com.xialan.app.bean.TrainingVideoBean;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/ic_hair_boy.
 */
public class TrainingVideoAdapter extends BaseQuickAdapter<TrainingVideoBean, BaseViewHolder> {


    public TrainingVideoAdapter(List<TrainingVideoBean> data) {
        super(R.layout.training_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TrainingVideoBean trainingVideoBean) {
        ImageView view = (ImageView) baseViewHolder.getView(R.id.iv_video_image);
        CardView card_view = (CardView) baseViewHolder.getView(R.id.card_view_training);
        RelativeLayout relativeLayout = (RelativeLayout) baseViewHolder.getView(R.id.rl_progress);
        ImageView ic_video_1 = (ImageView) baseViewHolder.getView(R.id.iv_video_start);
        baseViewHolder.setText(R.id.tv_shopping_title, trainingVideoBean.getTitle())
                .setText(R.id.tv_video_source, "来源：" + trainingVideoBean.getSoruce())
                .setText(R.id.tv_shopping_price, "时间：" + trainingVideoBean.getDate())
                .setText(R.id.tv_tag, "分类：" + trainingVideoBean.getCategory_id())
                .addOnClickListener(R.id.rl_progress)
                .linkify(R.id.tv_shopping_title);
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/lecture/images/" + trainingVideoBean.getIamge_name()), view);
        initCardView(trainingVideoBean.getIsSelected(), card_view, relativeLayout, ic_video_1);

    }

    private void initCardView(String isSelected, CardView card_view, RelativeLayout relativeLayout, ImageView ic_video_1) {
        switch (isSelected) {
            case "0":
                card_view.setCardBackgroundColor(Color.parseColor("#60ffffff"));
                relativeLayout.setVisibility(View.INVISIBLE);
                break;
            case "1":
                card_view.setCardBackgroundColor(Color.parseColor("#90ffffff"));
                relativeLayout.setVisibility(View.VISIBLE);
                Animation circle_anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.anim_round_rotate);
                LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
                circle_anim.setInterpolator(interpolator);
                ic_video_1.startAnimation(circle_anim);  //开始动画
                break;
        }
    }
}
