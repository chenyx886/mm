package com.chenyx.libs.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * @author 陈永祥
 * @Description: 图片加载工具
 * @date 2016年6月8日 下午8:58:20
 */
public class GlideShowImageUtils {

    /**
     * 通过文件路径加载本地图片
     *
     * @param imagePath
     * @param mImageView
     */
    public static void displayNativeImage(Context context, String imagePath, ImageView mImageView, int defaultImg) {
        Glide.with(context).load(new File(imagePath)).placeholder(defaultImg).crossFade().thumbnail(0.1f).into(mImageView);
    }

    /**
     * 显示手机本地圆形图片
     *
     * @param context
     * @param imagePath
     * @param mImageView
     * @param defaultImg
     */
    public static void displayCircleNativeImage(Context context, String imagePath, ImageView mImageView, int defaultImg) {
        Glide.with(context).load(new File(imagePath)).placeholder(defaultImg)
                .transform(new GlideCircleTransform(context)).crossFade().thumbnail(0.1f).into(mImageView);
    }

    /**
     * 显示网络图片
     *
     * @param imageUrl
     * @param mImageView
     * @param defaultImg
     */
    public static void displayNetImage(Context context, String imageUrl, ImageView mImageView, int defaultImg) {
        Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defaultImg).crossFade()
                .thumbnail(0.1f).into(mImageView);
    }

    /**
     * 显示网络圆形图片
     *
     * @param context
     * @param imageUrl
     * @param mImageView
     * @param defaultImg
     */
    public static void displayCircleNetImage(Context context, String imageUrl, ImageView mImageView, int defaultImg) {
        Glide.with(context).load(imageUrl).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(defaultImg)
                .transform(new GlideCircleTransform(context)).crossFade().thumbnail(0.1f).into(mImageView);
    }

    /**
     * 显示网络图片 圆角
     *
     * @param imageUrl
     * @param mImageView
     * @param defaultImg 默认图片
     * @param Displayer  圆角大小
     */
    public static void displayNetImage(Context context, String imageUrl, ImageView mImageView, int defaultImg, int Displayer) {
        Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(context, Displayer)).placeholder(defaultImg).crossFade()
                .thumbnail(0.1f).into(mImageView);
    }
}
