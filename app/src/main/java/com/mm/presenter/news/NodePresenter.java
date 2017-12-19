package com.mm.presenter.news;

import com.mm.contract.news.INodeView;
import com.mm.data.JsonRequestBody;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.NodeEntity;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.List;
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
public class NodePresenter extends BasePresenter<INodeView> {

    public NodePresenter(INodeView MvpView) {
        super(MvpView);
    }

    public void NodeList(int NodeID) {
        Map<String, Object> param = new HashMap<>();
        param.put("NodeID", NodeID);
        RequestBody body = JsonRequestBody.createJsonBody(param);

        addSubscription(RetrofitApiFactory.getNodeApi().NList(body), new SubscriberCallBack<List<NodeEntity>>() {
            @Override
            protected void onSuccess(List<NodeEntity> list) {
                MvpView.showNode(list);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }

}
