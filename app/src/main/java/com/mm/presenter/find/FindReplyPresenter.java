package com.mm.presenter.find;

import com.mm.contract.find.IFindReplyView;
import com.mm.data.JsonRequestBody;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.BaseEntity;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe： 发现回复
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FindReplyPresenter extends BasePresenter<IFindReplyView> {

    public FindReplyPresenter(IFindReplyView MvpView) {
        super(MvpView);
    }


    public void FindReply(int ArticleID, int FindID, int CID, int PID, String UserID, String Content) {
        Map<String, Object> param = new HashMap<>();
        param.put("FindID", FindID);
        param.put("CID", CID);
        param.put("PID", PID);
        param.put("UserID", UserID);
        param.put("Content", Content);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("操作中...");

        addSubscription(RetrofitApiFactory.getArticleApi().CommentAdd(body), new SubscriberCallBack<BaseEntity>() {
            @Override
            protected void onSuccess(BaseEntity data) {
                MvpView.AddCommentSuccess();
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }

}
