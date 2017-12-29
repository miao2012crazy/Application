package com.xialan.app.utils;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/9/1.
 * 请求网络的API接口类
 */
public interface ApiService {
    /**
     * 获取首页按钮 标签数据
     *
     * @return
     */
    @GET("IBSync/search_category.aspx")
    Observable<ResponseBody> getData();

    /**
     * 获取头皮页面数据
     *
     * @return
     */
    @GET("IBSync/search_video.aspx")
    Observable<ResponseBody> getVideo();

    /**
     * 获取皮肤页面数据
     *
     * @return
     */
    @GET("IBSync/search_skin_sample.aspx")
    Observable<ResponseBody> getSkinData();

    /**
     * 获取皮肤页面数据
     *
     * @return
     */
    @GET("IBSync/search_skulp_sample.aspx")
    Observable<ResponseBody> getSkupData();

    /**
     * 获取教育视频数据
     *
     * @return
     */
    @GET("IBSync/search_lecture_video.aspx")
    Observable<ResponseBody> getTrainingData();

    /**
     * 获取皮肤页面数据
     *
     * @return
     */
    @GET("IBSync/search_lecture_video.aspx")
    Observable<ResponseBody> getTrainingDataForSerch(@Query("no") String position_id, @Query("sv") String serch_text);

    /**
     * 首页面广告数据
     *
     * @return
     */
    @GET("/IBSync/search_ib_notice.aspx")
    Observable<ResponseBody> getNotice();

    /**
     * 首页面广告数据
     *
     * @return
     */
    @GET("IBSync/search_product.aspx")
    Observable<ResponseBody> getDataForProductDetail(@Query("no") String position_id);

    /**
     * 首页面广告数据
     *
     * @return
     */
    @GET("IBSync/search_bna.aspx")
    Observable<ResponseBody> getheadStyleHistory(@Query("id") String user_id);

    /**
     * 登陆
     *
     * @param user_tel
     * @param user_pwd
     * @return
     */
    @GET("IBSync/search_login.aspx")
    Observable<ResponseBody> login(@Query("id") String user_tel, @Query("password") String user_pwd);

    /**
     * 获取头皮页面数据
     *
     * @param user_id 用户id
     * @return
     */
    @GET("IBSync/search_skulp.aspx")
    Observable<ResponseBody> getSkupDataForUser(@Query("id") String user_id);

    /**
     * 获取皮肤页面数据
     *
     * @param user_id 用户id
     * @return
     */
    @GET("IBSync/search_skin.aspx")
    Observable<ResponseBody> getSkinDataForUser(@Query("id") String user_id);

    /**
     * 获取验证码
     * @param user_number
     * @return
     */
    @GET("/IBSync/sms/SMSJoinAuth.aspx")
    Observable<ResponseBody> getVerifyCode(@Query("mobile") String user_number);

    /**
     * 校验验证码
     * @param msgid
     * @param verify_code
     * @return
     */
    @GET("/IBSync/sms/SMSCheckValid.aspx")
    Observable<ResponseBody> checkVerifyCode(@Query("msgid") String msgid, @Query("code") String verify_code);

    /**
     * 上传用户注册信息
     * @param mac_id
     * @param smscode
     * @param tel
     * @param psd
     * @param nick_name
     * @param age
     * @param sex
     * @param recommender
     * @param pic
     * @return
     */
    @FormUrlEncoded
    @POST("/IBSync/join_member.aspx")
    Observable<ResponseBody> upLoadUserRegeditData(@Field("mac") String mac_id, @Field("smscode") String smscode, @Field("mobile") String tel, @Field("password") String psd, @Field("nickname") String nick_name, @Field("age") String age, @Field("gender") String sex, @Field("recommender") String recommender, @Field("picture") String pic);
    /**
     * 上传用户注册信息
     * @param mac_id
     * @param smscode
     * @param tel
     * @param psd
     * @param nick_name
     * @param age
     * @param sex
     * @return
     */
    @FormUrlEncoded
    @POST("/IBSync/join_member.aspx")
    Observable<ResponseBody> upLoadUserWechatRegeditData(@Field("mac") String mac_id, @Field("smscode") String smscode, @Field("mobile") String tel, @Field("password") String psd, @Field("nickname") String nick_name, @Field("age") String age, @Field("gender") String sex,@Field("wechat_openid") String wechat_openid);
    /**
     *
     */

    /**
     * 用户认证wechat
     * @param wechat_id union_id
     * @param uid   用户id
     * @return
     */
    @FormUrlEncoded
    @POST("/IBSync/update_wechat.aspx")
    Observable<ResponseBody> getAuthentication(@Field("union_id") String wechat_id, @Field("id") String uid);

    @GET("/IBSync/new/search_version_update.ashx")
    Observable<ResponseBody> getmobileversion();
}
