package com.mm.data.api;


import com.mm.data.BaseData;
import com.mm.data.PageEntity;
import com.mm.data.entity.ArticleEntity;
import com.mm.data.entity.CommentEntity;
import com.mm.data.entity.FindEntity;
import com.mm.data.entity.ImageEntity;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
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
public interface ArticleApi {


    /**
     * 文章列表
     *
     * @param body
     * @return
     */
    @POST("/App/AList")
    Observable<BaseData<PageEntity<ArticleEntity>>> AList(@Body RequestBody body);

    /**
     * 评论列表
     *
     * @param body
     * @return
     */
    @POST("/App/CommentList")
    Observable<BaseData<PageEntity<CommentEntity>>> CommentList(@Body RequestBody body);

    /**
     * 二级 评论列表
     *
     * @param body
     * @return
     */
    @POST("/App/CommentPIDList")
    Observable<BaseData<PageEntity<CommentEntity>>> CommentPIDList(@Body RequestBody body);

    /**
     * 提交文章、发现、回复评论
     *
     * @param body
     * @return
     */
    @POST("/App/CommentAdd")
    Observable<BaseData<CommentEntity>> CommentAdd(@Body RequestBody body);


    /**
     * 轮播
     *
     * @param body
     * @return
     */
    @POST("/App/AList")
    Observable<BaseData<PageEntity<ArticleEntity>>> Banners(@Body RequestBody body);

    /**
     * 发现列表
     *
     * @param body
     * @return
     */
    @POST("/App/FList")
    Observable<BaseData<PageEntity<FindEntity>>> FList(@Body RequestBody body);

    /**
     * 发布发现信息
     *
     * @param body
     * @return
     */
    @Multipart
    @POST("/App/AddFind")
    Observable<BaseData> AddFind(@QueryMap Map<String, Object> body, @Part List<MultipartBody.Part> multiparts);

    /**
     * 发布发现信息
     *
     * @param body
     * @return
     */
    @Multipart
    @POST("/App/AddVideoFind")
    Observable<BaseData> AddVideoFind(@QueryMap Map<String, Object> body, @Part List<MultipartBody.Part> multiparts);



    /**
     * 批量文件上传
     *
     * @param multiparts
     * @return
     */
    @Multipart
    @POST("/App/MyUpLoad")
    Observable<BaseData<List<ImageEntity>>> MyUpLoad(@Part List<MultipartBody.Part> multiparts);
}
