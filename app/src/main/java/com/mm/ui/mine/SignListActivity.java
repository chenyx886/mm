package com.mm.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chenyx.libs.utils.SysConfig;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.mine.ISignListView;
import com.mm.data.PageEntity;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.SignEntity;
import com.mm.presenter.mine.SignListPresenter;
import com.mm.ui.adapter.SignListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Company：苗苗
 * Class Describe：签到记录
 * Create Person：Chenyx
 * Create Time：2017/11/29 下午7:40
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class SignListActivity extends BaseMvpActivity<SignListPresenter> implements ISignListView {
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
    private SignListAdapter mAdapter;

    @Override
    protected SignListPresenter createPresenter() {
        return new SignListPresenter(this);
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
        mTitle.setText("签到记录");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new SignListAdapter(this));
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_PULL_REFRESH;
                mPresenter.SignList(SysConfig.nullToInt(UserCache.get().getUserID()), 0, 20);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.SignList(SysConfig.nullToInt(UserCache.get().getUserID()), p, 20);
            }
        });
        mRecyclerView.setRefreshing(true);
    }

    @Override
    public void SignList(PageEntity<SignEntity> data) {
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