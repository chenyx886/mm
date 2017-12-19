package com.mm.ui.find;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.mm.presenter.find.FindDetailPresenter;
import com.mm.ui.LoginActivity;
import com.mm.ui.adapter.CommentDetailAdapter;
import com.mm.widget.MLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;


public class CommentDetailActivity extends BaseMvpActivity<FindDetailPresenter> implements IFindDetailView {
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
    private CommentEntity mComment;

    /**
     * 适配器
     */
    private CommentDetailAdapter mAdapter;

    private int sizes = 0;
    private static int state = -1;
    private static int STATE_LOAD_MORE = 0X10;
    private static int STATE_PULL_REFRESH = 0X20;
    public int p = 1;


    private int ArticleID = 0, FindID = 0, CID = 0, PID = 0, UserID = 0, BRUserID = 0;
    private String BRNickName, Content;

    @Override
    protected FindDetailPresenter createPresenter() {
        return new FindDetailPresenter(this);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_comment_detail);
        setTranslucentStatus(R.color.colorAccent);
    }


    @Override
    protected void initUI() {

        mTitle.setVisibility(View.VISIBLE);

        mBack.setVisibility(View.VISIBLE);
        mComment = (CommentEntity) getIntent().getSerializableExtra("comment");
        mTitle.setText("回复");
        sendEdt.setHint("回复@" + mComment.getNickName());
        PID = mComment.getID();
        BRUserID = mComment.getUserID();
        BRNickName = mComment.getNickName();

        mCommentList.setLayoutManager(new LinearLayoutManager(this));
        mCommentList.setLoadingMoreProgressStyle(ProgressStyle.BallScaleMultiple);
        mCommentList.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mCommentList.setLoadingMoreEnabled(false);

        mAdapter = new CommentDetailAdapter(this);
        mCommentList.setAdapter(mAdapter);

        mAdapter.setOnItemClick(new CommentDetailAdapter.onItemAdapaterClick() {
            @Override
            public void onItemViewClick(int position) {
                sendEdt.setHint("回复@" + mComment.getNickName());
                PID = mComment.getID();
                BRUserID = mComment.getUserID();
                BRNickName = mComment.getNickName();
            }
        });

        mAdapter.setOnItemClickListener(new AbstractRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                PID = ((CommentEntity) mAdapter.getItem(position)).getID();
                BRUserID = ((CommentEntity) mAdapter.getItem(position)).getUserID();
                BRNickName = ((CommentEntity) mAdapter.getItem(position)).getNickName();
                sendEdt.setHint("回复@" + ((CommentEntity) mAdapter.getItem(position)).getNickName());
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
        initData();
    }

    protected void initData() {


        mCommentList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                state = STATE_PULL_REFRESH;
                mPresenter.RelpyPidList(mComment.getID(), 0);
            }

            @Override
            public void onLoadMore() {
                state = STATE_LOAD_MORE;
                mPresenter.RelpyPidList(mComment.getID(), p);
            }
        });
        mCommentList.setRefreshing(true);


    }


    @Override
    public void showData(PageEntity<CommentEntity> list) {
        List<BaseEntity> baseEntities = new ArrayList<>();
        if (state == STATE_PULL_REFRESH) {
            p = 0;
            baseEntities.add(mComment);
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
            //回复评论
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
                    JumpUtil.overlay(CommentDetailActivity.this, LoginActivity.class);
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


}
