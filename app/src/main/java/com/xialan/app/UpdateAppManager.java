package com.xialan.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.xialan.app.application.XLApplication;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.UIUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/8/28.
 */
public class UpdateAppManager {
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/" + "AutoUpdate" + "/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "AutoUpdate.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //Log日志打印标签
    private static final String TAG = "Update_log";
    private Context context;
    //获取版本数据的地址
    private String version_path = HttpUrl.setGetUrl("IBSync/search_appversion.aspx");
    //获取新版APK的默认地址
    public static String apk_path = HttpUrl.setGetUrl("IBSync/app_release/");
    // 下载应用的进度条
    private ProgressDialog progressDialog;

    //新版本号和描述语言
    private int update_versionCode;


    public UpdateAppManager(Context context) {
        this.context = context;
    }

    /**
     * 获取当前版本号
     */
    private int getCurrentVersion() {
        try {

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);


            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }
    }

    /**
     * 从服务器获得更新信息
     */
    public void getUpdateMsg() {
        class mAsyncTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url_version = new URL(params[0]);
                    connection = (HttpURLConnection) url_version.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));


                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                try { //回到主线程，更新UI
                    String[] dataForspild = CommonUtil.getDataForspild(s);
                    update_versionCode = Integer.parseInt(dataForspild[0]);

                    apk_path = HttpUrl.setGetUrl("IBSync/app_release/" + dataForspild[1]);
                    if (update_versionCode > getCurrentVersion()) {

                        showNoticeDialog();
                    } else {
                    }
                } catch (Exception e) {
                    update_versionCode=getCurrentVersion();
                }


            }
        }
        new mAsyncTask().execute(version_path);
    }


    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("检测到新版本！")
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                })
                .create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载新版本...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        new downloadAsyncTask().execute();
    }

    /**
     * 下载新版本应用
     */
    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {


            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(apk_path);
                connection = (HttpURLConnection) url.openConnection();

                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }

                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;


                while ((len = in.read(buffer)) != -1) {

                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;

                    int curProgress = (int) (((float) readLength / fileLength) * 100);


                    publishProgress(curProgress);

                    if (readLength >= fileLength) {

                        break;
                    }
                }

                out.flush();
                return INSTALL_TOKEN;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.dismiss();//关闭进度条
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File appFile = new File(FILE_NAME);
        try{
            if (!appFile.exists()) {
                return;
            }
            try{
                Intent data = new Intent();
                data.setClassName("com.xialan.appinstall", "com.xialan.appinstall.MainActivity");
                data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(data);
            }catch (Exception e){
                UIUtils.showToast(UIUtils.getContext(),"需要手动安装");
            }
//        silentInstall(FILE_NAME);
            // 跳转到新版本应用安装页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
            context.startActivity(intent);
            // 会返回结果,回调方法onActivityResult
//        android.os.Process.killProcess(android.os.Process.myPid());
        }catch (Exception e){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
            context.startActivity(intent);
            e.printStackTrace();
        }
    }
}
