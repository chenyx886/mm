package com.mm.contract;

/**
 * Company：苗苗
 * Class Describe：登录 Presention 和 View的 关联
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public interface IBaseMvpView {

    /**
     * This is a general method used for showing some kind of progress during a background task. For example, this
     * method should show a progress bar and/or disable buttons before some background work starts.
     */
    void showProgress(String message);

    /**
     * This is a general method used for hiding progress information after a background task finishes.
     */
    void hideProgress();

}
