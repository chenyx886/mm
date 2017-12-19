package com.mm.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.base.AbstractBaseAdapter;
import com.chenyx.libs.glide.GlideShowImageUtils;
import com.mm.R;
import com.mm.data.entity.ImageEntity;


/**
 * Company：苗苗
 * Class Describe：拍照时 得到的列表适配器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class ChoicePicAdapter extends AbstractBaseAdapter<ImageEntity> {


    public static interface DeleteImgInterface {
        public void show(int position);
    }

    /**
     * @param context
     */
    private DeleteImgInterface mDeleteImgInterface;


    public ChoicePicAdapter(Context context) {
        super(context);
    }

    public ChoicePicAdapter(Context context, DeleteImgInterface deleteImgInterface) {
        super(context);
        this.mDeleteImgInterface = deleteImgInterface;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(mContext, R.layout.item_pic_choice, null);
        ImageView choicePic =  convertView.findViewById(R.id.choicePic);
        ImageView deleteImg =   convertView.findViewById(R.id.deleteImg);

        if (getItem(position).isAddItem()) {
            choicePic.setImageResource(R.mipmap.ic_pic_add);
            deleteImg.setVisibility(View.GONE);
        } else {
//            if (getItem(position).isSelected()) {
                deleteImg.setVisibility(View.VISIBLE);
//            } else {
//                deleteImg.setVisibility(View.GONE);
//            }
            GlideShowImageUtils.displayNativeImage(mContext, getItem(position).getPath(),
                    choicePic, R.drawable.bg_default_pic);
        }
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteImgInterface != null) {
                    mDeleteImgInterface.show(position);
                }
            }
        });
        return convertView;
    }
}
