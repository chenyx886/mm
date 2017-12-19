package com.mm.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.utils.DateUtil;
import com.mm.R;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.CommentEntity;

import java.util.List;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */


public class CommentAdapter extends BaseQuickAdapter<CommentEntity, BaseViewHolder> {
    public CommentAdapter(List<CommentEntity> list) {
        super(R.layout.item_comment, list);
    }

    @Override
    protected void convert(BaseViewHolder vHolder, CommentEntity item) {

        String time = item.getCTime().replace("Date", "").replace("/", "").replace("(", "").replace(")", "");
        vHolder.setText(R.id.tv_realname, !TextUtils.isEmpty(item.getNickName()) ? item.getNickName() : item.getPhone())
                .setText(R.id.tv_content, item.getContent())
                .setText(R.id.tv_time, DateUtil.showTime(DateUtil.parseDate(DateUtil.formatDateTime(Long.parseLong(time))), null))
                .addOnClickListener(R.id.iv_user_ico);
        GlideShowImageUtils.displayCircleNetImage(mContext, RetrofitApiFactory.BASE_URL + item.getHeadImg(),
                (ImageView) vHolder.getView(R.id.iv_user_ico), R.mipmap.iv_header_resource_ico);
        //获取当前条目position
        int position = vHolder.getLayoutPosition();
        if (position == 1)
            vHolder.setVisible(R.id.tv_title_name, true);
    }


}


