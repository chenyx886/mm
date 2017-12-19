package com.mm.presenter.find;

import com.mm.contract.find.IFindListView;
import com.mm.data.JsonRequestBody;
import com.mm.data.PageEntity;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.FindEntity;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe：发现列表
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FindListPresenter extends BasePresenter<IFindListView> {

    public PageEntity pageEntity;

    public FindListPresenter(IFindListView MvpView) {
        super(MvpView);
    }

    public void FindList(int UserID, int Page, int Rows) {
        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserID);
        param.put("Page", Page);
        param.put("Rows", Rows);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("加载中");

        addSubscription(RetrofitApiFactory.getArticleApi().FList(body), new SubscriberCallBack<PageEntity<FindEntity>>() {
            @Override
            protected void onSuccess(PageEntity<FindEntity> list) {
                MvpView.FindList(list);
                pageEntity = list;
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
