package com.xialan.app.presenter;


import com.xialan.app.base.BasePresenter;
import com.xialan.app.contract.NoticeContract;
import com.xialan.app.model.NoticeModel;

/**
 * Created by Administrator on 2017/10/12.
 */

public class NoticePresenter extends BasePresenter implements NoticeContract.Presenter {
    private NoticeContract.View mView;
    private final NoticeModel noticeModel;

    public NoticePresenter(NoticeContract.View view) {
        this.mView=view;
        noticeModel = new NoticeModel(this);
    }

    @Override
    public void getNoticeSuccessed(String[] notice_data) {
        mView.onGetNoticeSuccessed(notice_data);
    }

    @Override
    public void getNoticeFailed() {
        mView.onGetNoticeFailed();
    }

    @Override
    public void getNotice() {
        noticeModel.getNoticeFromNet();
    }

}
