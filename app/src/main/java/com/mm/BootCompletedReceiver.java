package com.mm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.mm.data.cache.UserCache;

/**
 * Company：苗苗
 * Class Describe：开机自启动
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            // 网络连接可用
            if (networkInfo.isConnected()) {
                String userId = UserCache.get().getUserID();
                // 用户的处于登录状态
                if (!TextUtils.isEmpty(userId)) {

                    // 启动位置上传Service
                    if (MApplication.getInstance().uploadIsClosing()) {
                        MApplication.getInstance().startService(UploadLocationService.createIntent(MApplication.getInstance().getApplicationContext()));
                    }
                }
            }
        }
    }
}
