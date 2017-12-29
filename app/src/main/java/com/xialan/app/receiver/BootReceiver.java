package com.xialan.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xialan.app.MainActivity;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.SplashActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/7/19.
 */
public class BootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {     // boot
                Intent intent2 = new Intent(context, SplashActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            }
        }
}
