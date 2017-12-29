package com.xialan.app.contract;

/**
 * Created by Administrator on 2017/10/12.
 */

public interface NoticeContract {
    interface Model {
        void getNoticeFromNet();
    }

    interface View {
        void onGetNoticeSuccessed(String[] notice_data);
        void onGetNoticeFailed();
    }

    interface Presenter {
        void getNoticeSuccessed(String[] notice_data);
        void getNoticeFailed();
        void getNotice();
    }
}
