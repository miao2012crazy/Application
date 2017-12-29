package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.bean.HairStyleBABean;
import com.xialan.app.contract.HeadStyleContract;
import com.xialan.app.contract.HeadStyleContrastContract;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.retrofit.RetrofitUtil;
import com.xialan.app.utils.CommonUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.HttpUtil;
import com.xialan.app.utils.UIUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/1.
 */
public class HeadStyleContrastModel implements HeadStyleContrastContract.Model {
    private final HeadStyleContrastContract.Presenter mPresenter;

    public HeadStyleContrastModel(HeadStyleContrastContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void getDataForUserImageFromNet(String userId) {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getheadStyleHistory(userId), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                mPresenter.getDataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {

                    String string = responseBody.string();
                    if (string.equals("")){
                        UIUtils.showToast(UIUtils.getContext(),"暂无数据!");
                        mPresenter.getDataWithNull();
                        return;
                    }
                    String[] bAdata = CommonUtil.getBAdata(string);
                    List<HairStyleBABean> hairStyleBABeen = initRecyclerData(bAdata);
                    mPresenter.getDataSuccess(hairStyleBABeen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void upLoadUserPic(String user_id, String mac_id, String filepath) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user_id);
        map.put("mac", mac_id);

        new HttpUtil().postFile(HttpUrl.setGetUrl("/IBSync/write_bna.aspx"), map, new File(filepath), new HttpUtil.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                if (data == "NG") {
                    mPresenter.upLoadPicFailed();
                } else {
                  mPresenter.upLoadPicSuccess();
                }
            }



            @Override
            public void onError(String meg) {
                mPresenter.upLoadPicFailed();
            }
        });
    }

    private List<HairStyleBABean> initRecyclerData(String[] bAdata) {
        List<HairStyleBABean> hairStyleBABeens = new ArrayList<>();
        for (int i = 0; i < bAdata.length; i++) {
            String[] split = bAdata[i].split("#");
            HairStyleBABean hairStyleBABean = new HairStyleBABean(split[1], split[0], i == 0 ? true : false);
            hairStyleBABeens.add(hairStyleBABean);
        }
        return hairStyleBABeens;
    }
}
