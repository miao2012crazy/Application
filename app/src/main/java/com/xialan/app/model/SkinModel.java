package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.bean.SkinBean;
import com.xialan.app.bean.SkinKeepingBean;
import com.xialan.app.bean.SkupBean;
import com.xialan.app.bean.SkupKeepingBean;
import com.xialan.app.contract.SkinContract;
import com.xialan.app.presenter.SkinPresenter;
import com.xialan.app.retrofit.NovateUtil;
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

/**
 * Created by Administrator on 2017/7/19.
 */
public class SkinModel implements SkinContract.Model {
    private final SkinPresenter skinPresenter;
    private ArrayList<SkinBean> skupList;

    public SkinModel(SkinPresenter skinPresenter) {
        this.skinPresenter=skinPresenter;
    }

    @Override
    public void getSkinSampleDataFromNet() {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getSkinData(), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                skinPresenter.getSkinSampleDataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String[] skupSampledata = CommonUtil.getSkupSampledata(responseBody.string());
                    List<SkinBean> skupBeanList = initRecyclerViewData(skupSampledata);
                    skinPresenter.getSkinSampleDataSuccess(skupBeanList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getSkinForUserFromNet(String user_id) {
        NovateUtil.getInstance().call(NovateUtil.getApiService().getSkinDataForUser(user_id), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {
                skinPresenter.getSkinForUserdataFailed();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {

                    List<SkinKeepingBean> skinKeepingBeens = new ArrayList<>();
                    String string = responseBody.string();
                    if (string.equals("")){
                        UIUtils.showToast(UIUtils.getContext(),"暂无数据!");
                        skinPresenter.getDataWithNull();
                        return;
                    }
                    String[] skupdata = CommonUtil.getBAdata(string);
                    for (int i = 0; i < skupdata.length; i++) {
                        String[] dataForspild = CommonUtil.getDataForspild(skupdata[i]);
                        SkinKeepingBean skinKeepingBean1 = new SkinKeepingBean(dataForspild[1], dataForspild[0], i == 0 ? true : false);
                        skinKeepingBeens.add(skinKeepingBean1);
                    }
                    skinPresenter.getSkinForUserdataSuccess(skinKeepingBeens);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<SkinBean> initRecyclerViewData(String[] skinSampledata) {
        skupList = new ArrayList<>();
        for (int i = 0; i < skinSampledata.length; i++) {
            String[] split = skinSampledata[i].split("#");
            SkinBean skupBean = new SkinBean(split[2], split[0], split[1], i == 0 ? true : false);
            skupList.add(skupBean);
        }
        return skupList;
    }
    @Override
    public void upLoadUserPic(String user_id, String mac_id, String filepath) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user_id);
        map.put("mac", mac_id);

        new HttpUtil().postFile(HttpUrl.setGetUrl("/IBSync/write_skin.aspx"), map, new File(filepath), new HttpUtil.HttpCallBack() {
            @Override
            public void onSusscess(String data) {
                if (data == "NG") {
                    skinPresenter.upLoadPicFailed();
                } else {
                    skinPresenter.upLoadPicSuccess();
                }
            }
            @Override
            public void onError(String meg) {
                skinPresenter.upLoadPicFailed();
            }
        });
    }
}
