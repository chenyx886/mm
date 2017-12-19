package com.mm.contract.find;


import com.mm.contract.IBaseMvpView;
import com.mm.data.PageEntity;
import com.mm.data.entity.FindEntity;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午7:35
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IFindListView extends IBaseMvpView {

    /**
     * 发现列表
     */
    void FindList(PageEntity<FindEntity> data);
}
