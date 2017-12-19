package com.mm.contract.msg;


import com.mm.contract.IBaseMvpView;
import com.mm.data.PageEntity;
import com.mm.data.entity.MsgEntity;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午7:35
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IUnReadMsgListView extends IBaseMvpView {

    /**
     * 查询 我的消息 未读数量
     */
    void MsgUnLook(PageEntity<MsgEntity> data);

    /**
     * 分页显示未读消息
     *
     * @param pageEntity
     */
    void showUnReadMsgList(PageEntity<MsgEntity> pageEntity);

    /**
     * 显示已读操作结果
     *
     * @param msgLog
     */
    void showUpdateLockResult(MsgEntity msgLog);
}
