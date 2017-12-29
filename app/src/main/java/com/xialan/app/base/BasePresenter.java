package com.xialan.app.base;

import android.app.Activity;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * <p/>
 * 明确泛型 V 是一个View角色要实现的接口类型
 * (所有的View接口都要继承IBaseView接口可以把所有接口都要拥有的方法放到IBaseView)
 *
 * @param <T>代表view接口
 *           Created by ${苗春良}
 *                    on 2016/11/23.
 */
public abstract class BasePresenter<T> {

    /**
     * post请求参数集合  使用前请清空
     */
    protected Map<String,String> map=new HashMap<>();
    //View接口类型的弱引用
    protected Reference<T> mViewRef;
    /**
     * @param view  presenter 与view进行绑定
     */
    public void attachView(T view){
        mViewRef=new WeakReference<T>(view);
    }

    protected T getView(){
        return mViewRef.get();
    }

    /**
     * @return 是否关联
     */
    public boolean isViewAttached(){

        return mViewRef!=null&&mViewRef.get()!=null;
    }


    /**
     * 解除关联
     */
    public void detachView(){

        if (mViewRef!=null){

            mViewRef.clear();
            mViewRef=null;
        }
    }

}