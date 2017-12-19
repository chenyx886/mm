package com.mm.data.api;


import com.mm.data.BaseData;
import com.mm.data.PageEntity;
import com.mm.data.entity.MsgEntity;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 类描述：消息模块Http Api
 * 创建人：Chenyx
 * 创建时间：2016/11/22 9:14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface MessageApi {
    /**
     * 查询 我的消息 未读数量
     *
     * @return
     */
    @POST("/App/MsgUnLook")
    Observable<BaseData<PageEntity<MsgEntity>>> MsgUnLook(@Body RequestBody body);

    /***
     * 分页获取消息二级列表
     *
     * @return
     */
    @POST("/App/ByMsgType")
    Observable<BaseData<PageEntity<MsgEntity>>> ByMsgType(@Body RequestBody body);

    /**
     * 更新消息为已读状态
     *
     * @param body
     * @return
     */
    @POST("/App/UpdateMsgLook")
    Observable<BaseData> UpdateMsgLook(@Body RequestBody body);
}
