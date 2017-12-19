package com.mm.contract.mine;


import com.mm.contract.IBaseMvpView;
import com.mm.data.PageEntity;
import com.mm.data.entity.SignEntity;

/**
 * Company：苗苗
 * Class Describe：签到列表
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午7:35
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface ISignListView extends IBaseMvpView {

    /**
     * 签到列表
     */
    void SignList(PageEntity<SignEntity> data);
}
