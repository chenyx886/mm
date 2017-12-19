package com.mm.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TextView;

import com.mm.R;


/**
 * Company：苗苗
 * Class Describe：异步请求加载框
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MLoadingDialog {
    private static Dialog mDialog;
    private static TextView mInfo;
    /**
     * 显示加载框
     */
    public static void show(Activity context) {
        show(context, "", true);
    }

    /**
     * 显示加载框
     */
    public static void show(Activity context, String info) {
        show(context, info, true);
    }

    /**
     * @param context
     * @param info
     * @param listener
     */
    public static void show(Activity context, String info, DialogInterface.OnCancelListener listener) {
        show(context, info, true, listener);
    }

    /**
     * 显示加载框
     */
    public static void show(Activity context, String info, boolean cancelable) {
        show(context, info, cancelable, null);
    }

    /**
     * @param context
     * @param info
     * @param cancelable
     * @param listener
     */
    public static void show(Activity context, String info, boolean cancelable, DialogInterface.OnCancelListener listener) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        mDialog = new Dialog(context, R.style.showDialog);
        mDialog.setContentView(context.getLayoutInflater().inflate(R.layout.dialog_loading_layout, null));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(cancelable);
        mDialog.setOnCancelListener(listener);
        mInfo = mDialog.findViewById(R.id.tv_msg);
        if (!TextUtils.isEmpty(info)) {
            mInfo.setText(info);
        }
        mDialog.show();
    }

    /**
     * 对话框是否显示
     *
     * @return
     */
    public static boolean isShow() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 隐藏加载框
     */
    public static void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

}
