package com.mm.ui.find;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.JumpUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mm.R;
import com.mm.base.BaseMvpFragment;
import com.mm.contract.find.IFindListView;
import com.mm.data.PageEntity;
import com.mm.data.entity.FindEntity;
import com.mm.presenter.find.FindListPresenter;
import com.mm.ui.adapter.FindAdapter;

import butterknife.BindView;


/**
 * Company：苗苗
 * Class Describe：发现 FragmentFind
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class FragmentFind extends BaseMvpFragment<FindListPresenter> implements IFindListView {

    public static String TAG = FragmentFind.class.getName();
    @BindView(R.id.recyclerView)
    XRecyclerView mRecyclerView;

    public int p = 0;
    private static int state = -1;
    private static int STATE_LOAD_MORE = 0X10;
    private static int STATE_PULL_REFRESH = 0X20;
    private FindAdapter mAdapter;

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
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }

    @Override
    protected void initUI() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter = new FindAdapter(getActivity()));
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mAdapter.setOnItemViewClickListener(new AbstractRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("findEntity", mAdapter.getItem(position));
                JumpUtil.overlay(getActivity(), FindDetailActivity.class, bundle);
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_PULL_REFRESH;
                mPresenter.FindList(0,0, 20);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.FindList(0,p, 20);
            }
        });
        mRecyclerView.setRefreshing(true);
    }

    @Override
    public void FindList(PageEntity<FindEntity> data) {
        if (state == STATE_PULL_REFRESH) {
            p = 0;
            mAdapter.setData(data.getRows());
        } else if (state == STATE_LOAD_MORE) {
            mAdapter.insert(mAdapter.getItemCount(), data.getRows());
        }
        ++p;

    }


    @Override
    public void showProgress(String message) {
    }

    @Override
    public void hideProgress() {
        if (state == STATE_PULL_REFRESH) {
            mRecyclerView.refreshComplete();
        } else if (state == STATE_LOAD_MORE) {
            mRecyclerView.loadMoreComplete();
        }

        if (mPresenter.pageEntity != null && !mPresenter.pageEntity.getRows().isEmpty()) {
            mRecyclerView.setLoadingMoreEnabled(true);
            if (mAdapter.getItemCount() >= mPresenter.pageEntity.getTotal()) {
                mRecyclerView.setNoMore(true);
            } else {
                mRecyclerView.setLoadingMoreEnabled(true);
                mRecyclerView.setNoMore(false);
            }
        } else {
            mRecyclerView.setLoadingMoreEnabled(false);
        }
    }


}
