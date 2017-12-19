package com.mm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.DateUtil;
import com.mm.R;
import com.mm.data.entity.MsgTypeEntity;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Company：苗苗
 * Class Describe：消息列表数据适配器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MessageListAdapter extends AbstractRecyclerViewAdapter<MsgTypeEntity> {

    public MessageListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder != null) {
            ViewHolder vHolder = (ViewHolder) holder;
            MsgTypeEntity item = getItem(position);

            vHolder.tvMsgTitle.setText(item.msgName);
            vHolder.tvMsgDetail.setText(item.title);
            vHolder.imgMsgIcon.setImageResource(item.resId);

            //消息提醒时间
            if (!TextUtils.isEmpty(item.getRecTime())) {
                Date date = DateUtil.strToDate(DateUtil.DF_YYYY_MM_DD_HH_MM_SS, getItem(position).recTime);
                vHolder.tvMsgTime.setVisibility(View.VISIBLE);
                vHolder.tvMsgTime.setText(DateUtil.showTime(date, null));
            } else {
                vHolder.tvMsgTime.setVisibility(View.GONE);
            }

            //消息未读数
            if (item.totalNum > 0) {
                vHolder.tvUnRead.setVisibility(View.VISIBLE);
                String unReadCount = item.totalNum > 99 ? "99+" : getItem(position).totalNum + "";
                vHolder.tvUnRead.setText(unReadCount + "");
            } else {
                vHolder.tvUnRead.setVisibility(View.GONE);
            }

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 消息类型Icon
         */
        @BindView(R.id.img_msg_icon)
        ImageView imgMsgIcon;

        /**
         * 未读数
         */
        @BindView(R.id.tv_unRead)
        TextView tvUnRead;

        /**
         * 标题
         */
        @BindView(R.id.tv_msg_title)
        TextView tvMsgTitle;

        /**
         * 时间
         */
        @BindView(R.id.tv_msg_time)
        TextView tvMsgTime;

        /**
         * 消息内容
         */
        @BindView(R.id.tv_msg_detail)
        TextView tvMsgDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
