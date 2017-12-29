package com.xialan.app.base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xialan.app.ui.CustomProgressBar;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment 基类
 *
 * Created by ${苗春良}
 * on 2016/11/23.
 */
public abstract class BaseFragment<V,P extends BasePresenter<V>> extends Fragment {

    protected P mPresenter;
    private CustomProgressBar customProgressBar;
    private Unbinder bind;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=createPresenter();//创建presenter
        mPresenter.attachView((V) this);
        initVariables();
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentId(), container, false);
        bind = ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        bind.unbind();
    }
    /**
     * 接收其他页面传递过来的数据 子类可直接重写
     */
    protected void initVariables() {

    }

    /**
     * 提供布局id
     * @return
     */
    protected abstract int getContentId();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    /**
     * 中间p层对象
     * @return
     */
    protected abstract P createPresenter();
    protected void startLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void showProgress(){
        customProgressBar = UIUtils.getCustomProgressBar(getActivity());
        customProgressBar.show();
    }
    public void hideProgress(){
        if (customProgressBar!=null){
            customProgressBar.dismiss();
        }
    }


}