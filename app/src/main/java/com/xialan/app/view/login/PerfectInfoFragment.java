package com.xialan.app.view.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.PerfectInfoContract;
import com.xialan.app.presenter.PerfectInfoPresenter;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.ui.Camera2Manager;
import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.BitmapUtil;
import com.xialan.app.utils.CheckLoginUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.UIUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/9/19.
 */

public class PerfectInfoFragment extends BaseFragment implements PerfectInfoContract.View {

    @BindView(R.id.iv_perfect_img)
    ImageView ivPerfectImg;
    @BindView(R.id.iv_regedit_success_img)
    ImageView ivRegeditSuccessImg;
    @BindView(R.id.et_user_psd)
    EditText etUserPsd;
    @BindView(R.id.et_concert_psd)
    EditText etConcertPsd;
    @BindView(R.id.user_nickname)
    EditText userNickname;
    @BindView(R.id.et_user_age)
    EditText etUserAge;
    @BindView(R.id.rb_girl)
    RadioButton rbGirl;
    @BindView(R.id.rb_boy)
    RadioButton rbBoy;
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.ll_perfect_info)
    LinearLayout llPerfectInfo;
    @BindView(R.id.btn_regedit_success)
    Button btnRegeditSuccess;
    @BindView(R.id.rl_regedit_auto_login)
    LinearLayout rlRegeditAutoLogin;
    @BindView(R.id.iv_user_header)
    ImageView ivUserHeader;
    @BindView(R.id.et_user_recommender)
    EditText etUserRecommender;
    private PerfectInfoPresenter perfectInfoPresenter;
    private String psd;
    private String concertPsd;
    private String nick_name;
    private String user_age;
    private int sexState = -1;
    private String user_tel;
    private String user_pwd;
    private Camera2Manager camera2Manager;
    private Bitmap result;
    private String userRecommender;
    private String pathname = "";
    private Bitmap roundBitmap;
    private CustomProgressBar customProgressBar;
    private ImageView iv_camera_show;
    private Button btn_camera;
    private Button btn_save;
    private Button still_button;
    private Button btn_close_camera;
    private TextureView cameraView;
    private RelativeLayout rlCapture;

    @Override
    protected int getContentId() {
        return R.layout.fragment_perfect_info;
    }

    @Override
    protected void loadData() {
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_girl:
                        sexState = 1;
                        break;

                    case R.id.rb_boy:
                        sexState = 0;
                        break;
                }
            }
        });


    }

    @Override
    protected BasePresenter createPresenter() {
        perfectInfoPresenter = new PerfectInfoPresenter(this);
        return perfectInfoPresenter;
    }

    @OnClick({R.id.et_user_psd, R.id.et_concert_psd, R.id.user_nickname, R.id.et_user_age, R.id.btn_commit, R.id.btn_regedit_success, R.id.iv_user_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_user_psd:
                break;
            case R.id.et_concert_psd:
                break;
            case R.id.user_nickname:
                break;
            case R.id.et_user_age:
                break;
            case R.id.btn_commit:
                user_tel = XLApplication.uid;
                psd = etUserPsd.getText().toString();
                concertPsd = etConcertPsd.getText().toString();
                nick_name = userNickname.getText().toString();
                user_age = etUserAge.getText().toString();
                String verify_code = XLApplication.uid_verify_code;
                userRecommender = etUserRecommender.getText().toString();
                boolean b = checkInputState(psd, concertPsd, nick_name, user_age, sexState);
                if (b) {
                    //TODO 提交数据
                    perfectInfoPresenter.submitUserData(UIUtils.getAdresseMAC(getActivity()), verify_code, user_tel, psd, nick_name, user_age, sexState + "", userRecommender, pathname);
                    XLApplication.uid_psd = psd;
                }
                break;
            case R.id.btn_regedit_success:
                user_pwd = XLApplication.uid_psd;
                //TODO 自动登录逻辑
                NovateUtil.getInstance().call(NovateUtil.getApiService().login(this.user_tel, user_pwd), new BaseSubscriber<ResponseBody>() {
                    @Override
                    public void onError(Throwable e) {
                        UIUtils.showToast(getActivity(), "联网失败,请重试");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String[] dataForspild = CommonUtil.getDataForspild(responseBody.string());
                            CheckLoginUtil.parseLogin(dataForspild, user_tel);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.iv_user_header:
                initCameraDialog();
//                //TODO 启动相机(高清摄像头)
//                rlCamera.setVisibility(View.VISIBLE);
//                if (camera2Manager == null) {
//                    camera2Manager = new Camera2Manager(cameraView, 1, getActivity());
//                }
                break;
            /*case R.id.btn_cancle_camera:
                //取消拍照
                rlCamera.setVisibility(View.GONE);
                camera2Manager.stopCamera();
                break;

            case R.id.btn_camera:
                //重新拍照
                rlCapture.setVisibility(View.VISIBLE);
                ivCameraShow.setVisibility(View.GONE);
                break;
            case R.id.btn_save:
                if (roundBitmap == null) {
                    return;
                }
                ivUserHeader.setImageBitmap(roundBitmap);
                //保存照片
                rlCapture.setVisibility(View.VISIBLE);
                rlCamera.setVisibility(View.GONE);
                //保存照片
                pathname = UIUtils.getInnerSDCardPath() + "/user_header.png";
                BitmapUtil.compressBmpToFile(roundBitmap, new File(pathname));
                camera2Manager.stopCamera();
                break;
*//*
            case R.id.still_button:
                rlCapture.setVisibility(View.GONE);
                //拍照

                customProgressBar = new CustomProgressBar(getActivity());
                customProgressBar.setMessage("相机取景中...请保持...").show();
                camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
                    @Override
                    public void captureSuccess(Bitmap bitmap) {
                        roundBitmap = UIUtils.getRoundBitmap(bitmap);
////                        Bitmap ovalBitmap = UIUtils.getOvalBitmap(bitmap);
//                        result = roundBitmap;
                        ivCameraShow.setVisibility(View.VISIBLE);
                        ivCameraShow.setImageBitmap(roundBitmap);

                        customProgressBar.dismiss();
                    }

                    @Override
                    public void captureFailed() {
                        if (customProgressBar != null) {
                            customProgressBar.dismiss();
                        }
                    }
                });
                break;*/
        }
    }
    /**
     * 拍摄头像dialog
     */
    private void initCameraDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog_login);
        View inflate = UIUtils.inflate(R.layout.layout_camera_usercenter);
        dialog.setContentView(inflate);
        cameraView = (TextureView) inflate.findViewById(R.id.camera_view);
        dialog.setCanceledOnTouchOutside(false);
        iv_camera_show = (ImageView) inflate.findViewById(R.id.iv_camera_show);
        btn_camera = (Button) inflate.findViewById(R.id.btn_camera);
        btn_save = (Button) inflate.findViewById(R.id.btn_save);
        still_button = (Button) inflate.findViewById(R.id.still_button);
        btn_close_camera = (Button) inflate.findViewById(R.id.btn_close_camera);
        rlCapture = (RelativeLayout) inflate.findViewById(R.id.rl_capture);
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1000;
        params.height = 740;
        dialog.getWindow().setAttributes(params);
        camera2Manager = new Camera2Manager(cameraView, 1, getActivity());
        //拍照
        still_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlCapture.setVisibility(View.GONE);
                takePicture();
            }
        });
        //重新拍照
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlCapture.setVisibility(View.VISIBLE);
                iv_camera_show.setVisibility(View.GONE);
            }
        });
        //保存照片
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result == null) {
                    return;
                }
                dialog.dismiss();
                saveUserHeadImg();
            }
        });
        //关闭dialog camera
        btn_close_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                camera2Manager.stopCamera();
                iv_camera_show.setImageBitmap(null);
                iv_camera_show.setVisibility(View.GONE);
            }
        });

    }
    /**
     * 拍照
     */
    private void takePicture() {
        customProgressBar = new CustomProgressBar(getActivity());
        customProgressBar.setMessage("相机取景中...请保持...").show();
        camera2Manager.takePicture(new Camera2Manager.OnCaptureCompletedListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                Bitmap roundBitmap = UIUtils.getRoundBitmap(bitmap);
                result = roundBitmap;
                customProgressBar.dismiss();
                iv_camera_show.setVisibility(View.VISIBLE);
                iv_camera_show.setImageBitmap(roundBitmap);
            }

            @Override
            public void captureFailed() {
                if (customProgressBar != null) {
                    customProgressBar.dismiss();
                }
            }
        });
    }

    /**
     * 保存用户头像
     */
    private void saveUserHeadImg() {

        //保存照片
        rlCapture.setVisibility(View.VISIBLE);
        ivUserHeader.setImageBitmap(result);
        //保存照片
        pathname = UIUtils.getInnerSDCardPath() + "/user_header.png";
        BitmapUtil.compressBmpToFile(result, new File(pathname));
    }

    private boolean checkInputState(String psd, String concertPsd, String nick_name, String user_age, int sexState) {
        if (psd.length() <= 5) {
            UIUtils.showToast(getActivity(), "密码长度至少6位!");
            return false;
        }
        if (concertPsd.length() <= 5) {
            UIUtils.showToast(getActivity(), "确认密码长度至少6位!");
            return false;
        }
        if (TextUtils.isEmpty(psd) || !psd.matches("^[a-zA-Z0-9]{6,12}$")) {
            UIUtils.showToast(getActivity(), "未输入密码或非法字符!");
            return false;
        }
        if (TextUtils.isEmpty(concertPsd) || !psd.matches("^[a-zA-Z0-9]{6,12}$")) {
            UIUtils.showToast(getActivity(), "未输入确认密码或非法字符!");
            return false;
        }
        if (!concertPsd.equals(psd)) {
            UIUtils.showToast(getActivity(), "两次输入密码不一致!");
            return false;
        }
        if (nick_name.length() < 2) {
            UIUtils.showToast(getActivity(), "昵称最少两位!");
            return false;
        }
        if (TextUtils.isEmpty(user_age)) {
            UIUtils.showToast(getActivity(), "未填写年龄!");
            return false;
        }
        if (sexState == -1) {
            UIUtils.showToast(getActivity(), "请选择性别!");
            return false;
        }
        return true;
    }

    @Override
    public void onSubmitSuccess() {
        UIUtils.showToast(getActivity(), "注册成功");
        llPerfectInfo.setVisibility(View.GONE);
        rlRegeditAutoLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSubmitFailed() {
        UIUtils.showToast(getActivity(), "注册失败!");


    }

    @Override
    public void onLoadImgSuccess() {
        UIUtils.showToast(getActivity(), "上传头像成功!");
    }

    @Override
    public void onLoadImgFailed() {
        UIUtils.showToast(getActivity(), "上传头像失败!");
    }
}
