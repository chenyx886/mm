package com.mm.presenter.msg;

import com.mm.contract.msg.IUnReadMsgListView;
import com.mm.data.BaseData;
import com.mm.data.JsonRequestBody;
import com.mm.data.PageEntity;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.MsgEntity;
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
public class UnReadMsgListPresenter extends BasePresenter<IUnReadMsgListView> {

    public PageEntity pageEntity;

    public UnReadMsgListPresenter(IUnReadMsgListView MvpView) {
        super(MvpView);
    }

    /**
     * 查询 我的消息 未读数量
     *
     * @param UserID
     */
    public void MsgUnLook(String UserID) {
        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserID);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("加载中");

        addSubscription(RetrofitApiFactory.getMessageApi().MsgUnLook(body), new SubscriberCallBack<PageEntity<MsgEntity>>() {
            @Override
            protected void onSuccess(PageEntity<MsgEntity> list) {
                MvpView.MsgUnLook(list);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }

    /**
     * 分页获取消息二级列表
     *
     * @param UserID
     * @param MsgType
     * @param Page
     */
    public void ByMsgType(String UserID, int MsgType, int Page) {
        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserID);
        param.put("MsgType", MsgType);
        param.put("Page", Page);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("加载中");

        addSubscription(RetrofitApiFactory.getMessageApi().ByMsgType(body), new SubscriberCallBack<PageEntity<MsgEntity>>() {
            @Override
            protected void onSuccess(PageEntity<MsgEntity> list) {
                MvpView.showUnReadMsgList(list);
                pageEntity = list;
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }

    /**
     * 更新消息为已读状态
     *
     * @param msg
     */
    public void UpdateMsgLook(final MsgEntity msg) {
        Map<String, Object> param = new HashMap<>();
        param.put("ID", msg.getID());

        RequestBody body = JsonRequestBody.createJsonBody(param);
        addSubscription(RetrofitApiFactory.getMessageApi().UpdateMsgLook(body), new SubscriberCallBack<BaseData>() {
            @Override
            protected void onSuccess(BaseData data) {
                msg.setIsLook(1);
                MvpView.showUpdateLockResult(msg);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
