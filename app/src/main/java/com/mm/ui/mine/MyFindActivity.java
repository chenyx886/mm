package com.mm.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.SysConfig;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.find.IFindListView;
import com.mm.data.PageEntity;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.FindEntity;
import com.mm.presenter.find.FindListPresenter;
import com.mm.ui.adapter.FindAdapter;
import com.mm.ui.find.FindDetailActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Company：苗苗
 * Class Describe：我的发现
 * Create Person：Chenyx
 * Create Time：2017/11/25 下午10:37
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MyFindActivity extends BaseMvpActivity<FindListPresenter> implements IFindListView {
    /**
     * 返回
     */
    @BindView(R.id.tv_back)
    TextView mBack;
    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.recyclerView)
    XRecyclerView mRecyclerView;

    public int p = 0;
    private static int state = -1;
    private static int STATE_LOAD_MORE = 0X10;
    private static int STATE_PULL_REFRESH = 0X20;
    private FindAdapter mAdapter;

    @Override
    protected FindListPresenter createPresenter() {
        return new FindListPresenter(this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_my_find);
    }

    @Override
    protected void initUI() {
        mTitle.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mTitle.setText("我的发现");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new FindAdapter(this));
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mAdapter.setOnItemViewClickListener(new AbstractRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("findEntity", mAdapter.getItem(position));
                JumpUtil.overlay(MyFindActivity.this, FindDetailActivity.class, bundle);
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_PULL_REFRESH;
                mPresenter.FindList(SysConfig.nullToInt(UserCache.get().getUserID()), 0, 20);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.FindList(SysConfig.nullToInt(UserCache.get().getUserID()), p, 20);
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


    /**
     * 点击操作
     *
     * @param view
     */
    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {

            //返回
            case R.id.tv_back:
                finish();
                break;
            default:
                break;

        }
    }

}
