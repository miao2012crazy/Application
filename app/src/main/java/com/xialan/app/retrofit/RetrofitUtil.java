package com.xialan.app.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.StaticLayout;
import android.util.Log;

import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.ApiService;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.HttpUtil;
import com.xialan.app.utils.UIUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/9/1.
 */

public class RetrofitUtil {

    private static final String TAG = "RetrofitUtil";
    private static Retrofit retrofit;
    private static ApiService apiService;

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }

    public static OkHttpClient genericClient() {
        //缓存路径
        File cacheFile = new File(UIUtils.getContext().getCacheDir().getAbsolutePath(), "HttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);//缓存文件为10MB

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        int maxAge = 60 * 5; // 有网络时 设置缓存超时时间5分钟
                        int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                        Request request = chain.request();
                        if (isNetworkReachable(UIUtils.getContext())) {
                            request = request.newBuilder()
//                                    .addHeader("apikey", "2ffc3e48c7086e0e1faa003d781c0e69")
                                    .cacheControl(CacheControl.FORCE_NETWORK)//有网络时只从网络获取
                                    .build();
                        } else {
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                                    .build();
                        }
                        Response response = chain.proceed(request);
                        if (isNetworkReachable(UIUtils.getContext())) {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, max-age=" + maxAge)
                                    .build();
                        } else {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return response;
                    }

                })
                .connectTimeout(15, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        return httpClient;
    }

    /**
     * 单例 获取retrofit 对象 封装Apiservice 直接返回apiservice
     *
     * @return
     */
    public static ApiService getApiservice() {

        if (apiService != null) {
            return apiService;
        } else {
            retrofit = new Retrofit.Builder()
                    .client(genericClient())
                    .baseUrl(HttpUrl.baseUrl())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
            return apiService;
        }

    }

    /**
     * 请求开始 回调
     * 请求成功回调
     * 请求失败回调
     */
    public static abstract class HttpCallBack {
        //开始
        public abstract void onstart();
        //成功回调
        public abstract void onSusscess(String data);
        //失败
        public abstract void onError(String meg);

        //请求完成
        public abstract void onCompleted();
    }

    /**
     *
     * @param observe  RetrofitUtil.getApiservice().+方法()
     * @param httpCallBack
     */
    public static void setCallBack(Observable<ResponseBody> observe, final HttpCallBack httpCallBack) {
        httpCallBack.onstart();
        observe.subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        httpCallBack.onCompleted();
                    }
                    @Override
                    public void onError(java.lang.Throwable e) {
                        httpCallBack.onError(e.getMessage());
                    }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            httpCallBack.onSusscess(responseBody.string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
