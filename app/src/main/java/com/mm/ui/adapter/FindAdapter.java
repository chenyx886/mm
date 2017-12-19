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
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.picasso.PicassoLoader;
import com.chenyx.libs.utils.DateUtil;
import com.chenyx.libs.utils.JumpUtil;
import com.mm.R;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.FindEntity;
import com.mm.data.entity.ImageEntity;
import com.mm.ui.WebPhotoActivity;
import com.mm.ulits.ScreenTools;
import com.mm.widget.CustomImageView;
import com.mm.widget.EasyJCVideoPlayer;
import com.mm.widget.NineGridlayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Company：苗苗
 * Class Describe： 发现适配器
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FindAdapter extends AbstractRecyclerViewAdapter<FindEntity> {

    public FindAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_find_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder != null) {
            ViewHolder vHolder = (ViewHolder) holder;
            if (position == 0) {
                vHolder.mFindImg.setVisibility(View.VISIBLE);
            } else {
                vHolder.mFindImg.setVisibility(View.GONE);
            }
            final FindEntity item = items.get(position);

            //视频类型
            if (item.getType() == 2) {
                vHolder.mVideoPlayer.setVisibility(View.VISIBLE);
                setPlayer(vHolder.mVideoPlayer, item);
            } else {
                vHolder.mVideoPlayer.setVisibility(View.GONE);
            }

            if (item.getImgList().isEmpty() && item.getImgList().size() == 0) {
                vHolder.ivMore.setVisibility(View.GONE);
                vHolder.ivOne.setVisibility(View.GONE);
            } else if (item.getImgList().size() == 1 && item.getType() == 1) {
                vHolder.ivMore.setVisibility(View.GONE);
                vHolder.ivOne.setVisibility(View.VISIBLE);
                handlerOneImage(vHolder, item.getImgList().get(0));
            } else if (item.getType() == 1) {
                vHolder.ivMore.setVisibility(View.VISIBLE);
                vHolder.ivOne.setVisibility(View.GONE);
                vHolder.ivMore.setImagesData(item.getImgList());
            }
            vHolder.mAuthor.setText(!TextUtils.isEmpty(item.getNickName()) ? item.getNickName() : item.getNickName());
            vHolder.mContent.setText(item.getContent());
            vHolder.mLastNum.setText(item.getCNum() + "");


            String time = item.getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");
            vHolder.item_time.setText(DateUtil.showTime(DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))), null));

            vHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewClickListener.onViewClick(v, position);
                }
            });
            GlideShowImageUtils.displayCircleNetImage(mContext, RetrofitApiFactory.BASE_URL + item.getHeadImg(), vHolder.mUserIco, R.mipmap.ic_launcher);

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

        }
    }

    private void handlerOneImage(ViewHolder viewHolder, ImageEntity image) {
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


    private void setPlayer(EasyJCVideoPlayer videoPlayer, FindEntity find) {
        PicassoLoader.displayImage(mContext, find.getContent(), videoPlayer.thumbImageView, R.mipmap.default_error);
        videoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        videoPlayer.titleTextView.setText(find.getTitle());
        videoPlayer.setDurationText(find.getContent() + "");

        if (find.getImgList() != null && find.getImgList().size() > 0) {
            videoPlayer.setUp(RetrofitApiFactory.BASE_URL + find.getImgList().get(0).getPath(), JCVideoPlayer.SCREEN_LAYOUT_LIST, find.getContent());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 视频
         */
        @BindView(R.id.videoPlayer)
        EasyJCVideoPlayer mVideoPlayer;
        /**
         * 头部背景图
         */
        @BindView(R.id.find_img)
        ImageView mFindImg;
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
