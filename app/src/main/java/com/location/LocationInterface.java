package com.location;


import com.mm.data.entity.LocationEnitity;

/**
 * 类描述： 百度定位接口回调
 * 创建人：Chenyx
 * 创建时间：2016/11/9 10:25
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface LocationInterface {

    /**
     * 定位成功
     *
     * @param locationInfo
     */
    void onReLocationSucess(LocationEnitity locationInfo);

    /**
     * 定位失败
     *
     * @param mesSage
     */
    void onReLocationFail(String mesSage);

}
