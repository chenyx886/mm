package com.mm.contract.find;


import com.mm.contract.IBaseMvpView;
import com.mm.data.PageEntity;
import com.mm.data.entity.CommentEntity;

/**
 * Company：苗苗
 * Class Describe： 发现详情
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IFindDetailView extends IBaseMvpView {

    void showData(PageEntity<CommentEntity> list);

    void AddCommentSuccess();

}
