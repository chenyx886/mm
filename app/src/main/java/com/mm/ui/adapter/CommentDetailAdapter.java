package com.mm.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.utils.DateUtil;
import com.chenyx.libs.utils.JumpUtil;
import com.mm.R;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.BaseEntity;
import com.mm.data.entity.CommentEntity;
import com.mm.ui.mine.UserInfoActivity;
import com.mm.widget.MListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Company：苗苗
 * Class Describe：二级回复详情 适配器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class CommentDetailAdapter extends AbstractRecyclerViewAdapter<BaseEntity> {

    /**
     * 评论 主信息
     */
    private static int BANNER_TYPE = 0;

    /**
     * 回复 数据列表
     */
    private static int PLAY_TYPE = 1;

    private onItemAdapaterClick onItemClick;

    public interface onItemAdapaterClick {
        void onItemViewClick(final int position);
    }

    public CommentDetailAdapter(Context context) {
        super(context);
    }

    public void setOnItemClick(onItemAdapaterClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER_TYPE;
        }
        return PLAY_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //主信息
        if (viewType == BANNER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_detail_conent, parent, false);
            return new CommentFindViewHolder(view);
        }
        //回复列表
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relpy_textview, parent, false);
        return new RelpyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);


        if (holder instanceof CommentFindViewHolder) {
            CommentFindViewHolder vHolder = (CommentFindViewHolder) holder;
            final CommentEntity item = (CommentEntity) getItem(position);
            vHolder.mRealname.setText(!TextUtils.isEmpty(item.getNickName()) ? item.getNickName() : item.getPhone());
            String time = item.getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");
            vHolder.mTime.setText(DateUtil.showTime(DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))), null));
            vHolder.mContent.setText(item.getContent());
            GlideShowImageUtils.displayCircleNetImage(mContext, RetrofitApiFactory.BASE_URL + item.getHeadImg(), vHolder.mUserIco, R.mipmap.iv_header_resource_ico);
            vHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemViewClick(position);
                }
            });
            vHolder.mUserIco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("UserID", item.getUserID());
                    JumpUtil.overlay(mContext, UserInfoActivity.class, bundle);
                }
            });
        } else {
            RelpyViewHolder vHolder = (RelpyViewHolder) holder;
            final CommentEntity item = (CommentEntity) getItem(position);
            vHolder.mContent.setText(Html.fromHtml("<font color='#305e9b'>" + item.getNickName() + "</font> " + "@<font color='#305e9b'>" + item.getBRNickName() + "</font>: " + item.getContent()));
            vHolder.mContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }

    }


    /**
     * 评论主信息
     */
    static class CommentFindViewHolder extends RecyclerView.ViewHolder {

        /**
         * 回复时间
         */
        @BindView(R.id.tv_time)
        TextView mTime;
        /**
         * 头像
         */
        @BindView(R.id.iv_user_ico)
        ImageView mUserIco;
        /**
         * 姓名
         */
        @BindView(R.id.tv_realname)
        TextView mRealname;
        /**
         * 回复内容
         */
        @BindView(R.id.tv_content)
        TextView mContent;
        /**
         * 互回列表
         */
        @BindView(R.id.ml_content)
        MListView mListView;

        public CommentFindViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 回复列表
     */
    static class RelpyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView mContent;

        public RelpyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
