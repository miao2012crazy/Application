package com.xialan.app;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.ui.Camera2Manager;
import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.StringUtil;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.mainfragment.MainFragmentFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by Administrator on 2017/7/17.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.camera)
    ImageView camera;
    @BindView(R.id.skin)
    ImageView skin;
    @BindView(R.id.shop)
    ImageView shop;
    @BindView(R.id.user)
    ImageView user;
    @BindView(R.id.iv_headstyle_contrast)
    ImageView ivHeadstyleContrast;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.tv_b_a)
    TextView tvBA;
    @BindView(R.id.tv_skup)
    TextView tvSkup;
    @BindView(R.id.tv_skin)
    TextView tvSkin;
    @BindView(R.id.tv_training)
    TextView tvTraining;
    @BindView(R.id.tv_user)
    TextView tvUser;
    private ImageView[] imageArr;
    private int currentposition = 99;
    private CustomProgressBar customProgressBar;
    private TextView[] tvArr;
    private int pic_state = 0;
    private Bitmap result;
    private Camera2Manager camera2Manager;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        UIUtils.dip2px(20);
        XLApplication.uid = "";
        XLApplication.uid_sex = "";
        XLApplication.uid_head_url = "";
        XLApplication.uid_nick_name_2 = "";
        customProgressBar = UIUtils.getCustomProgressBar(this);
        setDefaultFragment();
        initImageIcon();
        setIvIconLarge(0);
        //版本更新

        Observable<Integer> auto_exit = RxBus.get().register("auto_exit", Integer.class);
        auto_exit.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final Integer integer) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        //Home Fragment 产品
                        setIvIconLarge(integer);
                        replaceFragment(integer);
                        currentposition = integer;
                    }
                });
            }
        });
    }



    /**
     * 初始化图标
     */
    private void initImageIcon() {
        imageArr = new ImageView[]{ivMain, ivHeadstyleContrast, camera, skin, shop, user};
        tvArr = new TextView[]{tvHome, tvBA, tvSkup, tvSkin, tvTraining, tvUser};
    }

    /**
     * 设置底部导航选择放大效果
     *
     * @param position
     */
    private void setIvIconLarge(int position) {
        for (int i = 0; i < imageArr.length; i++) {
            if (position == i) {
                imageArr[i].setScaleX(1.2f);
                imageArr[i].setScaleY(1.2f);
                tvArr[i].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                imageArr[i].setScaleX(1.0f);
                imageArr[i].setScaleY(1.0f);
                tvArr[i].setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    /**
     * 设置产品fragment为默认显示的fragment
     */
    private void setDefaultFragment() {
        replaceFragment(0);
        currentposition = 0;
    }


    /**
     * 切换fragment
     *
     * @param position
     */
    private void replaceFragment(int position) {
        if (position == currentposition) {
            return;
        }
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        BaseFragment fragment = MainFragmentFactory.getFragment(position);
        beginTransaction.replace(R.id.contentContainer, fragment);
        beginTransaction.commit();
    }
    @OnClick({R.id.ll_tab_product, R.id.ll_tab_contrast, R.id.ll_tab_skup, R.id.ll_tab_skin, R.id.ll_tab_shopping_cart, R.id.ll_tab_user_center})
    public void onViewClicked(View view) {
        if (currentposition == 0) {
            RxBus.get().post("hide_web_view", "");
        }
        switch (view.getId()) {
            case R.id.ll_tab_product:
                //Home Fragment 产品
                setIvIconLarge(0);
                replaceFragment(0);
                currentposition = 0;
                break;
            case R.id.ll_tab_contrast:
                //发型前后
                setIvIconLarge(1);
                replaceFragment(1);
                currentposition = 1;
                break;
            case R.id.ll_tab_skup:
                //发质管理页面
                setIvIconLarge(2);
                replaceFragment(2);
                currentposition = 2;
                break;
            case R.id.ll_tab_skin:
                //皮肤管理
                setIvIconLarge(3);
                replaceFragment(3);
                currentposition = 3;
                break;

            case R.id.ll_tab_shopping_cart:
                //购物车
                setIvIconLarge(4);
                replaceFragment(5);
                currentposition = 5;
                break;
            case R.id.ll_tab_user_center:
                //个人中心
                setIvIconLarge(5);
                replaceFragment(6);
                currentposition = 7;
                break;
        }
    }



    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //自动更新
        UpdateAppManager updateAppManager = new UpdateAppManager(this);
        updateAppManager.getUpdateMsg();//检查更新
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        String uid = XLApplication.uid;
        if (!StringUtil.isEmpty(uid)){
            RxBus.get().post("onUserInteraction","");
        }
    }
}
