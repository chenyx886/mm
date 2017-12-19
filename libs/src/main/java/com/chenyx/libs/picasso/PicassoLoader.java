package com.chenyx.libs.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;


public class PicassoLoader {


    public static void displayImage(Context context, int resId, ImageView view, int defaultImg) {
        Picasso.with(context).load(resId).config(Bitmap.Config.RGB_565).placeholder(defaultImg).into(view);
    }

    public static void displayImage(Context context, String uri, ImageView view, int placeholder) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        Picasso.with(context).load(uri).config(Bitmap.Config.RGB_565).placeholder(placeholder).into(view);

    }

    public static void displayImage(Context context, String uri, ImageView view, int placeholder, Callback callback) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        RequestCreator requestCreator = Picasso.with(context).load(uri).config(Bitmap.Config.RGB_565).placeholder(placeholder).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        requestCreator.into(view);
        requestCreator.fetch(callback);
    }

    public static void displayImage(Context context, String uri, ImageView view, Callback callback) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        RequestCreator requestCreator = Picasso.with(context).load(uri).config(Bitmap.Config.RGB_565).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        requestCreator.into(view);
        requestCreator.fetch(callback);
    }

    //图片适配，宽度适配屏幕，高度按比例缩放
    public static void displayImageWidthMatchParent(Context context, String uri, final ImageView view, int defaultImg) {

        if (TextUtils.isEmpty(uri)) {
            return;
        }
        Picasso.with(context)
                .load(uri).config(Bitmap.Config.RGB_565)
                .placeholder(defaultImg)
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {

                        int targetWidth = view.getWidth();

                        if (source.getWidth() == 0 || source.getWidth() == targetWidth) {
                            return source;
                        }

                        //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                        int targetHeight = (int) (targetWidth * aspectRatio);
                        if (targetHeight != 0 && targetWidth != 0) {
                            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                            if (result != source) {
                                // Same bitmap is returned if sizes are the same
                                source.recycle();
                            }
                            return result;
                        } else {
                            return source;
                        }

                    }

                    @Override
                    public String key() {
                        return "transformation" + " desiredWidth";
                    }
                })
                .into(view);
    }
}
