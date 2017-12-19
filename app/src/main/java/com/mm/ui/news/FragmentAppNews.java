package com.mm.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mm.R;
import com.mm.base.BaseMvpFragment;
import com.mm.contract.news.INewsView;
import com.mm.data.PageEntity;
import com.mm.data.entity.ArticleEntity;
import com.mm.data.entity.BaseEntity;
import com.mm.presenter.news.NewsPresenter;
import com.mm.ui.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 类描述：新闻
 * 创建人：Chenyx
 * 创建时间：2017/3/23 9:35
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class FragmentAppNews extends BaseMvpFragment<NewsPresenter> implements INewsView {

    public static String TAG = FragmentAppNews.class.getName();

    private int NodeID;
    /**
     * 数据列表
     */
    @BindView(R.id.xr_data_list)
    XRecyclerView mDataList;

    /**
     * 数据适配器
     */
    private NewsAdapter mAdapter;

    /**
     * 数据列表
     */
    private List<BaseEntity> baseEntities = new ArrayList<>();

    private int sizes = 0;
    private static int state = -1;
    private static int STATE_LOAD_MORE = 0X10;
    private static int STATE_PULL_REFRESH = 0X20;
    public int p = 0;


    @Override
    protected NewsPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new NewsPresenter(this);
        }
        return mPresenter;
    }


    public static FragmentAppNews getInstance(int NodeID) {
        FragmentAppNews sf = new FragmentAppNews();
        sf.NodeID = NodeID;
        return sf;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_item_news, null);
    }

    @Override
    protected void lazyLoad() {
        if (mAdapter != null && mAdapter.getItemCount() <= 0) {
            if (mDataList != null) {
                mDataList.setRefreshing(true);
            }
        }
    }


    @Override
    protected void initUI() {
        mDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDataList.setNoMore(true);
        mDataList.setLoadingMoreEnabled(false);
        mDataList.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mDataList.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);

        mAdapter = new NewsAdapter(getActivity());
        mDataList.setAdapter(mAdapter);

        initData();

    }

    protected void initData() {

        mDataList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mDataList.setLoadingMoreEnabled(false);
                state = STATE_PULL_REFRESH;
                mPresenter.Banners(NodeID, 1, 0, 5);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.ArticleList(NodeID, p, 20);
            }
        });
        mDataList.setRefreshing(true);
    }

    @Override
    public void Banners(PageEntity<ArticleEntity> data) {
        if (data.getRows()!=null) {
            baseEntities.clear();
            baseEntities.add(data);
            mAdapter.setData(baseEntities);
            p = 0;
            mPresenter.ArticleList(NodeID, p, 20);
        }
    }

    @Override
    public void ArticleList(PageEntity<ArticleEntity> data) {

        List<BaseEntity> baseEns = new ArrayList<>();
        baseEns.addAll(data.getRows());
        mAdapter.insert(mAdapter.getItemCount(), baseEns);
        if (p == 0) {
            if (mDataList != null)
                mDataList.setLoadingMoreEnabled(true);
        }
        ++p;
        sizes = data.getTotal();
    }


    @Override
    public void showProgress(String message) {

    }

    @Override
    public void hideProgress() {
        if (state == STATE_PULL_REFRESH) {
            if (mDataList != null)
                mDataList.refreshComplete();
        } else if (state == STATE_LOAD_MORE) {
            if (mDataList != null)
                mDataList.loadMoreComplete();
        }
        if (mAdapter.getItemCount() >= sizes + 1) {
            if (mDataList != null)
                mDataList.setNoMore(true);
        } else {
            if (mDataList != null)
                mDataList.setNoMore(false);
        }
    }


}