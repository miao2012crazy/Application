package com.xialan.app.view.mainfragment;

import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.xialan.app.R;
import com.xialan.app.adapter.GalleryAdapter;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.HairStyleBABean;
import com.xialan.app.contract.HeadStyleContrastContract;
import com.xialan.app.presenter.HeadStyleContrastPresenter;
import com.xialan.app.ui.Camera2Manager;
import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.BitmapUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/8/ic_hair_boy.
 */
public class HeadStyleContrastFragment extends BaseFragment implements HeadStyleContrastContract.View {

    @BindView(R.id.iv_show_big)
    ImageView ivShowBig;
    @BindView(R.id.camera_hair_b_a)
    TextureView cameraHairBA;
    @BindView(R.id.still_button)
    Button stillButton;
    @BindView(R.id.iv_show)
    ImageView iv_show;
    @BindView(R.id.tv_time_camera)
    TextView tv_time_camera;
    @BindView(R.id.recycler_view_b_a)
    RecyclerView recyclerViewBA;
    @BindView(R.id.btn_login_b_a)
    Button btnLogin;
    @BindView(R.id.ll_login)
    RelativeLayout llLogin;
    @BindView(R.id.restart_still_button)
    Button restartStillButton;
    @BindView(R.id.rl_capture)
    RelativeLayout rlCapture;
    @BindView(R.id.ll_reset_btn)
    LinearLayout llResetBtn;
    private int lastSelectPosition = 0;
    private String userId;
    private Bitmap result;
    private GalleryAdapter galleryAdapter;
    private HeadStyleContrastPresenter headStyleContrastPresenter;
    private Camera2Manager camera2Manager;
    private CustomProgressBar customProgressBar;
    private Observable<String> stringObservable;

    @Override
    protected int getContentId() {
        return R.layout.fragment_headstyle_contrast;
    }

    @Override
    protected void loadData() {
        startCamera();
        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        initRecycler();

    }

    private void startCamera() {
        camera2Manager = new Camera2Manager(cameraHairBA, 1,getActivity());
    }

    /**
     * 初始化列表
     */
    private void initRecycler() {

    }

    @Override
    protected BasePresenter createPresenter() {
        headStyleContrastPresenter = new HeadStyleContrastPresenter(this);
        return headStyleContrastPresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        userId = XLApplication.uid;
        if (userId.equals("") || userId == null) {
            llLogin.setVisibility(View.VISIBLE);
            return;
        }
        llLogin.setVisibility(View.GONE);
        initRecyclerList();

        stringObservable = RxBus.get().register("takephoto", String.class);
        stringObservable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if (s.equals("headba")){
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            takePhoto();
                        }
                    });
                }
            }
        });


    }


    /**
     * 使用recyclerview 展示图片
     */
    private void initRecyclerList() {
        if (userId == "" || userId == null) {
            llLogin.setVisibility(View.VISIBLE);
            return;
        }
        headStyleContrastPresenter.getDataForUserImage(userId);
    }

    /**
     * 初始化高级图片展示
     *
     * @param hairStyleBABean1
     */
    private void initDetail(HairStyleBABean hairStyleBABean1) {
        ImageLoaderManager.displayImage(HttpUrl.setGetUrl("IBSync/bna/" + hairStyleBABean1.getImage()), ivShowBig);
        tv_time_camera.setText(hairStyleBABean1.getTitle());
    }


    @OnClick({R.id.still_button, R.id.btn_save_photo, R.id.btn_login_b_a, R.id.restart_still_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.still_button:
                takePhoto();
                break;
            case R.id.btn_save_photo:
                try {
                    String pathname = UIUtils.getInnerSDCardPath() + "/0.png";
                    BitmapUtil.compressBmpToFile(result, new File(pathname));
                    upLoadImageFile(pathname);
                    //保存照片
                    rlCapture.setVisibility(View.VISIBLE);
                    llResetBtn.setVisibility(View.GONE);
                    iv_show.setVisibility(View.GONE);
                } catch (Exception e) {
                    UIUtils.showToast(getActivity(), "还没有拍照!");
                }
                break;
            case R.id.btn_login_b_a:
                startLogin();
                break;
            case R.id.restart_still_button:
                //重新拍照
                rlCapture.setVisibility(View.VISIBLE);
                llResetBtn.setVisibility(View.GONE);
                iv_show.setVisibility(View.GONE);
                break;
        }
    }

    private void takePhoto() {
        customProgressBar = new CustomProgressBar(getActivity());
        customProgressBar.setMessage("拍照取景进行中。。。").show();
        camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                result = bitmap;
                if (customProgressBar != null) {
                    customProgressBar.setMessage("拍照完成!");
                    customProgressBar.dismiss();
                }
                iv_show.setVisibility(View.VISIBLE);
                iv_show.setImageBitmap(result);
                //拍照
                rlCapture.setVisibility(View.GONE);
                llResetBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void captureFailed() {
                if (customProgressBar!=null){
                    customProgressBar.dismiss();
                }
            }
        });
    }

    /**
     * 上传图片
     *
     * @param filePath 图片路径
     */
    private void upLoadImageFile(String filePath) {
        if (userId == "") {
            startLogin();
            return;
        }
        headStyleContrastPresenter.upLoadPicToNet(userId, UIUtils.getAdresseMAC(getActivity()), filePath);
    }

    @Override
    public void getDataForUserImageSuccess(List<HairStyleBABean> hairStyleBABeanList) {
        initDetail(hairStyleBABeanList.get(0));

        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBA.setLayoutManager(layout);
        //设置适配器
        galleryAdapter = new GalleryAdapter(hairStyleBABeanList);
        recyclerViewBA.setAdapter(galleryAdapter);
        recyclerViewBA.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (lastSelectPosition == position) {
                    return;
                } else {
                    List data = baseQuickAdapter.getData();
                    HairStyleBABean hairStyleBABean = (HairStyleBABean) data.get(lastSelectPosition);
                    hairStyleBABean.setSelect(false);
                    HairStyleBABean hairStyleBABean1 = (HairStyleBABean) data.get(position);
                    hairStyleBABean1.setSelect(true);
                    galleryAdapter.notifyItemChanged(lastSelectPosition);
                    galleryAdapter.notifyItemChanged(position);
                    lastSelectPosition = position;
                    initDetail(hairStyleBABean1);
                }
            }
        });
    }
    @Override
    public void getDataForUserImageFailed() {
        UIUtils.showToast(getActivity(), "请求错误!");
    }
    @Override
    public void savePicSuccess() {
        UIUtils.showToast(getActivity(), "照片保存成功!");
        initRecyclerList();
//        iv_show.setVisibility(View.GONE);
    }
    @Override
    public void savePicFailed() {
        UIUtils.showToast(getActivity(), "照片保存失败!");
    }
    @Override
    public void onPause() {
        super.onPause();
        camera2Manager.stopCamera();
        RxBus.get().unregister("takephoto",stringObservable);
    }

}
