package com.mm.presenter;


import com.mm.contract.IBaseMvpView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Company：苗苗
 * Class Describe： BasePresenter 基类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class BasePresenter<V extends IBaseMvpView> implements IPresenter<V> {
    /**
     * View 层
     */
    public V MvpView;

    private CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V MvpView) {
        this.MvpView = MvpView;
    }

    public BasePresenter(V mvpView) {
        attachView(mvpView);
    }

    @Override
    public void detachView() {
        onUnsubscribe();
        this.MvpView = null;
    }

    //RxJava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}