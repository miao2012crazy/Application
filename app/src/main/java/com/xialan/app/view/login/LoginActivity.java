package com.xialan.app.view.login;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.xialan.app.R;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.utils.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/21.
 */
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.ll_container_2)
    LinearLayout llContainer_2;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.btn_return)
    Button btn_return;
    private Observable<String> finishActivity;
    private Observable<String> replace;
    private RadioGroup radioGroup;
    private Observable<String> replace_to_fragment;
    private Unbinder bind;


    protected void initData() {
        initRgGroup();
        initFinishListener();
        replace = RxBus.get().register("replace", String.class);
        replace.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                llContainer.setVisibility(View.GONE);
                FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
                BaseFragment fragment = LoginFragmentFactory.getFragment(2);
                beginTransaction.replace(R.id.ll_container_2, fragment);
                beginTransaction.commit();
            }
        });


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化关闭页面监听
     */
    private void initFinishListener() {
        finishActivity = RxBus.get().register("finishActivity", String.class);
        finishActivity.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                finish();
            }
        });
    }

    private void initRgGroup() {
        radioGroup = (RadioGroup) findViewById(R.id.rg_login);
        replaceFragment(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    //登录
                    case R.id.rb_login:
                        replaceFragment(0);
                        break;
                    //注册
                    case R.id.rb_regist:
                        replaceFragment(1);
                        break;
                    //wechat登录
                    case R.id.rb_weichat:
                        replaceFragment(3);
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind = ButterKnife.bind(this);
        initData();
    }

    /**
     * 切换fragment
     *
     * @param position
     */
    private void replaceFragment(int position) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        BaseFragment fragment = LoginFragmentFactory.getFragment(position);
        beginTransaction.replace(R.id.rl_container, fragment);
        beginTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
        RxBus.get().unregister("replace",replace);
        RxBus.get().unregister("replace_to_fragment",replace_to_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        replace_to_fragment = RxBus.get().register("replace_to_fragment", String.class);
        replace_to_fragment.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                switch (s){
                    case "0":
                        //登录
                        radioGroup.check(R.id.rb_login);
                        break;
                    case "1":
                        //注册
                        radioGroup.check(R.id.rb_regist);
                        break;
                    case "3":
                        //wechat登录
                        radioGroup.check(R.id.rb_weichat);
                        break;
                }
                replaceFragment(1);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        XLApplication.user_open_id="";
    }
}
