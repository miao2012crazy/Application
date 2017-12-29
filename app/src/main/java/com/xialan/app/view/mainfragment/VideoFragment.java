package com.xialan.app.view.mainfragment;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.xialan.app.R;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.VideoContract;
import com.xialan.app.presenter.VideoPresenter;
import com.xialan.app.utils.FileUtils;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.utils.VideoUtils;

import java.io.File;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;

import static com.xialan.app.application.XLApplication.getProxy;

/**
 * Created by Administrator on 2017/10/12.
 */
public class VideoFragment extends BaseFragment implements VideoContract.View, MediaPlayer.OnCompletionListener,View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    @BindView(R.id.videoview)
    VideoView videoview;
    @BindView(R.id.iv_product_tag)
    ImageView ivProductTag;
    @BindView(R.id.rl_vv)
    RelativeLayout rl_vv;
    @BindView(R.id.ll_progress_bar)
    LinearLayout progressBar;
    private VideoPresenter videoPresenter;
    private String[] video_data;
    private int mPosition;
    private GestureDetector mGestureDetector;
    private HttpProxyCacheServer proxy;
    //广告视频暂停时位置
    private int currentPosition=0;
    //广告视频
    private String current_video_path="";

    //产品视频是否正在播放 默认false
    private Boolean isProductVideoPlaying=false;
    private MediaController controller;
    private String mProductVideoPath="";

    @Override
    protected int getContentId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void loadData() {
        proxy = getProxy();
        String innerSDCardPath = UIUtils.getInnerSDCardPath();
        String strFile = innerSDCardPath + "/video/";
        boolean b = UIUtils.fileIsExists(strFile);
        File file = new File(innerSDCardPath + "/video/");
        File[] files = file.listFiles();
        try {
        for (int i=0;i<files.length;i++){
            String fileSuffix = FileUtils.getFileSuffix(files[i].getName());
            if (fileSuffix.equals("download")){
                boolean b1 = FileUtils.deleteFile(files[i]);
                Log.e("miao111",b1+"");
            }
            Log.e("miao111",files[i].getName());
        }


            long fileSizes = FileUtils.getFileSizes(file);
            String s = FileUtils.FormetFileSize(fileSizes);
            Log.e("miao111", s);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!b) {
            File file1 = new File(innerSDCardPath + "/video/");
            if (!file.exists()) {
                boolean mkdir = file1.mkdir();
            }
        }
        controller = new MediaController(getActivity());
        videoview.setMediaController(controller);
        videoview.setOnCompletionListener(this);
        videoview.setOnPreparedListener(this);
        videoview.setOnErrorListener(this);

        progressBar.setVisibility(View.VISIBLE);
        videoPresenter.getVideodata();
        initListener();
        initGestureDetector();
    }

    /**
     * 初始化监听  播放产品视频
     */
    private void initListener() {
        //中断当前视频 , 保存位置 播放其他视频
        Observable<String> vv_soruce = RxBus.get().register("vv_soruce", String.class);
        vv_soruce.subscribe(new Action1<String>() {
            @Override
            public void call(String videopath) {
                mProductVideoPath=videopath;
                //中断广告视频
                currentPosition=videoview.getCurrentPosition();
                HttpProxyCacheServer proxy = getProxy();
                String proxyUrl = proxy.getProxyUrl(videopath);
                //播放其他视频
                playVideoForPath(proxyUrl, 0);
            }
        });
        //重新播放已暂停的广告视频
        Observable<String> vv_reset_soruce = RxBus.get().register("vv_reset_soruce", String.class);
        vv_reset_soruce.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mProductVideoPath="";
                //判断产品视频视频是否正在播放
                if (isProductVideoPlaying){
                    ivProductTag.setVisibility(View.INVISIBLE);
                    isProductVideoPlaying=false;
                    playVideoForPath(current_video_path, currentPosition);//继续播放广告视频
                }
                if (XLApplication.getIsVideoState()){
                    playVideoForPath(current_video_path, currentPosition);//继续播放广告视频
                }
            }
        });
        //视频播放标签
        RxBus.get().register("update_product_tag", boolean.class).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                isProductVideoPlaying =aBoolean;
                if (aBoolean) {
                    //产品信息标签显示
                    ivProductTag.setVisibility(View.VISIBLE);
                } else {
                    //产品信息标签不显示
                    ivProductTag.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        videoPresenter = new VideoPresenter(this);
        return videoPresenter;
    }


    @Override
    public void OnVideoDataSuccessed(String[] video_data) {
        this.video_data = video_data;
        startVideo(0);
    }


    @Override
    public void OnVideoDataFailed() {

    }

    @Override
    public void startVideo(int position) {
        this.mPosition = position;
        String proxyUrl = proxy.getProxyUrl(HttpUrl.setGetUrl("IBSync/videos/ad/" + video_data[position]));
        String strFile = UIUtils.getInnerSDCardPath() + "/video/" + video_data[position];
        current_video_path = proxyUrl;
        //判断本地是否有当前文件  如果有 直接播放本地视频
        boolean b = UIUtils.fileIsExists(strFile);
        if (b){
            String path = UIUtils.getInnerSDCardPath() + "/video/" + video_data[position];
            videoview.setVideoPath(path);
            videoview.requestFocus();
            videoview.start();
            return;
        }
        //本地没有视频  启动缓存服务器 边下边播
        playVideoForPath(proxyUrl,0);
    }

    /**
     * 根据视频的路径播放视频
     *
     * @param proxyUrl      视频url
     * @param seek_position 播放位置
     */
    private void playVideoForPath(String proxyUrl,int seek_position) {
        videoview.setVideoPath(proxyUrl);
        if (seek_position!=0){
            videoview.seekTo(seek_position);
        }
        videoview.requestFocus();
        videoview.start();
    }

    @Override
    public void pauseVideo() {

    }

    @Override
    public void playVideoCompleted() {

    }

    @Override
    public void playVideoError() {

    }


    /**
     * 视频播放完成
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //教育视频
        boolean isVideoState = XLApplication.getIsVideoState();
        if (isVideoState){
            //教育视频播放完成
            XLApplication.IS_VIDEO=false;
            XLApplication.trainVideoPosition=-1;
            RxBus.get().post("VIDEO_COMPLETE","");
//            重新播放广告视频
            playVideoForPath(current_video_path, currentPosition);//继续播放广告视频
            return;
        }

        //产品视频播放完成
        if (isProductVideoPlaying) {
            playVideoForPath(mProductVideoPath, 0);
            //视频资源
//            RxBus.get().post("vv_reset_soruce","");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        if (mPosition == video_data.length - 1) {
            mPosition = 0;
        } else {
            mPosition++;
        }
        startVideo(mPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        //恢复播放广告视频
        if (current_video_path.equals("")){
            return;
        }
        playVideoForPath(current_video_path,currentPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        //失去焦点时保存当前位置 保存广告视频位置
        currentPosition = videoview.getCurrentPosition();
    }

    /**
     * 初始化视频播放器手势监听
     */
    private void initGestureDetector() {
        //设置显示控制条
        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float FLING_MIN_BRIGHTNESS = 1.0f * VideoUtils.dp2px(getActivity(), 1f);//调节亮度时的敏感度，
                float FLING_MIN_VOICE = 1.0f * VideoUtils.dp2px(getActivity(), 5f);//调节声音时的敏感度
                float distance = Math.abs(distanceY);//竖直方向移动范围
                //在屏幕左侧滑动调节亮度，在屏幕右侧滑动调节声音
                if (e1.getX() < VideoUtils.getScreenWidth(getActivity()) / 2) {//左侧
                    if (distance > FLING_MIN_BRIGHTNESS) {//移动范围满足
                        if (e1.getY() - e2.getY() > 0) {//上滑
                            VideoUtils.setScreenBrightness(getActivity(), 15);//亮度增加，第二参数的大小代表着敏感度
                        } else {//下滑
                            VideoUtils.setScreenBrightness(getActivity(), -15);
                        }
                    }
                } else {//右侧
                    if (distance > FLING_MIN_VOICE) {//移动范围满足
                        if (e1.getY() - e2.getY() > 0) {//上滑
                            //VideoUtils.setVoiceVolume(this, 1);//音量增加
                            VideoUtils.setVoiceVolumeAndShowNotification(getActivity(), true);//系统自动控制音量增加减小级别
                        } else {//下滑
                            //    VideoUtils.setVoiceVolume(this, -1);
                            VideoUtils.setVoiceVolumeAndShowNotification(getActivity(), false);
                        }
                    }
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return true;
            }
        });
        videoview.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);//把Touch事件传递给手势识别器，这一步非常重要！
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        UIUtils.showToast(UIUtils.getContext(),"当前视频无法播放");
        if (mPosition == video_data.length - 1) {
            mPosition = 0;
        } else {
            mPosition++;
        }
        startVideo(mPosition);
        return true;
    }





}
