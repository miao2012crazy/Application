package com.xialan.app.model;

import com.xialan.app.contract.NoticeContract;
import com.xialan.app.presenter.NoticePresenter;
import com.xialan.app.retrofit.RetrofitUtil;
import com.xialan.app.utils.CommonUtil;

/**
 * Created by Administrator on 2017/10/12.
 */

public class NoticeModel implements NoticeContract.Model {
    private NoticePresenter mNoticePresenter;

    public NoticeModel(NoticePresenter noticePresenter) {
        this.mNoticePresenter=noticePresenter;
    }

    @Override
    public void getNoticeFromNet() {
        RetrofitUtil.setCallBack(RetrofitUtil.getApiservice().getNotice(), new RetrofitUtil.HttpCallBack() {
            @Override
            public void onstart() {

            }

            @Override
            public void onSusscess(String data) {
                String[] notice_data = CommonUtil.getBAdata(data);
                mNoticePresenter.getNoticeSuccessed(notice_data);
            }

            @Override
            public void onError(String meg) {
                mNoticePresenter.getNoticeFailed();
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
