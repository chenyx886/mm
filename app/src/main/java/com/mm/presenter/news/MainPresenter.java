package com.mm.presenter.news;


import com.chenyx.libs.utils.Logs;
import com.mm.contract.news.IMainView;
import com.mm.data.BaseData;
import com.mm.data.JsonRequestBody;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe：主页 业务操作类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(IMainView MvpView) {
        super(MvpView);
    }

    /**
     * 注册JPuashID
     *
     * @param UserID
     * @param JPushID
     * @param DeviceId
     */
    public void RJPushID(String UserID, String JPushID, String DeviceId) {
        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserID);
        param.put("JPushID", JPushID);
        param.put("DeviceId", DeviceId);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        addSubscription(RetrofitApiFactory.getUserApi().RJPush(body), new SubscriberCallBack<BaseData>() {
            @Override
            protected void onSuccess(BaseData data) {
                Logs.d("注册成功！");
            }

            @Override
            public void onCompleted() {
            }
        });
    }
}
