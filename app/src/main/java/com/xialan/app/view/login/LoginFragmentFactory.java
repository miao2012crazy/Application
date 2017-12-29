package com.xialan.app.view.login;

import com.xialan.app.base.BaseFragment;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/18.
 */
public class LoginFragmentFactory {
    private static Map<Integer, BaseFragment> mFragments = new HashMap<Integer, BaseFragment>();
    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        fragment = mFragments.get(position);  //在集合中取出来Fragment
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new LoginFragment();
                    break;
                case 1:
                    fragment = new RegeditFragment();
                    break;
                case 2:
                    fragment = new PerfectInfoFragment();
                    break;
                case 3:
                    fragment = new WeChatLoginFragment();
                    break;
            }

        }
        return fragment;
    }
}
