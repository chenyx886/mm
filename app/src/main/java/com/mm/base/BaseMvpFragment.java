package com.mm.base;

import android.os.Bundle;
import android.view.View;

import com.mm.presenter.BasePresenter;

/**
 * Company：苗苗
 * Class Describe：MvpFragment基类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends LazyLoadFragment {

    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        if (mPresenter == null) mPresenter = createPresenter();
        super.onViewCreated(view, bundle);
    }

    @Override
    protected void lazyLoad() {
        if (mPresenter == null) mPresenter = createPresenter();
        super.lazyLoad();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
