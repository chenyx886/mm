package com.mm.presenter.mine;

import com.mm.contract.mine.ISignListView;
import com.mm.data.JsonRequestBody;
import com.mm.data.PageEntity;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.SignEntity;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe：签到列表
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class SignListPresenter extends BasePresenter<ISignListView> {

    public PageEntity pageEntity;

    public SignListPresenter(ISignListView MvpView) {
        super(MvpView);
    }

    public void SignList(int UserID, int Page, int Rows) {
        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserID);
        param.put("Page", Page);
        param.put("Rows", Rows);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("加载中");

        addSubscription(RetrofitApiFactory.getUserApi().SignList(body), new SubscriberCallBack<PageEntity<SignEntity>>() {
            @Override
            protected void onSuccess(PageEntity<SignEntity> list) {
                MvpView.SignList(list);
                pageEntity = list;
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
