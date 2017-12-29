package com.xialan.app.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xialan.app.R;
import com.xialan.app.utils.UIUtils;


/**
 * Created by ${苗春良}
 * on 2016/11/23.
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends Activity {
    protected T mPresenter;
    /**
     * 两次点击的间隔时间
     */
    private static final int QUIT_INTERVAL = 2000;
    /**
     * 上次点击返回键的时间
     */
    private long lastBackPressed;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mPresenter = createPresenter();
        //presenter层和view层绑定
        mPresenter.attachView((V) this);
        initVariables();
        initViews(savedInstanceState);
        initData();

        //透明状态栏 向下兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//        //开启友盟测试
//        MobclickAgent.setDebugMode(false);
    }


    /**
     * 加载数据操作
     */
    protected abstract void initData();

    /**
     * @param savedInstanceState 初始化布局
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化变量 接收从其他界面传递过来的参数
     */
    protected abstract void initVariables();


    /**
     * @return 创建presenter
     */
    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            //解除绑定
            mPresenter.detachView();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            long backPressed = System.currentTimeMillis();
//            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
//                lastBackPressed = backPressed;
//                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//            } else {
//                finish();
////                MobclickAgent.onKillProcess(this);
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onBackPressed() {
//        long backPressed = System.currentTimeMillis();
//        if (backPressed - lastBackPressed > QUIT_INTERVAL) {
//            lastBackPressed = backPressed;
//            Toast.makeText(this, "再按一次退出", Toast.LENGTH_LONG).show();
//        } else {
//            finish();
////            MobclickAgent.onKillProcess(this);
//            System.exit(0);
//        }
//    }

    /**
     * 关闭当前界面 启动新界面
     *
     * @param clazz 启动界面方法  带有动画效果
     */
    protected void startActivity(Class clazz) {
        Intent intent = new Intent(UIUtils.getContext(), clazz);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
    }

    public void onResume() {
        super.onResume();
        mPresenter.attachView((V) this);

    }

    public void onPause() {
        super.onPause();

    }


}

