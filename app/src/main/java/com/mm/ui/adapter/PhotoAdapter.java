package com.mm.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.utils.JumpUtil;
import com.mm.MApplication;
import com.mm.R;
import com.mm.base.CommonHolder;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.entity.FindEntity;
import com.mm.ui.WebPhotoActivity;
import com.mm.ulits.FileUtils;

import butterknife.BindView;


/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class PhotoAdapter extends BaseRecyclerAdapter<FindEntity> {
    @Override
    public CommonHolder<FindEntity> setViewHolder(ViewGroup parent) {
        return new CardHolder(parent.getContext(), parent);
    }

    class CardHolder extends CommonHolder<FindEntity> {

        @BindView(R.id.tv_info)
        TextView tv_info;

        @BindView(R.id.iv_pic)
        ImageView iv_pic;

        public CardHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.item_photo);
        }

        @Override
        public void bindData(final FindEntity photo) {
            tv_info.setText(photo.getTitle());
            GlideShowImageUtils.displayNetImage(MApplication.getAppContext(),
                    RetrofitApiFactory.BASE_URL + photo.getTitle(), iv_pic, R.mipmap.default_error);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String imageUrl[] = new String[photo.getImgList().size()];
                    for (int i = 0, j = photo.getImgList().size(); i < j; i++) {
                        imageUrl[i] = RetrofitApiFactory.BASE_URL + photo.getImgList().get(i).getPath();
                    }

                    Bundle b = new Bundle();
                    b.putStringArray("imageUrls", imageUrl);
                    b.putString("curImageUrl", photo.getTitle());
                    int size = FileUtils.findSize(imageUrl, RetrofitApiFactory.BASE_URL + photo.getTitle());
                    if (size == -1) {
                        return;
                    }


                    JumpUtil.overlay(MApplication.getAppContext(), WebPhotoActivity.class, b, Intent.FLAG_ACTIVITY_NEW_TASK);
                }


            });
        }
    }
}