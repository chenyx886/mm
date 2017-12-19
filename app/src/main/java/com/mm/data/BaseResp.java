package com.mm.data;


import com.google.gson.Gson;
import com.mm.data.entity.BaseEntity;

/**
 * Company：苗苗
 * Class Describe：客户端与服务器的数据传输的接收ValueObject
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class BaseResp extends BaseEntity {
    /**
     * 重写toString方法以实现JSON返回
     *
     * @return JSON字符串
     */
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

