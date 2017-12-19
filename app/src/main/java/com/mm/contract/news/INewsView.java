package com.mm.contract.news;


import com.mm.contract.IBaseMvpView;
import com.mm.data.PageEntity;
import com.mm.data.entity.ArticleEntity;

/**
 * 类描述：
 * 创建人：Chenyx
 * 创建时间：2017/3/24 21:14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface INewsView extends IBaseMvpView {
    /**
     * 轮播
     */
    void Banners(PageEntity<ArticleEntity> data);

    /**
     * 新闻列表
     */
    void ArticleList(PageEntity<ArticleEntity> data);
}
