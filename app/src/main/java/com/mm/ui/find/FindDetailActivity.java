package com.mm.ui.find;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.SysConfig;
import com.chenyx.libs.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.find.IFindDetailView;
import com.mm.data.PageEntity;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.BaseEntity;
import com.mm.data.entity.CommentEntity;
import com.mm.data.entity.FindEntity;
import com.mm.presenter.find.FindDetailPresenter;
import com.mm.ui.LoginActivity;
import com.mm.ui.adapter.DetailFindAdapter;
import com.mm.widget.MLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;


/**
 * Company：苗苗
 * Class Describe：发现详情
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class FindDetailActivity extends BaseMvpActivity<FindDetailPresenter> implements IFindDetailView {

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
    /**
     * 数据 列表
     */
    @BindView(R.id.comment_list)
    XRecyclerView mCommentList;
    /**
     * 发布
     */
    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.panel_root)
    KPSwitchFSPanelLinearLayout panelRoot;
    /**
     * 写评论
     */
    @BindView(R.id.send_edt)
    EditText sendEdt;


    /**
     * 主信息
     */
    private FindEntity mFindEntity;

    /**
     * 适配器
     */
    private DetailFindAdapter mAdapter;


    private int sizes = 0;
    private static int state = -1;
    private static int STATE_LOAD_MORE = 0X10;
    private static int STATE_PULL_REFRESH = 0X20;
    public int p = 0;


    private int ArticleID = 0, FindID = 0, CID = 0, PID = 0, UserID = 0, BRUserID = 0;
    private String BRNickName, Content;

    @Override
    protected FindDetailPresenter createPresenter() {
        return new FindDetailPresenter(this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_find_detail);
        setTranslucentStatus(R.color.colorAccent);
    }


    @Override
    protected void initUI() {

        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("详情");
        mBack.setVisibility(View.VISIBLE);
        mFindEntity = (FindEntity) getIntent().getSerializableExtra("findEntity");
        sendEdt.setHint("评论@" + mFindEntity.getNickName());
        FindID = mFindEntity.getFindID();
        BRUserID = mFindEntity.getUserID();
        BRNickName = mFindEntity.getNickName();

        mCommentList.setLayoutManager(new LinearLayoutManager(this));
        mCommentList.setLoadingMoreProgressStyle(ProgressStyle.BallScaleMultiple);
        mCommentList.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mCommentList.setLoadingMoreEnabled(false);

        mAdapter = new DetailFindAdapter(this);
        mCommentList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AbstractRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == 0) {

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("comment", mAdapter.getItem(position));
                    JumpUtil.overlay(FindDetailActivity.this, CommentDetailActivity.class, bundle);
                }

            }
        });
        sendEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    panelRoot.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });
        mCommentList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard();
                    panelRoot.setVisibility(View.GONE);
                }
                return false;
            }
        });
        mAdapter.setOnItemViewClickListener(new AbstractRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                if (position == 0) {
                    FindID = mFindEntity.getFindID();
                    CID = 0;
                    PID = 0;
                    sendEdt.setHint("评论@" + mFindEntity.getNickName());
                    BRUserID = mFindEntity.getUserID();
                    BRNickName = mFindEntity.getNickName();

                } else {
                    FindID = 0;
                    CID = ((CommentEntity) mAdapter.getItem(position)).getID();
                    PID = ((CommentEntity) mAdapter.getItem(position)).getID();

                    BRUserID = ((CommentEntity) mAdapter.getItem(position)).getUserID();
                    BRNickName = ((CommentEntity) mAdapter.getItem(position)).getNickName();

                    sendEdt.setHint("回复@" + ((CommentEntity) mAdapter.getItem(position)).getNickName());

                }
            }
        });
        initData();
    }


    protected void initData() {
        mCommentList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_PULL_REFRESH;
                mPresenter.RelpyList(mFindEntity.getFindID(), 0);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.RelpyList(mFindEntity.getFindID(), p);
            }
        });
        mCommentList.setRefreshing(true);
    }


    @Override
    public void showData(PageEntity<CommentEntity> list) {
        List<BaseEntity> baseEntities = new ArrayList<>();
        if (state == STATE_PULL_REFRESH) {
            p = 0;
            baseEntities.add(mFindEntity);
            for (CommentEntity comment : list.getRows()) {
                baseEntities.add(comment);
            }
            mAdapter.setData(baseEntities);
            mCommentList.setLoadingMoreEnabled(true);
        } else if (state == STATE_LOAD_MORE) {
            baseEntities.addAll(list.getRows());
            mAdapter.insert(mAdapter.getItemCount(), baseEntities);
        }
        ++p;
        sizes = list.getTotal();

    }


    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
        if (state == STATE_PULL_REFRESH) {
            if (mCommentList != null)
                mCommentList.refreshComplete();
        } else if (state == STATE_LOAD_MORE) {
            if (mCommentList != null)
                mCommentList.loadMoreComplete();
        }
        if (mAdapter.getItemCount() >= sizes + 1) {
            if (mCommentList != null) {
                mCommentList.setLoadingMoreEnabled(false);
                mCommentList.setNoMore(true);
            }
        } else {
            if (mCommentList != null)
                mCommentList.setNoMore(false);
        }

    }


    /**
     * 点击操作
     *
     * @param view
     */
    @OnClick({R.id.tv_back, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_send:
                if (UserCache.get() != null) {
                    Content = sendEdt.getText().toString();
                    if (TextUtils.isEmpty(Content)) {
                        ToastUtils.showShort("请说点点什么...");
                        return;
                    }
                    UserID = SysConfig.nullToInt(UserCache.get().getUserID());

                    mPresenter.FindReply(ArticleID, FindID, CID, PID, UserID, BRUserID, BRNickName, Content);

                } else {
                    JumpUtil.overlay(FindDetailActivity.this, LoginActivity.class);
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void AddCommentSuccess() {
        hideKeyboard();
        mCommentList.setRefreshing(true);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sendEdt.clearFocus();
        sendEdt.setText("");
        imm.hideSoftInputFromWindow(sendEdt.getWindowToken(), 0);
        btnSend.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (panelRoot.getVisibility() == View.VISIBLE) {
                panelRoot.setVisibility(View.GONE);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
