package com.mm.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chenyx.libs.fresco.FrescoUtil;
import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.utils.DateUtil;
import com.chenyx.libs.utils.JumpUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.R;
import com.mm.data.PageEntity;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.ArticleEntity;
import com.mm.data.entity.BaseEntity;
import com.mm.data.entity.ImageEntity;
import com.mm.ui.news.HtmlActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Company：苗苗
 * Class Describe：首页数据适配器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class NewsAdapter extends AbstractRecyclerViewAdapter<BaseEntity> {

    /**
     * 轮播
     */
    private static int BANNER_TYPE = 0;

    /**
     * 数据列表
     */
    private static int PLAY_TYPE = 1;


    /**
     * 广播索引值
     */
    private int bannerIndex;

    /**
     * 时间间隔值
     */
    private long scrollDuration = 4000;

    public NewsAdapter(Context context) {
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

        //轮播
        if (viewType == BANNER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
            return new HomeBannerViewHolder(view);
        }

        //数据列表
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_layout, parent, false);
        return new NewsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        //轮播
        if (holder instanceof HomeBannerViewHolder) {
            final PageEntity<ArticleEntity> pageEntity = (PageEntity<ArticleEntity>) getItem(position);
            HomeBannerViewHolder homeBannerViewHolder = (HomeBannerViewHolder) holder;
            if (pageEntity.getRows().size() > 0) {
                homeBannerViewHolder.mCNews.stopTurning();
                homeBannerViewHolder.mCNews.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, pageEntity.getRows()).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focus})
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                homeBannerViewHolder.mCNews.setCanLoop(true);
                homeBannerViewHolder.mCNews.setcurrentitem(bannerIndex);
                homeBannerViewHolder.mCNews.startTurning(scrollDuration);

                homeBannerViewHolder.mCNews.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        ArticleEntity item = pageEntity.getRows().get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("url", RetrofitApiFactory.BASE_URL + "/App/GetHtml?NodeID=" + item.getNodeID() + "&ArticleID=" + item.getArticleID());
                        JumpUtil.overlay(mContext, HtmlActivity.class, bundle);

                    }
                });
            } else {
                homeBannerViewHolder.mCNews.setVisibility(View.GONE);
            }
        }


        //新闻
        else {

            NewsListViewHolder vHolder = (NewsListViewHolder) holder;
            if (getItem(position) != null) {
                final ArticleEntity item = (ArticleEntity) getItem(position);
                vHolder.mTitle.setText(item.getArticleTitle());

                String time = item.getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");

                vHolder.mTime.setText(DateUtil.formatDateTime(Long.parseLong(time)));

                vHolder.mllThree.setVisibility(View.GONE);
                vHolder.mRightImg.setVisibility(View.GONE);

                List<ImageEntity> imgList = ((ArticleEntity) getItem(position)).getImgList();
                if (imgList.size() > 0) {

                    if (imgList.size() == 1) {
                        vHolder.mRightImg.setVisibility(View.VISIBLE);
                        vHolder.mllThree.setVisibility(View.GONE);
                        FrescoUtil.loadGifPicOnNet(vHolder.mRightImg, RetrofitApiFactory.BASE_URL + imgList.get(0).getPath());

                    } else if (imgList.size() >= 2) {
                        vHolder.mllThree.setVisibility(View.VISIBLE);
                        vHolder.mRightImg.setVisibility(View.GONE);
                        FrescoUtil.loadGifPicOnNet(vHolder.mImgOne, RetrofitApiFactory.BASE_URL + imgList.get(0).getPath());
                        FrescoUtil.loadGifPicOnNet(vHolder.mImgTwo, RetrofitApiFactory.BASE_URL + imgList.get(1).getPath());
                        if (imgList.size() >= 3)
                            FrescoUtil.loadGifPicOnNet(vHolder.mImgThree, RetrofitApiFactory.BASE_URL + imgList.get(2).getPath());
                    }
                }


                vHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("ArticleID", item.getArticleID());
                        bundle.putString("url", RetrofitApiFactory.BASE_URL + "/App/GetHtml?NodeID="
                                + item.getNodeID() + "&ArticleID=" + item.getArticleID());
                        JumpUtil.overlay(mContext, HtmlActivity.class, bundle);
                    }
                });
            }
        }
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof HomeBannerViewHolder) {
            HomeBannerViewHolder homeBannerViewHolder = (HomeBannerViewHolder) holder;
            bannerIndex = homeBannerViewHolder.mCNews.getCurrentItem();
            homeBannerViewHolder.mCNews.stopTurning();
        }
    }


    public class NetworkImageHolderView implements Holder<ArticleEntity> {
        private View view;

        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.banner_item, null, false);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, ArticleEntity data) {
            ((TextView) view.findViewById(R.id.tv_title)).setText(data.getArticleTitle());
            ImageView imageView = view.findViewById(R.id.iv_image);
            if (data.getImgList() != null && data.getImgList().size() > 0)
                GlideShowImageUtils.displayNetImage(context, RetrofitApiFactory.BASE_URL + data.getImgList().get(0).getPath(), imageView, R.mipmap.default_img);
        }


    }

    static class HomeBannerViewHolder extends RecyclerView.ViewHolder {

        /**
         * 轮播组件
         */
        @BindView(R.id.cb_news)
        ConvenientBanner mCNews;

        public HomeBannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    static class NewsListViewHolder extends RecyclerView.ViewHolder {

        /**
         * 标题
         */
        @BindView(R.id.tv_title)
        TextView mTitle;

        /**
         * 三张图片
         */
        @BindView(R.id.img_one)
        SimpleDraweeView mImgOne;
        @BindView(R.id.img_two)
        SimpleDraweeView mImgTwo;
        @BindView(R.id.img_three)
        SimpleDraweeView mImgThree;
        @BindView(R.id.ll_three)
        LinearLayout mllThree;
        /**
         * 时间
         */
        @BindView(R.id.tv_time)
        TextView mTime;
        /**
         * 单图
         */
        @BindView(R.id.iv_right_img)
        SimpleDraweeView mRightImg;


        public NewsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
