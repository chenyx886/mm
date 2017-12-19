package com.mm.data.api;


import com.mm.data.BaseData;
import com.mm.data.PageEntity;
import com.mm.data.entity.BaseEntity;
import com.mm.data.entity.LocationEnitity;
import com.mm.data.entity.SignEntity;
import com.mm.data.entity.UserEntity;
import com.mm.data.entity.VersionEntity;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Company：苗苗
 * Class Describe： 我的模块接口
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface UserApi {

    /**
     * 注册JPush
     *
     * @param body
     * @return
     */
    @POST("/App/RJPush")
    Observable<BaseData> RJPush(@Body RequestBody body);

    /**
     * 用户登录
     *
     * @param body
     * @return
     */
    @POST("/App/login")
    Observable<BaseData<UserEntity>> Login(@Body RequestBody body);

    /**
     * 用户注册
     *
     * @param body
     * @return
     */
    @POST("/App/Register")
    Observable<BaseData<UserEntity>> Register(@Body RequestBody body);

    /**
     * 更新头像
     *
     * @param body
     * @return
     */
    @POST("/App/UpdateHeadImg")
    Observable<BaseData<BaseEntity>> UpdateHeadImg(@Body RequestBody body);

    /**
     * 更新头像
     *
     * @param body
     * @return
     */
    @POST("/App/checkVersion")
    Observable<BaseData<VersionEntity>> checkVersion(@Body RequestBody body);

    /**
     * 签到
     *
     * @param body
     * @return
     */
    @Multipart
    @POST("/App/UserSign")
    Observable<BaseData<SignEntity>> UserSign(@QueryMap Map<String, Object> body, @Part List<MultipartBody.Part> multiparts);

    /**
     * 签到列表
     *
     * @param body
     * @return
     */
    @POST("/App/SignList")
    Observable<BaseData<PageEntity<SignEntity>>> SignList(@Body RequestBody body);


    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
