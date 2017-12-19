package com.mm.presenter;


import com.chenyx.libs.utils.Apps;
import com.mm.MApplication;
import com.mm.contract.ILoginView;
import com.mm.data.JsonRequestBody;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.RequestBody;

/**
 * Company：苗苗
 * Class Describe： 登录界面业务操作类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UserPresenter extends BasePresenter<ILoginView> {

    public UserPresenter(ILoginView MvpView) {
        super(MvpView);
    }

    /**
     * 登录
     *
     * @param phone 手机号
     * @param Pwd   密码
     */
    public void Login(String phone, final String Pwd) {
        Map<String, Object> param = new HashMap<>();
        param.put("Phone", phone);
        param.put("Pwd", Pwd);
        param.put("JPushID", JPushInterface.getRegistrationID(MApplication.getInstance()));
        param.put("DeviceId", Apps.getDeviceId(MApplication.getInstance()));

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("登录中");

        addSubscription(RetrofitApiFactory.getUserApi().Login(body), new SubscriberCallBack<UserEntity>() {
            @Override
            protected void onSuccess(UserEntity user) {
                MvpView.LoginSuccess(user);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }


    /**
     * 注册
     *
     * @param phone 手机号
     * @param Pwd   密码
     */
    public void Register(String phone, String Pwd, String NickName) {
        Map<String, Object> param = new HashMap<>();
        param.put("Phone", phone);
        param.put("Pwd", Pwd);
        param.put("NickName", NickName);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("注册中");

        addSubscription(RetrofitApiFactory.getUserApi().Register(body), new SubscriberCallBack<UserEntity>() {
            @Override
            protected void onSuccess(UserEntity user) {
                MvpView.LoginSuccess(user);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
