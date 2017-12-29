package com.xialan.app.view.mainfragment;


import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.adapter.RecyclerAdapter;
import com.xialan.app.adapter.SkupAdapter;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;
import com.xialan.app.contract.SkulpContract;
import com.xialan.app.presenter.SkulpPresenter;
import com.xialan.app.ui.Camera2Manager;
import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.BitmapUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/18.
 */
public class SkupFragment extends BaseFragment implements SkulpContract.View, View.OnClickListener {
    @BindView(R.id.vp_pager)
    ViewPager view_pager;
    @BindView(R.id.tab_skup_66)
    TabLayout tab;
    //子view集合
    private List<View> mViewList;
    private View view1;
    private View view2;
    private RecyclerView mRecyclerView;
    private SkupAdapter adapter;
    private List<SkupBean> skupList = new ArrayList<>();
    private ImageView ivSkupItemDetail;
    private int lastSelectPosition = 0;
    private int lastSelectPositionSkupKeeping = 0;
    private RecyclerAdapter adapter2;
    private RecyclerView recyclerView;
    private TextView tvSkupContent;
    private SkulpPresenter skulpPresenter;
    private TextureView surfaceView_skup;
    private Camera2Manager camera2Manager;
    private RelativeLayout rl_capture;
    private Button btn_camera;
    private Button btn_save;
    private Button still_button;
    private List<SkupKeepingBean> skupKeepingBeen = new ArrayList<>();
    private Bitmap result;
    private ImageView iv_skup_camera;
    private String userId;
    private ImageView iv_skup_show;
    private CustomProgressBar customProgressBar;
    private RelativeLayout ll_login;
    private Button btn_login;
    private Observable<String> takephotobs;
    private Observable<String> netstate;


    @Override
    protected int getContentId() {
        return R.layout.skup_fagment;
    }

    @Override
    protected void loadData() {
        tab.addTab(tab.newTab().setText("1"));
        tab.addTab(tab.newTab().setText("2"));
        mViewList = new ArrayList<>();
        view1 = UIUtils.inflate(R.layout.viewpager_item_1);
        view2 = UIUtils.inflate(R.layout.viewpager_item_2);
        mViewList.add(view1);
        mViewList.add(view2);
        MyAdapter myAdapter = new MyAdapter();
        view_pager.setAdapter(myAdapter);
        tab.setupWithViewPager(view_pager);
        tab.setTabMode(TabLayout.GRAVITY_FILL);
        tab.setTabsFromPagerAdapter(myAdapter);

        initView2();
        initView1_ImageView_Detail();
        initView1();
//        initScan();
        initView2_detail(0);

    }

