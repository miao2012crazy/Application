package com.xialan.app.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xialan.app.MainActivity;
import com.xialan.app.R;
import com.xialan.app.utils.NetState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/9/28.
 */

public class SplashActivity extends Activity {

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
       checkNetState();

    }

    private void checkNetState() {
        int i = NetState.checkState(SplashActivity.this);
        if (i==0){
            builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("网络设置检测");
            builder.setMessage("如果您已经连接了有线网络,可直接点击"+"不设置网络");
            builder.setNegativeButton("不设置网络", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent1=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent1);
                }
            });
            builder.setPositiveButton("已完成网络设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    checkNetState();
                }
            });
            builder.show();
            return;
        }
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent1=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        };
        timer.schedule(timerTask,1000*3);
    }

}
