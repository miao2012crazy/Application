package com.xialan.app.utils;




import android.content.Context;
import android.util.Log;

import com.xialan.app.MainActivity;
import com.xialan.app.ui.CustomProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 网络请求工具类
 * post请求  json数据为body
 * post请求  map是body
 * get 请求
 * Created by root on 2016/11/11.
 *
 * @author mcl
 */
public class HttpUtil {
    private static final OkHttpClient client;
    private static final long cacheSize = 1024*1024*200;//缓存文件最大限制大小200M
    private static String cachedirectory = "data" + "/caches";  //设置缓存文件路径
    private static Cache cache = new Cache(new File(cachedirectory), cacheSize);  //
    private Context mContext;//activity对象自动加载进度条
    //超时时间
    public static final int TIMEOUT = 1000 * 60;
    //json请求
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);  //设置连接超时时间
        builder.writeTimeout(8, TimeUnit.SECONDS);//设置写入超时时间
        builder.readTimeout(8, TimeUnit.SECONDS);//设置读取数据超时时间
        builder.retryOnConnectionFailure(false);//设置不进行连接失败重试
//        builder.cache(cache);//这种缓存
        client = builder.build();
    }


    /**
     * post请求  json数据为body
     */
    public void postJson(String url, String json, final HttpCallBack callBack) {
        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder().url(url).post(body).build();
        OnStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }

    /**
     * post请求  map是body
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void postMap(String url, Map<String, String> map, final HttpCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        //遍历map
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        OnStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }

        });
    }

    /**
     * get 请求
     *
     * @param url
     * @param callBack
     */
    public void getJson(String url, final HttpCallBack callBack) {
//        Request request = new Request.Builder().cacheControl(new CacheControl.Builder().maxAge(60, TimeUnit.SECONDS).build()).url(url).build();
        Request request = new Request.Builder().url(url).build();
        OnStart(callBack);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                OnError(callBack, e.getMessage());
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }
    /**
     * 上传文件
     *
     * @param url
     */
    public void postFile(String url, Map<String,Object> map, File file , final HttpCallBack callBack){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(file != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
        }
        OnStart(callBack);
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //onsuccess(final HttpCallBack callBack, final String data);
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     */
    public void postFile_2(String url, Map<String,Object> map, File front_file ,File bg_file, final HttpCallBack callBack){
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(front_file != null&bg_file!=null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body1 = RequestBody.create(MediaType.parse("image/*"), front_file);
            RequestBody body2 = RequestBody.create(MediaType.parse("image/*"), bg_file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file1", front_file.getName(), body1);
            requestBody.addFormDataPart("file2", bg_file.getName(), body2);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
        }
        OnStart(callBack);
        Request request = new Request.Builder().url(url).post(requestBody.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //onsuccess(final HttpCallBack callBack, final String data);
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }


    /**
     * 根据文件路径判断MediaType
     *
     * @param path
     * @return
     */
    private static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }





    public void OnStart(HttpCallBack callBack) {
        if (callBack != null) {
            callBack.onstart();
        }
    }

    public void onSuccess(final HttpCallBack callBack, final String data) {
        if (callBack != null) {
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onSusscess(data);
                }
            });

        }
    }

    public void OnError(final HttpCallBack callBack, final String msg) {
        if (callBack != null) {
            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(msg);
                }
            });
        }
    }

    /**
     * 请求开始 回调
     * 请求成功回调
     * 请求失败回调
     */
    public static abstract class HttpCallBack {
        //开始
        public void onstart() {

        }
        //成功回调
        public abstract void onSusscess(String data);

        //失败
        public void onError(String meg) {

        }
    }
    /**
     * 下载文件
     * @param url
     * @param fileDir
     * @param fileName
     */
    public static void downFile(String url, final String fileDir, final String fileName, final HttpCallBack httpCallBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call =client.newCall(request);
        httpCallBack.onstart();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onError("下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(fileDir, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    httpCallBack.onSusscess("下载成功");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }

}
