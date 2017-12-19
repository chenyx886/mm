package com.mm.contract;


import com.mm.data.entity.UserEntity;

/**
 * 类描述：
 * 公司：多彩贵州网
 * 创建人：陈永祥
 * 创建时间：2017/11/3 下午5:27
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface ILoginView extends IBaseMvpView {
    /**
     * 登录成功
     */
    void LoginSuccess(UserEntity data);
}
