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
import com.xialan.app.adapter.SkinAdapter;
import com.xialan.app.adapter.SkinSampleAdapter;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.SkinBean;
import com.xialan.app.bean.SkinKeepingBean;
import com.xialan.app.contract.SkinContract;
import com.xialan.app.presenter.SkinPresenter;
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
public class SkinFragment extends BaseFragment implements SkinContract.View, View.OnClickListener {
    @BindView(R.id.vp_pager_skin)
    ViewPager view_pager;
    @BindView(R.id.tab_skin_66)
    TabLayout tab;
    //子view集合
    private List<View> mViewList;
    private View view1;
    private View view2;
    private RecyclerView mRecyclerView;
    private RecyclerView recyclerView;
    private List<SkinBean> skinList = new ArrayList<>();
    private SkinAdapter adapter;
    private int lastSelectPosition=0;
    private ImageView imageViewDetail;
    private TextView tvSkinDetailContent;
    private SkinPresenter skinPresenter;
    private  List<SkinKeepingBean> skinKeepingList = new ArrayList<>();
    private SkinSampleAdapter adapter2;
    private TextureView surfaceView_skup;
    private Camera2Manager camera2Manager;
    private String userId;
    private RelativeLayout rl_capture;
    private Button btn_camera;
    private Button btn_save;
    private Button still_button;
    private ImageView iv_skin_camera;
    private Bitmap result;
    private int lastSelectPositionSkupKeeping = 0;
    private ImageView iv_skin_show;
    private CustomProgressBar customProgressBar;
    private RelativeLayout rl_login;
    private Button btn_login;
    private Observable<String> takephotobs;
    private Observable<String> netstate;

    @Override
    protected void initVariables() {

    }

    @Override
    protected int getContentId() {
        return R.layout.skin_fagment;
    }

    @Override
    protected void loadData() {
        tab.addTab(tab.newTab().setText("1"));
        tab.addTab(tab.newTab().setText("2"));
        tab.setTabMode(TabLayout.GRAVITY_FILL);
        mViewList = new ArrayList<>();
        view1 = UIUtils.inflate(R.layout.skin_viewpager_item_1);
        view2 = UIUtils.inflate(R.layout.skin_viewpager_item_2);
        mViewList.add(view1);
        mViewList.add(view2);
        MyAdapter myAdapter = new MyAdapter();
        view_pager.setAdapter(myAdapter);
        tab.setupWithViewPager(view_pager);
        tab.setTabsFromPagerAdapter(myAdapter);
        initView1();
        initView2();
    }

