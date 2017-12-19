package com.mm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.chenyx.libs.app.AppManager;
import com.chenyx.libs.utils.Logs;
import com.mm.ui.news.HtmlActivity;
import com.mm.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;


/**
 * Company：苗苗
 * Class Describe：广播接收器
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午11:48
 * Update Person：
 * Update Time：
 * Update Remark：
 */


public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String jsonString = "";
            Bundle bundle = intent.getExtras();
            String msg = printBundle(bundle);

            Logs.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + msg);

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logs.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

                //透传消息
                jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (!TextUtils.isEmpty(jsonString)) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Logs.e(TAG, "透传消息:" + jsonObject.toString());
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logs.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logs.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

                if (!TextUtils.isEmpty(jsonString)) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Logs.e(TAG, "下来的通知:" + jsonObject.toString());
                }

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logs.d(TAG, "[MyReceiver] 用户点击打开了通知");
                jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
                if (!TextUtils.isEmpty(jsonString)) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String KeyID = jsonObject.getString("KeyID");
                    String NodeID = jsonObject.getString("NodeID");
                    String Type = jsonObject.getString("Type");

                    processCustomMessage(context, KeyID, NodeID, Type);
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logs.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logs.d(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logs.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            Logs.d(TAG, "[MyReceiver] Unhandled intent - " + e.getMessage());
        }

    }

    private void processCustomMessage(Context context, final String KeyID, final String NodeID, String Type) {


        Context mContext = context;
        //当前 程序 激活，直接进,
        if (AppManager.getActivityCount() > 0) {
            Logs.e("AppManager.getActivityCount()>0");
            mContext = AppManager.currentActivity();
            if (TextUtils.equals(Type, "News")) {
                enterNewsActivity(mContext, KeyID, "http://app.ztmzw.com/App/GetHtml?NodeID=" + NodeID + "&ArticleID=" + KeyID);
            } else if (TextUtils.equals(Type, "Find")) {

            }
        }
        //先到主界面，再打开详情
        else {
            try {
                Logs.e("start main start ");
                Intent mIntent = new Intent(mContext, MainActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(mIntent);
                Logs.e("start main end ");
            } catch (Exception ex) {
                Logs.e(ex.getMessage() + ex.getStackTrace());
            }

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Logs.e("start detail start ");
                    Context theContext = AppManager.currentActivity();
                    enterNewsActivity(theContext, KeyID, "http://app.ztmzw.com/App/GetHtml?NodeID=" + NodeID + "&ArticleID=" + KeyID);
                    Logs.e("start detail end ");
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 1000);//1秒后执行TimeTask的run方法
        }
    }

    private void enterNewsActivity(Context context, String KeyID, String url) {

        if (!TextUtils.isEmpty(url)) {

            Intent mIntent = new Intent(context, HtmlActivity.class);
            mIntent.putExtra("url", url);
            mIntent.putExtra("ArticleID", KeyID);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(mIntent);
        }

    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logs.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logs.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
