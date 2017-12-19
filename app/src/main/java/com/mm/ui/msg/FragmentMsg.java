package com.mm.ui.msg;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.DateUtil;
import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.Logs;
import com.mm.MApplication;
import com.mm.R;
import com.mm.base.BaseMvpFragment;
import com.mm.contract.msg.IUnReadMsgListView;
import com.mm.data.PageEntity;
import com.mm.data.cache.MessageTypeCache;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.MsgEntity;
import com.mm.data.entity.MsgTypeEntity;
import com.mm.data.event.UnReadMsgItemEvent;
import com.mm.presenter.msg.UnReadMsgListPresenter;
import com.mm.ui.adapter.MessageListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;


/**
 * Company：苗苗
 * Class Describe： 消息
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class FragmentMsg extends BaseMvpFragment<UnReadMsgListPresenter> implements IUnReadMsgListView, AbstractRecyclerViewAdapter.OnItemViewClickListener {

    public static String TAG = FragmentMsg.class.getName();
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
     * 数据列表
     */
    @BindView(R.id.dataList)
    RecyclerView mDataList;


    @BindView(R.id.headerLayout)
    RelativeLayout HeaderLayout;

    /**
     * 适配器
     */
    private MessageListAdapter mAdapter;

    @Override
    protected UnReadMsgListPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new UnReadMsgListPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        return view;
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("消息");
        mDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MessageListAdapter(getActivity());
        mDataList.setAdapter(mAdapter);
        mAdapter.setOnItemViewClickListener(this);

        mAdapter.setData(MessageTypeCache.getMsgTypeBeans());

        if (UserCache.get() != null)
            mPresenter.MsgUnLook(UserCache.get().getUserID());
    }

    public void onViewClick(View itemView, int position) {

        MsgTypeEntity msgTypeEntity = mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("msgName", msgTypeEntity.getMsgName());
        bundle.putInt("msgType", msgTypeEntity.getMsgType());
        JumpUtil.overlay(getActivity(), MessageTypeCache.getMsgTypeView(msgTypeEntity.getMsgType() + ""), bundle);

    }

    @Override
    public void MsgUnLook(PageEntity<MsgEntity> msgTypeEs) {

        MessageTypeCache.initBizTypeItems(MApplication.getAppContext());
        List<MsgTypeEntity> msgTypeEntities = MessageTypeCache.getMsgTypeBeans();
        //其他业务提醒数
        for (int i = 0; i < msgTypeEntities.size(); i++) {
            MsgTypeEntity typeEntity = msgTypeEntities.get(i);
            for (int j = 0; j < msgTypeEs.getRows().size(); j++) {
                if (msgTypeEs.getRows().get(j).getMsgType() == typeEntity.getMsgType()) {
                    typeEntity.totalNum += 1;
                    typeEntity.title = msgTypeEs.getRows().get(j).getContent();
                    if (msgTypeEs.getRows().get(j).getCTime() != null) {
                        String time = msgTypeEs.getRows().get(j).getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");
                        typeEntity.recTime = DateUtil.dateToStr(DateUtil.DF_YYYY_MM_DD_HH_MM_SS, DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))));
                    }
                }
            }
        }

        mAdapter.setData(msgTypeEntities);

    }

    @Override
    public void showUnReadMsgList(PageEntity<MsgEntity> pageEntity) {

    }

    @Override
    public void showUpdateLockResult(MsgEntity msgLog) {

    }

    /**
     * 推送消息事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nReadMsgItemEvent(UnReadMsgItemEvent event) {
        Logs.d(TAG, "into nReadMsgItemEvent");
        mPresenter.MsgUnLook(UserCache.get().getUserID());

    }

    @Override
    public void showProgress(String message) {
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
