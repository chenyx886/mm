package com.mm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.DateUtil;
import com.mm.R;
import com.mm.data.entity.MsgEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Company：苗苗
 * Class Describe：任务适配器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UnReadMsgListAdapter extends AbstractRecyclerViewAdapter<MsgEntity> implements android.widget.Filterable {

    public List<MsgEntity> sourceItems = new ArrayList<>();

    public UnReadMsgListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unread_msg_layout, parent, false);
        return new MsgViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        if (holder instanceof MsgViewHolder) {
            MsgViewHolder vHolder = (MsgViewHolder) holder;
            vHolder.tvTitle.setText(getItem(position).getTitle());
            vHolder.tvContent.setText(getItem(position).getContent());
            String time = getItem(position).getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");
            vHolder.tvTime.setText(DateUtil.showTime(DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))), null));
            if (position == 0) {
                vHolder.mLineHeight.setVisibility(View.GONE);
            } else {
                vHolder.mLineHeight.setVisibility(View.VISIBLE);
            }

            if (getItem(position).getIsLook() == 1) {
                vHolder.tvUnreadIcon.setVisibility(View.GONE);
            } else {
                vHolder.tvUnreadIcon.setVisibility(View.VISIBLE);
            }
        }

    }


    class MsgViewHolder extends RecyclerView.ViewHolder {
        /**
         * 分割线
         */
        @BindView(R.id.line_height)
        View mLineHeight;
        /**
         * 未读Icon
         */
        @BindView(R.id.tv_unread_icon)
        TextView tvUnreadIcon;

        /**
         * 标题
         */
        @BindView(R.id.tv_title)
        TextView tvTitle;

        /**
         * 内容
         */
        @BindView(R.id.tv_content)
        TextView tvContent;

        /**
         * 时间
         */
        @BindView(R.id.tv_time)
        TextView tvTime;

        /**
         * ItemView
         */
        @BindView(R.id.rl_task_layout)
        RelativeLayout rlTaskLayout;

        public MsgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (List<MsgEntity>) results.values;
            notifyDataSetChanged();
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults filterResults = new FilterResults();
            if (prefix == null || prefix.length() == 0) {
                filterResults.values = new ArrayList<>(sourceItems);
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<MsgEntity> values = new ArrayList<>(sourceItems);
                final int count = values.size();
                final ArrayList<MsgEntity> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    final MsgEntity value = values.get(i);
                    final String valueText = value.toString().toLowerCase();
                    if (valueText.contains(prefixString)) {
                        newValues.add(value);
                    }
                }
                filterResults.values = newValues;
                filterResults.count = newValues.size();
            }
            return filterResults;
        }
    };
}
