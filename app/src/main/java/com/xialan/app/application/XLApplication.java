package com.xialan.app.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xialan.app.MainActivity;
import com.xialan.app.UpdateAppManager;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.ImageLoaderManager;
import com.xialan.app.utils.ParseDataUtil;
import com.xialan.app.utils.UIUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/7/17.
 */
public class XLApplication extends Application implements Thread.UncaughtExceptionHandler {
    public static  boolean IS_VIDEO = false;
    private static Context context;
    private static Handler handler;
    private static Thread mainThread;
    private static int mainThreadId;
    private static XLApplication app;
    //wechat 首次登录 完成信息
    public static String user_nick_name = "";
    public static String user_sex = "";
    public static String user_open_id = "";
    //登录成功 初始化用户信息
    public static String uid = "";
    public static String uid_sex = "";
    public static String uid_head_url = "";
    public static String uid_nick_name_2 = "";
    public static String uid_verify_code = "";
    public static String wechat_id = "";
    public static String uid_psd = "";
    public static boolean isWechating;
    public static int trainVideoPosition=-1;
    public static String app_version_android ="";
    public static String app_version_ios="";
    //视频缓存
    private HttpProxyCacheServer proxy;

    //将此初始化的代码放在application的oncreate方法中,或者应用程序第一个activity的oncreate方法中
    @Override
    public void onCreate() {
        super.onCreate();
        //当程序发生Uncaught异常的时候,由该类来接管程序,一定要在这里初始化
//		CrashHandler.getApiservice().init(this);
        app = this;
        //在此方法中需要获取handler对象,上下文环境
        context = getApplicationContext();
        //准备handler对象
        handler = new Handler();
        //hanlder应用场景(子线程往主线程中发送message)

        //获取主线程的对象,WLBApplication的onCreate运行在主线程中的代码
        mainThread = Thread.currentThread();
        //获取主线程(当前线程)id方法
        mainThreadId = android.os.Process.myTid();
        mMainThreadHandler = new Handler();
        //让ImageLoader进行初始化
        ImageLoaderManager.initImageLoader(getApplicationContext());
        //设置Thread Exception Handler
        //Thread.setDefaultUncaughtExceptionHandler(this);
    }


    public static HttpProxyCacheServer getProxy() {
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        HttpProxyCacheServer httpProxyCacheServer = new HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(100)
                .cacheDirectory(new File(UIUtils.getInnerSDCardPath() + "/video"))
                .fileNameGenerator(new CustomFileNameGenerator())
                .build();
        return httpProxyCacheServer;
    }

    public static void setWechatUserInfo(String[] str) {
        user_nick_name = str[0];
        user_sex = str[1];
        user_open_id = str[3];
    }

    /**
     * 用户登录成功
     *
     * @param str_user_info
     */
    public static void setUserLoginData(String[] str_user_info) {
        uid = str_user_info[0];
        uid_sex = str_user_info[1];
        uid_head_url = str_user_info[2];
        uid_nick_name_2 = str_user_info[3];
        wechat_id = str_user_info[4];
    }

    /**
     * 退出登录
     */
    public static void returnUserLoginData() {
        uid = "";
        uid_sex = "";
        uid_head_url = "";
        uid_nick_name_2 = "";
        uid_verify_code = "";
        uid_psd = "";
        wechat_id = "";
        isWechating = false;
    }


    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Thread getMainThread() {
        return mainThread;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    private static Handler mMainThreadHandler = null;

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Context getInstance() {
        return app;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private class CustomFileNameGenerator implements FileNameGenerator {
        @Override
        public String generate(String url) {
            Uri uri = Uri.parse(url);
            String[] dataForspild_2 = CommonUtil.getDataForspild_2(uri.toString());
            if (dataForspild_2 != null) {
                String s = dataForspild_2[dataForspild_2.length - 1];
                return s;
            }
            return url;
        }
    }

    public  static boolean getIsVideoState(){
        return IS_VIDEO;

    }
    public static int getPlayingVideoPosition(){
        return trainVideoPosition;
    }
}
