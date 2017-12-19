package com.mm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.AbstractRecyclerViewAdapter;
import com.chenyx.libs.utils.JumpUtil;
import com.mm.R;
import com.mm.data.cache.UserCache;
import com.mm.ui.LoginActivity;
import com.mm.ui.mine.MyCollectionActivity;
import com.mm.ui.mine.MyFindActivity;
import com.mm.ui.mine.MyIntegralActivity;
import com.mm.ui.mine.SignListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Company：苗苗
 * Class Describe：我的 我的文章 我的发现 我的消息 我的积分 适配器
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class PersonaltemAdapter extends AbstractRecyclerViewAdapter<String> {


    private final String[] tvs = {"我的发现", "我的积分", "我的收藏", "签到记录"};

    private final int[] ic = {R.mipmap.ic_notice_notify, R.mipmap.ic_notice_notify,
            R.mipmap.ic_notice_notify, R.mipmap.ic_notice_notify};


    @Override
    public int getItemCount() {
        return ic.length;
    }

    public PersonaltemAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_personal_layout, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            ApplicationViewHolder appHolder = ((ApplicationViewHolder) holder);
            appHolder.tvName.setText(tvs[position]);
            appHolder.ivImage.setImageResource(ic[position]);
            JumpAcivity(position, appHolder);
        }
    }

    class ApplicationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView ivImage;

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.ll_items_application)
        LinearLayout mLinearLayout;

        public ApplicationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 跳转页面
     *
     * @param pos
     */
    private void JumpAcivity(int pos, ApplicationViewHolder appHolder) {
        switch (pos) {

            case 0:
                appHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCache.get() != null) {
                            JumpUtil.overlay(mContext, MyFindActivity.class);
                        } else {
                            JumpUtil.overlay(mContext, LoginActivity.class);
                        }
                    }
                });
                break;

            case 1:
                appHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCache.get() != null) {
                            JumpUtil.overlay(mContext, MyIntegralActivity.class);
                        } else {
                            JumpUtil.overlay(mContext, LoginActivity.class);
                        }
                    }
                });
                break;

            case 2:
                appHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCache.get() != null) {
                            JumpUtil.overlay(mContext, MyCollectionActivity.class);
                        } else {
                            JumpUtil.overlay(mContext, LoginActivity.class);
                        }
                    }
                });
                break;

            case 3:
                appHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCache.get() != null) {
                            JumpUtil.overlay(mContext, SignListActivity.class);
                        } else {
                            JumpUtil.overlay(mContext, LoginActivity.class);
                        }
                    }
                });
                break;

        }
    }

}
