package com.mm.data.api;


import com.mm.data.BaseData;
import com.mm.data.entity.NodeEntity;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
public interface NodeApi {


    /**
     * 节点列表
     *
     * @param body
     * @return
     */
    @POST("/App/NList")
    Observable<BaseData<List<NodeEntity>>> NList(@Body RequestBody body);


}
