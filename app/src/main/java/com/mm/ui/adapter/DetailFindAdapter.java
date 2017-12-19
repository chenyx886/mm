package com.mm.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.utils.DateUtil;
import com.chenyx.libs.utils.JumpUtil;
import com.mm.R;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.BaseEntity;
import com.mm.data.entity.CommentEntity;
import com.mm.data.entity.FindEntity;
import com.mm.data.entity.ImageEntity;
import com.mm.ui.WebPhotoActivity;
import com.mm.ui.mine.UserInfoActivity;
import com.mm.ulits.ScreenTools;
import com.mm.widget.CustomImageView;
import com.mm.widget.NineGridlayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Company：苗苗
 * Class Describe：发现详情 适配器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class DetailFindAdapter extends AbstractRecyclerViewAdapter<BaseEntity> {

    /**
     * 发现 信息
     */
    private static int BANNER_TYPE = 0;

    /**
     * 回复 数据列表
     */
    private static int PLAY_TYPE = 1;


    public DetailFindAdapter(Context context) {
        super(context);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_layout, parent, false);
            return new FindViewHolder(view);
        }
        //回复列表
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_find_layout, parent, false);
        return new CommentFindListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof FindViewHolder) {
            FindViewHolder vHolder = (FindViewHolder) holder;
            final FindEntity item = (FindEntity) items.get(position);

            if (item.getImgList().isEmpty() && item.getImgList().size() == 0) {
                vHolder.ivMore.setVisibility(View.GONE);
                vHolder.ivOne.setVisibility(View.GONE);
            } else if (item.getImgList().size() == 1) {
                vHolder.ivMore.setVisibility(View.GONE);
                vHolder.ivOne.setVisibility(View.VISIBLE);
                handlerOneImage(vHolder, item.getImgList().get(0));
            } else {
                vHolder.ivMore.setVisibility(View.VISIBLE);
                vHolder.ivOne.setVisibility(View.GONE);
                vHolder.ivMore.setImagesData(item.getImgList());
            }
            vHolder.mAuthor.setText(!TextUtils.isEmpty(item.getNickName()) ? item.getNickName() : item.getNickName());
            vHolder.mContent.setText(item.getContent());
            vHolder.mLastNum.setText(item.getCNum() + "");

            String time = item.getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");
            vHolder.item_time.setText(DateUtil.showTime(DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))), null));
            GlideShowImageUtils.displayCircleNetImage(mContext, RetrofitApiFactory.BASE_URL + item.getHeadImg(), vHolder.mUserIco,
                    R.mipmap.iv_header_resource_ico);

            vHolder.ivOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageUrl[] = new String[item.getImgList().size()];
                    for (int i = 0; i < item.getImgList().size(); i++) {
                        imageUrl[i] = RetrofitApiFactory.BASE_URL + item.getImgList().get(i).getPath();
                    }

                    Bundle b = new Bundle();
                    b.putStringArray("imageUrls", imageUrl);
                    b.putString("curImageUrl", RetrofitApiFactory.BASE_URL + item.getImgList().get(0).getPath());
                    JumpUtil.overlay(mContext, WebPhotoActivity.class, b, Intent.FLAG_ACTIVITY_NEW_TASK);

                }
            });
        } else {
            CommentFindListViewHolder vHolder = (CommentFindListViewHolder) holder;
            final CommentEntity item = (CommentEntity) getItem(position);
            vHolder.mRealname.setText(!TextUtils.isEmpty(item.getNickName()) ? item.getNickName() : item.getPhone());
            vHolder.mTime.setText("2017-09-23 18:34");
            vHolder.mContent.setText(item.getContent());


            if (item.getRInfo() != null && item.getRInfo().size() > 0) {
                vHolder.mCommentDetail.setVisibility(View.VISIBLE);
                vHolder.mName.setText(item.getRInfo().get(0).getNickName());
                vHolder.mCommentNum.setText("共" + item.getRInfo().size() + "条回复");
            } else {
                vHolder.mCommentDetail.setVisibility(View.GONE);
            }
            GlideShowImageUtils.displayCircleNetImage(mContext,
                    RetrofitApiFactory.BASE_URL + item.getHeadImg(), vHolder.mUserIco, R.mipmap.iv_header_resource_ico);

            vHolder.mCommentDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
            vHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewClickListener.onViewClick(v, position);
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
        }
    }

    /**
     * 主信息
     */
    static class FindViewHolder extends RecyclerView.ViewHolder {

        /**
         * 头像
         */
        @BindView(R.id.iv_user_ico)
        ImageView mUserIco;
        @BindView(R.id.iv_ngrid_layout)
        NineGridlayout ivMore;
        @BindView(R.id.iv_oneimage)
        CustomImageView ivOne;
        @BindView(R.id.author)
        TextView mAuthor;
        @BindView(R.id.item_time)
        TextView item_time;
        @BindView(R.id.content)
        TextView mContent;
        /**
         * 评论数量
         */
        @BindView(R.id.tv_lastnum)
        TextView mLastNum;

        public FindViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 回复列表
     */
    static class CommentFindListViewHolder extends RecyclerView.ViewHolder {

        /**
         * 头像
         */
        @BindView(R.id.iv_user_ico)
        ImageView mUserIco;
        /**
         * 真实姓名
         */
        @BindView(R.id.tv_realname)
        TextView mRealname;
        /**
         * 时间
         */
        @BindView(R.id.tv_time)
        TextView mTime;
        /**
         * 内容
         */
        @BindView(R.id.tv_content)
        TextView mContent;
        /**
         * 回复人姓名
         */
        @BindView(R.id.tv_name)
        TextView mName;
        /**
         * 回复条数
         */
        @BindView(R.id.tv_comment_num)
        TextView mCommentNum;
        /**
         * 回复 统计数据
         */
        @BindView(R.id.comment_detail)
        LinearLayout mCommentDetail;

        public CommentFindListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void handlerOneImage(FindViewHolder viewHolder, ImageEntity image) {
        int totalWidth;
        int imageWidth;
        int imageHeight;
        ScreenTools screentools = ScreenTools.instance(mContext);
        totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
        imageWidth = screentools.dip2px(150);
        imageHeight = screentools.dip2px(180);
        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }
        ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();
        layoutparams.height = imageHeight;
        layoutparams.width = imageWidth;
        viewHolder.ivOne.setLayoutParams(layoutparams);
        viewHolder.ivOne.setClickable(true);
        viewHolder.ivOne.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
        viewHolder.ivOne.setImageUrl(image.getPath());


    }

}
