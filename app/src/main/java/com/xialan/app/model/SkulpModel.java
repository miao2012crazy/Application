package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;
import com.xialan.app.contract.SkulpContract;
import com.xialan.app.presenter.SkulpPresenter;
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
 * Created by Administrator on 2017/7/19.
 */
public class SkulpModel implements SkulpContract.Model {
    private final SkulpPresenter skulpPresenter;
    private ArrayList<SkupBean> skupList;

    public SkulpModel(SkulpPresenter skulpPresenter) {
        this.skulpPresenter=skulpPresenter;
    }

    @Override
    public void getSkupSampleDataFromNet() {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getSkupData(), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                skulpPresenter.getSkupSampleDataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {

                    String[] skupSampledata = CommonUtil.getSkupSampledata(responseBody.string());
                    List<SkupBean> skupBeanList = initRecyclerViewData(skupSampledata);
                    skulpPresenter.getSkupSampleDataSuccess(skupBeanList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void getSkupForUserFromNet(String user_id) {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getSkupDataForUser(user_id), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                skulpPresenter.getSkupForUserdataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {

                    List<SkupKeepingBean> skupKeepingBeen = new ArrayList<>();
                    String string = responseBody.string();
                    if (string.equals("")){
                        UIUtils.showToast(UIUtils.getContext(),"暂无数据!");
                        skulpPresenter.getDataWithNull();
                        return;
                    }
                    String[] skupdata = CommonUtil.getBAdata(string);
                    for (int i = 0; i < skupdata.length; i++) {
                        String[] dataForspild = CommonUtil.getDataForspild(skupdata[i]);
                        SkupKeepingBean skupKeepingBean1 = new SkupKeepingBean(dataForspild[1], dataForspild[0], i == 0 ? true : false);
                        skupKeepingBeen.add(skupKeepingBean1);
                    }
                    skulpPresenter.getSkupForUserdataSuccess(skupKeepingBeen);
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

        new HttpUtil().postFile(HttpUrl.setGetUrl("/IBSync/write_skulp.aspx"), map, new File(filepath), new HttpUtil.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                if (data == "NG") {
                    skulpPresenter.upLoadPicFailed();
                } else {
                    skulpPresenter.upLoadPicSuccess();
                }
            }
            @Override
            public void onError(String meg) {
                skulpPresenter.upLoadPicFailed();
            }
        });
    }

    private List<SkupBean> initRecyclerViewData(String[] skupSampledata) {
        skupList = new ArrayList<>();
        for (int i = 0; i < skupSampledata.length; i++) {
            String[] split = skupSampledata[i].split("#");
            SkupBean skupBean = new SkupBean(split[2], split[0], split[1], i == 0 ? true : false);
            skupList.add(skupBean);
        }
        return skupList;
    }
}
