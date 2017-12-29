package com.xialan.app.utils;

import com.xialan.app.application.XLApplication;

/**
 * Created by Administrator on 2017/10/29.
 */

public class CheckLoginUtil {
    /**
     * 登录判定
     *
     * @param data
     * @param mobile_uid
     */
    public static void parseLogin(String[] data, String mobile_uid) {
        switch (data[0]) {
            case "0":
                UIUtils.showToast(UIUtils.getContext(), "不存在的用户ID!");
                break;
            case "1":
                UIUtils.showToast(UIUtils.getContext(), "用户密码错误!");
                break;
            case "2":
                UIUtils.showToast(UIUtils.getContext(), "登录成功!");
                String[] loginstr={mobile_uid, data[2],data[3],data[1],data[4]};
                XLApplication.setUserLoginData(loginstr);
                RxBus.get().post("finishActivity", "");
                RxBus.get().post("is_login", "1");
                break;
            default:
                UIUtils.showToast(UIUtils.getContext(), "未知错误,请联系管理员!");
                break;
        }
    }

}
