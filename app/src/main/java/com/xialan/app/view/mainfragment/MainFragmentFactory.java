package com.xialan.app.view.mainfragment;

import com.xialan.app.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

/**  主工厂类
 * Created by Administrator on 2017/7/18.
 */
public class MainFragmentFactory {
    private static Map<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();
    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        fragment = mFragments.get(position);  //在集合中取出来Fragment
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new ProductFragment2();
                    break;
                case 1:
                    fragment = new HeadStyleContrastFragment();
                    break;
                case 2:
                    fragment = new SkupFragment();
                    break;
                case 3:
                    fragment = new SkinFragment();
                    break;
                case 5:
                    fragment = new TrainingVideoFragment();
                    break;
                case 6:
                    fragment = new UserCenterFragment();
                    break;
            }
            mFragments.put(position,fragment);
        }
        return fragment;
    }
}
