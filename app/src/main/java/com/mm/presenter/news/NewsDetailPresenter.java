package com.mm.presenter.news;

import com.mm.contract.news.INewsDetailView;
import com.mm.data.BaseData;
import com.mm.data.JsonRequestBody;
import com.mm.data.PageEntity;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.CommentEntity;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe：新闻详情
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

    public NewsDetailPresenter(INewsDetailView MvpView) {
        super(MvpView);
    }

    /**
     * 评论列表
     *
     * @param ArticleID
     */
    public void CommentList(int ArticleID, int Page) {
        Map<String, Object> param = new HashMap<>();
        param.put("ArticleID", ArticleID);
        param.put("Page", Page);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        addSubscription(RetrofitApiFactory.getArticleApi().CommentList(body), new SubscriberCallBack<PageEntity<CommentEntity>>() {
            @Override
            protected void onSuccess(PageEntity<CommentEntity> list) {
                if (MvpView != null)
                    MvpView.CommentList(list);
            }

            @Override
            public void onCompleted() {
                if (MvpView != null)
                    MvpView.hideProgress();
            }
        });
    }

    /**
     * 提交评论
     *
     * @param ArticleID
     * @param UserID
     * @param CommentContent
     */
    public void CommentAdd(int ArticleID, String UserID, String CommentContent) {
        Map<String, Object> param = new HashMap<>();
        param.put("ArticleID", ArticleID);
        param.put("UserID", UserID);
        param.put("Content", CommentContent);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("提交中");

        addSubscription(RetrofitApiFactory.getArticleApi().CommentAdd(body), new SubscriberCallBack<BaseData>() {
            @Override
            protected void onSuccess(BaseData data) {
                MvpView.AddCommentSuccess();
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
