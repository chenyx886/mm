package com.mm.base;

import android.os.Bundle;

import com.mm.presenter.BasePresenter;


/**
 * Company：苗苗
 * Class Describe：MvpActivity 基类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(Bundle bundle) {
        mPresenter = createPresenter();
        super.onCreate(bundle);
    }

    protected abstract P createPresenter();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}