    /**
     * 初始化第一页面
     */
    private void initView1() {
        recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view_skin);
        imageViewDetail = (ImageView) view1.findViewById(R.id.iv_skin_item_detail);
        tvSkinDetailContent = (TextView) view1.findViewById(R.id.tv_skin_detail_content);
        surfaceView_skup = (TextureView) view1.findViewById(R.id.surfaceView_skup);
        rl_capture = (RelativeLayout) view1.findViewById(R.id.rl_capture);
        btn_camera = (Button) view1.findViewById(R.id.btn_camera);
        btn_save = (Button) view1.findViewById(R.id.btn_save);
        still_button = (Button) view1.findViewById(R.id.still_button);
        iv_skin_camera = (ImageView) view1.findViewById(R.id.iv_skin_camera);
        adapter = new SkinAdapter(skinList);
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
                    SkinBean skinBean = (SkinBean) data.get(lastSelectPosition);
                    skinBean.setIs_select(false);
                    SkinBean skinBean1 = (SkinBean) data.get(position);
                    skinBean1.setIs_select(true);
//                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(lastSelectPosition);
                    adapter.notifyItemChanged(position);
                    lastSelectPosition = position;
                    load_detail(skinBean1);
                }
            }
        });
        skinPresenter.getSkinSampleData();
        camera2Manager = new Camera2Manager(surfaceView_skup, 0,getActivity());
        btn_save.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        still_button.setOnClickListener(this);
    }
    /**
     * 加载详情
     *
     */
    private void load_detail(SkinBean skinBean) {
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/skin_sample/"+skinBean.getImagedrawage()),imageViewDetail);
        tvSkinDetailContent.setText(skinBean.getDetailContent());
    }
    /**
     * 初始化第二页面
     */
    private void initView2() {
        mRecyclerView = (RecyclerView) view2.findViewById(R.id.recycler_view_skin_2);
        iv_skin_show = (ImageView) view2.findViewById(R.id.iv_skin_show);
        rl_login = (RelativeLayout) view2.findViewById(R.id.ll_login);
        btn_login = (Button) view2.findViewById(R.id.btn_login_b_a);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter2 = new SkinSampleAdapter(R.layout.skup_view1_item, skinKeepingList);
        mRecyclerView.setAdapter(adapter2);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (lastSelectPositionSkupKeeping == position) {
                    return;
                } else {
                    List data = baseQuickAdapter.getData();
                    SkinKeepingBean skinKeepingBean = (SkinKeepingBean) data.get(lastSelectPositionSkupKeeping);
                    skinKeepingBean.setIs_Select(false);
                    SkinKeepingBean skinKeepingBean1 = (SkinKeepingBean) data.get(position);
                    skinKeepingBean1.setIs_Select(true);
                    adapter2.notifyItemChanged(lastSelectPositionSkupKeeping);
                    lastSelectPositionSkupKeeping = position;
                    adapter2.notifyItemChanged(position);
                    load_detai2(skinKeepingBean1);
                }
            }
        });
    }

    /**
     * 详细数据
     * @param skinKeepingBean1
     */
    private void load_detai2(SkinKeepingBean skinKeepingBean1) {
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("/IBSync/skin/" + skinKeepingBean1.getIv_keep_lv()), iv_skin_show);
    }

    @Override
    public void onResume() {
        super.onResume();
        userId = XLApplication.uid;
        if (userId !=null&& !userId .equals("")){
            skinPresenter.getSkinForUserdata(userId);
            rl_login.setVisibility(View.GONE);
        }else{
            rl_login.setVisibility(View.VISIBLE);
        }

        netstate = RxBus.get().register("NETSTATE", String.class);
        netstate.subscribe(new BaseSubscriber<String>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //网络已连接
                skinPresenter.getSkinSampleData();
                if (userId !=null&& !userId .equals("")){
                    skinPresenter.getSkinForUserdata(userId);
                    rl_login.setVisibility(View.GONE);
                }else{
                    rl_login.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        skinPresenter = new SkinPresenter(this);
        return skinPresenter;
    }

    @Override
    public void onSkinSampleSuccess(List<SkinBean> skinBeanList) {
        skinList.addAll(skinBeanList);
        load_detail(skinBeanList.get(0));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSkinSampleFailed() {
        UIUtils.showToast(getActivity(),"网络未连接!");
    }

    @Override
    public void onSkinUserDataSuccess(List<SkinKeepingBean> skinKeepingBeanList) {
        if (skinKeepingList.size() !=0){
            skinKeepingList.clear();
        }
        skinKeepingList.addAll(skinKeepingBeanList);
        adapter2.notifyDataSetChanged();
        load_detai2(skinKeepingBeanList.get(0));
    }

    @Override
    public void onSkinUserDataFailed() {
        UIUtils.showToast(getActivity(),"请求失败!");
    }


    @Override
    public void savePicSuccess() {
        UIUtils.showToast(getActivity(),"上传成功!");
        rl_capture.setVisibility(View.VISIBLE);
        skinPresenter.getSkinForUserdata(userId);
        iv_skin_camera.setImageBitmap(null);
    }

    @Override
    public void savePicFailed() {
        UIUtils.showToast(getActivity(),"上传失败!");
        rl_capture.setVisibility(View.VISIBLE);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                //保存照片
                String pathname = UIUtils.getInnerSDCardPath() + "/skup.png";
                BitmapUtil.compressBmpToFile(result, new File(pathname));
                upLoadImageFile(pathname);
                break;
            case R.id.btn_camera:
                //重新拍照
                rl_capture.setVisibility(View.VISIBLE);
                iv_skin_camera.setImageBitmap(null);
                break;
            case R.id.still_button:
                takePhoto();

                break;
        }
    }

    private void takePhoto() {

        //拍照
        customProgressBar = new CustomProgressBar(getActivity());
        customProgressBar.setMessage("拍照准备中...请勿动...").show();
        rl_capture.setVisibility(View.GONE);
        camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                result = bitmap;
                iv_skin_camera.setImageBitmap(result);
                customProgressBar.dismiss();
            }

            @Override
            public void captureFailed() {
                if (customProgressBar!=null){
                    customProgressBar.dismiss();
                }
            }
        });

    }

    private void upLoadImageFile(String pathname) {
        if (userId == "") {
            startLogin();
            return;
        }
        skinPresenter.upLoadPicToNet(userId,UIUtils.getAdresseMAC(getActivity()),pathname);
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
         tab.getTabAt(1).setText("我的发质");
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "皮肤样例";
                case 1:
                    return "我的皮肤";
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
