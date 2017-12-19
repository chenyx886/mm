package com.mm.data;

/**
 * Company：苗苗
 * Class Describe： 区分请求成功和请求失败，只允许成功的进入onSuccess方法里，把错误逻辑都扔到onFailure里，避免了原来在onResponse里解析时的过多的判断
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public abstract class SubscriberCallBack<T> extends BaseCallBack<BaseData<T>> {

    @Override
    public void onNext(BaseData response) {
        if (response.getResult() == 1) {
            onSuccess((T) response.getData());
        } else {
            onFailure(response);
        }
    }

    protected abstract void onSuccess(T response);
}