    /**
     * 初始化 viewpager2详情
     */
    private void initView2_detail(int position) {
        TextView tv_content_2 = (TextView) view2.findViewById(R.id.tv_content_2);
        iv_skup_show = (ImageView) view2.findViewById(R.id.iv_skup_show);
        ll_login = (RelativeLayout) view2.findViewById(R.id.ll_login);
        btn_login = (Button) view2.findViewById(R.id.btn_login_b_a);
        TextView tv_skup_title_2 = (TextView) view2.findViewById(R.id.tv_skup_title_2);
        TextView tv_skup_product = (TextView) view2.findViewById(R.id.tv_skup_product);
        TextView tv_shiyongzhouqi = (TextView) view2.findViewById(R.id.tv_shiyongzhouqi);
        //启动登录
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });

    }


    /**
     * 初始化详情展示
     */
    private void initView1_ImageView_Detail() {
        ivSkupItemDetail = (ImageView) view1.findViewById(R.id.iv_skup_item_detail);
        tvSkupContent = (TextView) view1.findViewById(R.id.tv_skup_detail_content);
    }

    /**
     * 初始化listview 模拟数据
     */
    private void initView1() {
        recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view_skup);
        surfaceView_skup = (TextureView) view1.findViewById(R.id.surfaceView_skup);
        rl_capture = (RelativeLayout) view1.findViewById(R.id.rl_capture);
        btn_camera = (Button) view1.findViewById(R.id.btn_camera);
        btn_save = (Button) view1.findViewById(R.id.btn_save);
        still_button = (Button) view1.findViewById(R.id.still_button);
        iv_skup_camera = (ImageView) view1.findViewById(R.id.iv_skup_camera);
        adapter = new SkupAdapter(skupList);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (lastSelectPosition == position) {
                    return;
                } else {
                    List data = baseQuickAdapter.getData();
                    SkupBean skupBean = (SkupBean) data.get(lastSelectPosition);
                    skupBean.setIs_select(false);
                    SkupBean skupBean1 = (SkupBean) data.get(position);
                    skupBean1.setIs_select(true);
                    adapter.notifyItemChanged(lastSelectPosition);
                    adapter.notifyItemChanged(position);
                    lastSelectPosition = position;
                    load_detail(skupBean1);
                }
            }
        });
        skulpPresenter.getSkupSampleData();

        btn_save.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        still_button.setOnClickListener(this);
    }


    /**
     * 加载详情
     */
    private void load_detail(SkupBean skupBean) {
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/skin_sample/" + skupBean.getImagedrawage()), ivSkupItemDetail);
        tvSkupContent.setText(skupBean.getDetailContent());
    }

    /**
     * 初始化页面2
     */
    private void initView2() {
        mRecyclerView = (RecyclerView) view2.findViewById(R.id.recycler_view_skup_2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter2 = new RecyclerAdapter(R.layout.skup_view1_item, skupKeepingBeen);
        mRecyclerView.setAdapter(adapter2);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (lastSelectPositionSkupKeeping == position) {
                    return;
                } else {
                    List data = baseQuickAdapter.getData();
                    SkupKeepingBean skupKeepingBean = (SkupKeepingBean) data.get(lastSelectPositionSkupKeeping);
                    skupKeepingBean.setIs_Select(false);
                    SkupKeepingBean skupKeepingBean1 = (SkupKeepingBean) data.get(position);
                    skupKeepingBean1.setIs_Select(true);
                    adapter2.notifyItemChanged(lastSelectPositionSkupKeeping);
                    lastSelectPositionSkupKeeping = position;
                    initView2_detail(position);
                    adapter2.notifyItemChanged(position);
                    load_detai2(skupKeepingBean1);

                }
            }
        });


    }

    /**
     * 用户数据详情
     *
     * @param skupKeepingBean1
     */
    private void load_detai2(SkupKeepingBean skupKeepingBean1) {
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/skulp/" + skupKeepingBean1.getIv_keep_lv()), iv_skup_show);
    }

    @Override
    protected BasePresenter createPresenter() {
        skulpPresenter = new SkulpPresenter(this);
        return skulpPresenter;
    }

    @Override
    public void onSkupSampleSuccess(List<SkupBean> skupBeenList) {
        skupList.addAll(skupBeenList);
        load_detail(skupList.get(0));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSkupSampleFailed() {
        UIUtils.showToast(getActivity(), "网络未连接!");
    }

    @Override
    public void onSkupUserDataSuccess(List<SkupKeepingBean> skupKeepingBeenList) {
        if (skupKeepingBeen.size() != 0) {
            skupKeepingBeen.clear();
        }
        skupKeepingBeen.addAll(skupKeepingBeenList);
        adapter2.notifyDataSetChanged();
        load_detai2(skupKeepingBeenList.get(0));
    }

    @Override
    public void onSkupUserDataFailed() {
        UIUtils.showToast(getActivity(), "网络未连接!");
    }

    @Override
    public void savePicSuccess() {
        UIUtils.showToast(getActivity(), "上传成功!");
        rl_capture.setVisibility(View.VISIBLE);
        skulpPresenter.getSkupForUserdata(userId);
        iv_skup_camera.setImageBitmap(null);
    }

    @Override
    public void savePicFailed() {
        UIUtils.showToast(getActivity(), "上传失败!");
        rl_capture.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                //保存照片
                String pathname = UIUtils.getInnerSDCardPath() + "/skup.png";
                BitmapUtil.compressBmpToFile(result, new File(pathname));
                upLoadImageFile(pathname);
                break;
            case R.id.btn_camera:
                //重新拍照
                rl_capture.setVisibility(View.VISIBLE);
                iv_skup_camera.setImageBitmap(null);
                break;
            case R.id.still_button:
                takePhoto();
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        //拍照
//        customProgressBar = new CustomProgressBar(getActivity());
//        customProgressBar.setMessage("发质获取中，请保持...").show();
        rl_capture.setVisibility(View.GONE);
        camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                result = bitmap;
                iv_skup_camera.setImageBitmap(result);
//                if (customProgressBar!=null){
//                    customProgressBar.setMessage("获取完成！");
//                    customProgressBar.dismiss();
//                }

            }

            @Override
            public void captureFailed() {
                UIUtils.showToast(UIUtils.getContext(), "获取图像失败,请重启设备!");
            }
        });
    }


    private void upLoadImageFile(String filePath) {
        if (userId == "") {
            startLogin();
            return;
        }
        skulpPresenter.upLoadPicToNet(userId, UIUtils.getAdresseMAC(getActivity()), filePath);
    }

    @Override
    public void onResume() {
        super.onResume();
        userId = XLApplication.uid;
        camera2Manager = new Camera2Manager(surfaceView_skup, 0, getActivity());
        if (userId != null && !userId.equals("")) {
            skulpPresenter.getSkupForUserdata(userId);
            ll_login.setVisibility(View.GONE);
        } else {
            ll_login.setVisibility(View.VISIBLE);
        }
        netstate = RxBus.get().register("NETSTATE", String.class);
        netstate.subscribe(new BaseSubscriber<String>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //网络已连接
                skulpPresenter.getSkupSampleData();
                if (userId != null && !userId.equals("")) {
                    skulpPresenter.getSkupForUserdata(userId);
                    ll_login.setVisibility(View.GONE);
                } else {
                    ll_login.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 适配器
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {//必须实现
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {//必须实现
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
            container.removeView(mViewList.get(position));
        }

        /**
         * tab.getTabAt(0).setText("发质样例");
         * tab.getTabAt(1).setText("我的发质");
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "发质样例";
                case 1:
                    return "我的发质";
            }
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        camera2Manager.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RxBus.get().unregister("NETSTATE", netstate);
    }
}
