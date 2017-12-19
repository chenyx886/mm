package com.mm.presenter.find;

import com.mm.contract.find.IFindDetailView;
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
 * Class Describe： 发现详情
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FindDetailPresenter extends BasePresenter<IFindDetailView> {

    public FindDetailPresenter(IFindDetailView MvpView) {
        super(MvpView);
    }


    public void RelpyList(int FindID, int Page) {
        Map<String, Object> param = new HashMap<>();
        param.put("FindID", FindID);
        param.put("Page", Page);

        RequestBody body = JsonRequestBody.createJsonBody(param);

        addSubscription(RetrofitApiFactory.getArticleApi().CommentList(body), new SubscriberCallBack<PageEntity<CommentEntity>>() {
            @Override
            protected void onSuccess(PageEntity<CommentEntity> data) {
                MvpView.showData(data);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }


    public void FindReply(int ArticleID, int FindID, int CID, int PID, int UserID, int BRUserID, String BRNickName, String Content) {
        Map<String, Object> param = new HashMap<>();
        param.put("FindID", FindID);
        param.put("CID", CID);
        param.put("PID", PID);
        param.put("UserID", UserID);
        param.put("BRUserID", BRUserID);
        param.put("BRNickName", BRNickName);
        param.put("Content", Content);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("操作中...");

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

    public void RelpyPidList(int PID, int Page) {
        Map<String, Object> param = new HashMap<>();
        param.put("PID", PID);
        param.put("Page", Page);

        RequestBody body = JsonRequestBody.createJsonBody(param);

        addSubscription(RetrofitApiFactory.getArticleApi().CommentPIDList(body), new SubscriberCallBack<PageEntity<CommentEntity>>() {
            @Override
            protected void onSuccess(PageEntity<CommentEntity> data) {
                MvpView.showData(data);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });

    }
}
