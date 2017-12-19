package com.mm.presenter.news;

import com.mm.contract.news.INewsView;
import com.mm.data.JsonRequestBody;
import com.mm.data.PageEntity;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.ArticleEntity;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class NewsPresenter extends BasePresenter<INewsView> {

    public NewsPresenter(INewsView MvpView) {
        super(MvpView);
    }


    public void Banners(int NodeID, int IsHeadline, int Page, int Rows) {
        Map<String, Object> param = new HashMap<>();
        param.put("NodeID", NodeID);
        param.put("IsHeadline", IsHeadline);
        param.put("Page", Page);
        param.put("Rows", Rows);

        RequestBody body = JsonRequestBody.createJsonBody(param);

        addSubscription(RetrofitApiFactory.getArticleApi().Banners(body), new SubscriberCallBack<PageEntity<ArticleEntity>>() {
            @Override
            protected void onSuccess(PageEntity<ArticleEntity> list) {
                MvpView.Banners(list);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }


    public void ArticleList(int NodeID, int Page, int Rows) {
        Map<String, Object> param = new HashMap<>();
        param.put("NodeID", NodeID);
        param.put("Page", Page);
        param.put("Rows", Rows);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("加载中");

        addSubscription(RetrofitApiFactory.getArticleApi().AList(body), new SubscriberCallBack<PageEntity<ArticleEntity>>() {
            @Override
            protected void onSuccess(PageEntity<ArticleEntity> list) {
                MvpView.ArticleList(list);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
