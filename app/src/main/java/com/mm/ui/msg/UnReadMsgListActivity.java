package com.mm.ui.msg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.msg.IUnReadMsgListView;
import com.mm.data.PageEntity;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.MsgEntity;
import com.mm.data.event.UnReadMsgItemEvent;
import com.mm.presenter.msg.UnReadMsgListPresenter;
import com.mm.ui.adapter.UnReadMsgListAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Company：苗苗
 * Class Describe：未读消息类型下消息列表
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class UnReadMsgListActivity extends BaseMvpActivity<UnReadMsgListPresenter> implements IUnReadMsgListView {


    /**
     * 数据列表
     */
    @BindView(R.id.dataList)
    XRecyclerView mDataList;


    private int msgType;

    private String msgName;

    /**
     * 返回
     */
    @BindView(R.id.tv_back)
    TextView tvBack;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**
     * 加载更多页数
     */
    private int page = 0;
    private static int STATE_LOAD_MORE = 0X10;
    private static int STATE_PULL_REFRESH = 0X20;
    private static int state = -1;

    private UnReadMsgListAdapter mAdapter;

    @Override
    protected UnReadMsgListPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new UnReadMsgListPresenter(this);
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_un_read_msg);
        initData();
    }

    @Override
    protected void initUI() {

        tvBack.setVisibility(View.VISIBLE);

        mDataList.setLayoutManager(new LinearLayoutManager(this));
        mDataList.setLoadingMoreProgressStyle(ProgressStyle.BallScaleMultiple);
        mDataList.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mDataList.setLoadingMoreEnabled(false);
        mDataList.setAdapter(mAdapter = new UnReadMsgListAdapter(this));

        //点击ItemView
        mAdapter.setOnItemViewClickListener(new AbstractRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {

                MsgEntity msg = mAdapter.getItem(position);
                if (msg.getIsLook() == 0) {
                    mPresenter.UpdateMsgLook(msg);
                }
            }
        });
    }

    private void initData() {

        msgType = getIntent().getIntExtra("msgType", 0);
        msgName = getIntent().getStringExtra("msgName");
        tvTitle.setText(msgName);

        mDataList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_PULL_REFRESH;
                page = 0;
                mPresenter.ByMsgType(UserCache.get().getUserID(), msgType, page);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.ByMsgType(UserCache.get().getUserID(), msgType, page);

            }
        });
        mDataList.setRefreshing(true);
    }


    @Override
    public void MsgUnLook(PageEntity<MsgEntity> msgTypeEs) {
    }

    @Override
    public void showUnReadMsgList(PageEntity<MsgEntity> pageEntity) {
        if (state == STATE_PULL_REFRESH) {
            page = 0;
            mAdapter.setData(pageEntity.getRows());
        } else if (state == STATE_LOAD_MORE) {
            mAdapter.insert(mAdapter.getItemCount(), pageEntity.getRows());
        }
        //原数据
        mAdapter.sourceItems.addAll(pageEntity.getRows());
        ++page;
    }

    @Override
    public void showUpdateLockResult(MsgEntity msgLog) {
        mAdapter.notifyDataSetChanged();
        //通知界面更新
        Log.d(TAG, "通知界面更新...");
        EventBus.getDefault().post(new UnReadMsgItemEvent());
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void hideProgress() {
        if (mDataList != null) {
            if (state == STATE_PULL_REFRESH) {
                mDataList.refreshComplete();
            } else if (state == STATE_LOAD_MORE) {
                mDataList.loadMoreComplete();
            }
            if (mPresenter.pageEntity != null && !mPresenter.pageEntity.getRows().isEmpty()) {
                mDataList.setLoadingMoreEnabled(true);
                if (mAdapter.sourceItems.size() >= mPresenter.pageEntity.getTotal()) {
                    mDataList.setNoMore(true);
                } else {
                    mDataList.setLoadingMoreEnabled(true);
                    mDataList.setNoMore(false);
                }
            } else {
                mDataList.setLoadingMoreEnabled(false);
            }
        }
    }


    @OnClick(R.id.tv_back)
    public void onClick(View v) {
        this.finish();
    }

}
