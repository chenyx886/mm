package com.chenyx.libs.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类 ycf
 */
public class Toasts {
    public static boolean isShow = true;

    private Toasts() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            showToast(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            showToast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            showToast(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            showToast(context, message, duration);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            showToast(context, message, duration);
    }

    private static Toast mToast = null;

    public static void showToast(Context context, CharSequence message, int duration) {
        if (mToast != null)
            mToast.setText(message);
        else
            mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

    public static void showToast(Context context, int message, int duration) {
        if (mToast != null)
            mToast.setText(message);
        else
            mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }
}
