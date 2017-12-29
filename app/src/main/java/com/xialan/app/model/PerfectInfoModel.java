package com.xialan.app.model;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.contract.PerfectInfoContract;
import com.xialan.app.retrofit.NovateUtil;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/9/19.
 */

public class PerfectInfoModel implements PerfectInfoContract.Model {
    private  PerfectInfoContract.Presenter mPresenter;
    private File file=null;

    public PerfectInfoModel(PerfectInfoContract.Presenter presenter) {
        this.mPresenter=presenter;

    }

    @Override
    public void submitUserDataToNet(String mac_id, String smscode, final String tel, String psd, String nick_name, String age, String sex, String recommender, final String header_img_path) {
        if (!header_img_path.equals("")){
            file = new File(header_img_path);
        }
        NovateUtil.getInstance().call(NovateUtil.getApiService().upLoadUserRegeditData(mac_id, smscode, tel, psd, nick_name, age, sex, recommender ,""), new BaseSubscriber<ResponseBody>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String string = responseBody.string();
                    if (string.equals("OK")){
                        mPresenter.submitSuccess();
                        if (!header_img_path.equals("")){
                            upLoadHeaderImg(tel,header_img_path);
                        }
                    }else{
                        mPresenter.submitFailed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private void upLoadHeaderImg(String user_id,String img_path) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", user_id);
        new HttpUtil().postFile(HttpUrl.setGetUrl("IBSync/join_member_picture.aspx"), map, new File(img_path), new HttpUtil.HttpCallBack() {
            private String str;
            @Override
            public void onSusscess(String data) {
                try{
                    if (data!=""||data!=null){
                        str= data.substring(0,2);
                    }
                }catch (Exception e){

                }
                if (str == "NG") {
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
}
