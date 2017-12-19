package com.mm.contract.news;


import com.mm.contract.IBaseMvpView;
import com.mm.data.PageEntity;
import com.mm.data.entity.CommentEntity;

/**
 * 类描述：
 * 创建人：Chenyx
 * 创建时间：2017/3/24 21:14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface INewsDetailView extends IBaseMvpView {

    /**
     * 提交评论成功
     */
    void AddCommentSuccess();

    /**
     * 评论列表
     */
    void CommentList(PageEntity<CommentEntity> list);
}
