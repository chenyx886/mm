package com.mm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.DateUtil;
import com.mm.R;
import com.mm.data.entity.SignEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Company：苗苗
 * Class Describe：签到记录
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class SignListAdapter extends AbstractRecyclerViewAdapter<SignEntity> {

    public SignListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sign_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);


        if (holder != null) {
            ViewHolder vHolder = (ViewHolder) holder;
            SignEntity item = getItem(position);

            vHolder.tvTitle.setText(item.getAdderss());

            String time = item.getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");

            vHolder.tvTime.setText(DateUtil.showTime(DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))), null));

        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        /**
         * 标题
         */
        @BindView(R.id.tv_title)
        TextView tvTitle;

        /**
         * 时间
         */
        @BindView(R.id.tv_time)
        TextView tvTime;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
