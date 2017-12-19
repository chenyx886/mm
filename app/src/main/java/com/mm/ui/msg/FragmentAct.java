package com.mm.ui.msg;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.mm.R;
import com.mm.base.BaseMvpFragment;
import com.mm.contract.find.IFindListView;
import com.mm.data.PageEntity;
import com.mm.data.entity.FindEntity;
import com.mm.presenter.find.FindListPresenter;
import com.mm.ui.adapter.PhotoAdapter;

import butterknife.BindView;


/**
 * Company：苗苗
 * Class Describe：活动
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class FragmentAct extends BaseMvpFragment<FindListPresenter> implements IFindListView {

    public static String TAG = FragmentAct.class.getName();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.coord_container)
    CoordinatorLayout mCoordContainer;

    @BindView(R.id.refresh)
    TwinklingRefreshLayout mRefreshLayout;
    public int p = 0;

    private PhotoAdapter photoAdapter;

    @Override
    protected FindListPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new FindListPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_act, container, false);
        return view;
    }

    @Override
    protected void initUI() {

        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        photoAdapter = new PhotoAdapter();
        mRecyclerview.setAdapter(photoAdapter);

        ProgressLayout headerView = new ProgressLayout(getActivity());
        mRefreshLayout.setHeaderView(headerView);
        mRefreshLayout.setOverScrollRefreshShow(false);
        mRefreshLayout.setFloatRefresh(true);
        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setHeaderHeight(140);
        mRefreshLayout.setMaxHeadHeight(240);
        mRefreshLayout.setTargetView(mRecyclerview);


        mRefreshLayout.startRefresh();

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                p = 0;
                mPresenter.FindList(0, p, 20);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                mPresenter.FindList(0, p, 20);
            }
        });

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mRefreshLayout.setEnableRefresh(true);
                    mRefreshLayout.setEnableOverScroll(false);
                } else {
                    mRefreshLayout.setEnableRefresh(false);
                    mRefreshLayout.setEnableOverScroll(false);
                }
            }
        });
    }

    @Override
    public void FindList(PageEntity<FindEntity> data) {
        if (p == 0) {
            photoAdapter.setDataList(data.getRows());
        } else {
            photoAdapter.addItems(data.getRows());
        }
        ++p;
    }


    @Override
    public void showProgress(String message) {
    }

    @Override
    public void hideProgress() {
        mRefreshLayout.finishRefreshing();
        mRefreshLayout.finishLoadmore();
    }


}
