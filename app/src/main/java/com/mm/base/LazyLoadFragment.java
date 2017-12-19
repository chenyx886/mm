package com.mm.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Company：苗苗
 * Class Describe：懒加载Fragment 主要用于ViewPager 切换Fragment的时候
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public abstract class LazyLoadFragment extends BaseFragment {

    protected View laView;

    /**
     * 是否需要加载数据
     */
    protected boolean isNeedData = true;

    /**
     * 组件是否已经准备好
     */
    protected boolean isPrepared;

    /**
     * 界面对用户是否可见
     */
    protected boolean isVisible;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return super.onCreateView(inflater, container, bundle);
    }

    protected void inflateView(int resId, LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        laView = inflater.inflate(resId, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        // 界面组件准备完毕
        isPrepared = true;
        visible();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            visible();
        } else {
            isVisible = false;
        }
    }

    protected void isNeedata(boolean isNeedData) {
        this.isNeedData = isNeedData;
    }

    /**
     * 懒加载的条件： 1.界面准备完成 isPrepared 2.界面对用户可见 3.是否需要加载数据
     */
    private void visible() {
        if (isNeedData && isPrepared && isVisible) {
            lazyLoad();
        }
    }

    /**
     * 懒加载抽象方法:加载数据
     */
    protected void lazyLoad() {
    }

}
