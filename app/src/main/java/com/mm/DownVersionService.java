package com.mm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Company：苗苗
 * Class Describe： App版本升级Service
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class DownVersionService extends Service {

    private static final String TAG = "DownVersionService";

    public static final String DOWNURL = "downUrl";

    /* 下载中 */
    private static final int DOWNLOAD = 1;

    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    /* 下载失败 */
    private static final int DOWNLOAD_FAIL = 3;

    /* 下载保存路径 */
    private String mSavePath;

    /* 下载 保存 的文件名 */
    private String fileName = "gywgj.apk";

    /* 记录进度条数量 */
    private int progress;

	/* 是否取消更新 */

    private String downUrl;

    private static final int notifyId = 0x4534;

    private Notification notify;

    private boolean isStopUpdate;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "into onStartCommand()");
        if (intent != null) {
            downUrl = intent.getStringExtra(DOWNURL);
            if (createApkFile() != null) {
                initNotification(0, 100);
                new downloadApkThread().start();
                new Thread(new VersionRunnable()).start();
            } else {
                Toast.makeText(getApplicationContext(), "文件创建失败，无法进行升级", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "downUrl " + downUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 创建Apk升级文件
     *
     * @return
     */
    private File createApkFile() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 获得存储卡的路径
            String sdpath = Environment.getExternalStorageDirectory() + "/";
            mSavePath = sdpath + "download";
            File file = new File(mSavePath);
            // 判断文件目录是否存在
            if (!file.exists()) {
                file.mkdir();
            }
            File apkFile = new File(mSavePath, fileName);
            return apkFile;
        }
        return null;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                // 正在下载
                case DOWNLOAD:

                    Log.d(TAG, "下载进度更新");
                    int count = msg.arg1;
                    int length = msg.arg2;
                    progress = (int) ((double) count / (double) length * 100);
                    Log.d(TAG, "progress :" + progress);
                    // updateNotification(progress, 100);
                    break;

                // 安装文件
                case DOWNLOAD_FINISH:
                    Log.d(TAG, "下载进完成");
                    setClickNotify();
                    installApk();
                    isStopUpdate = true;
                    // 下载完成，结束Service
                    stopSelf();
                    break;

                // 下载失败
                case DOWNLOAD_FAIL:
                    File apkFile = new File(mSavePath, fileName);
                    if (apkFile != null && apkFile.exists()) {
                        apkFile.delete();
                    }
                    isStopUpdate = true;
                    // 下载失败，结束Service
                    stopSelf();
                    Toast.makeText(DownVersionService.this, "文件下载失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * @author 毛华磊
     * @ClassName: downloadApkThread
     * @Description: 文件下载线程
     * @date 2016年6月24日 上午11:33:19
     */
    private class downloadApkThread extends Thread {

        @Override
        public void run() {

            URL url = null;

            HttpURLConnection conn = null;

            InputStream is = null;

            OutputStream os = null;

            try {

                url = new URL(downUrl);

                // 创建连接
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                // 获取文件大小
                int length = conn.getContentLength();
                Log.d(TAG, "length " + length);

                // 创建输入流
                is = conn.getInputStream();

                File apkFile = new File(mSavePath, fileName);
                os = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                int len = 0;

                // 写入到文件中
                while ((len = is.read(buf)) != -1) {

                    count += len;

                    // 写入文件
                    os.write(buf, 0, len);

                    // 计算进度条位置
                    // Message msg = mHandler.obtainMessage();
                    // msg.what = DOWNLOAD;
                    // msg.arg1 = count;
                    // msg.arg2 = length;

                    // 更新进度
                    // mHandler.sendMessageDelayed(msg, 200);
                    progress = (int) ((double) count / (double) length * 100);
                    if (count >= length) {
                        break;
                    }
                }
                // 下载完成
                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {

        Intent intent = getInstallIntent();
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 获取安装Apk的Intent
     *
     * @return
     */
    private Intent getInstallIntent() {

        File apkfile = new File(mSavePath, fileName);
        if (!apkfile.exists()) {
            return null;
        }
        Uri uri = Uri.fromFile(apkfile);
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");// 设置intent的数据类型
        return installIntent;

    }

    /**
     * 通知栏初始化
     */
    private void initNotification(int max, int progress) {

        // 通知栏配置
        NotificationCompat.Builder mBuilder = new Builder(this);
        mBuilder.setContent(initRemoteViews(max, progress)).setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示.setPriority(Notification.PRIORITY_DEFAULT)//
                .setOngoing(true).setSmallIcon(R.mipmap.ic_launcher);
        notify = mBuilder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;

        // 显示通知栏
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyId, notify);

    }

    /**
     * 初始化后台下载界面
     *
     * @param max
     * @param progress
     * @return
     */
    private RemoteViews initRemoteViews(int max, int progress) {

        // 先设定RemoteViews
        RemoteViews view_version = new RemoteViews(getPackageName(), R.layout.verson_update_layout);

        // 设置对应IMAGEVIEW的ID的资源图片
        view_version.setImageViewResource(R.id.versionIcon, R.mipmap.ic_launcher);

        // 标题
        view_version.setTextViewText(R.id.versionTitle, "行政执法");

        // 进度显示
        view_version.setTextViewText(R.id.versionProgressText, "0%");

        // 进度条显示
        view_version.setProgressBar(R.id.versionProgressBasr, max, progress, false);
        return view_version;
    }

    /**
     * 通知栏进度更新
     *
     * @param bytesWritten
     * @param totalSize
     */
    private void updateNotification(int bytesWritten, int totalSize) {

        if (totalSize != 0) {

            String progressText = (int) ((double) bytesWritten / (double) totalSize * 100) + "%";

            // 更新进度
            RemoteViews contentView = notify.contentView;
            contentView.setTextViewText(R.id.versionProgressText, progressText);
            contentView.setProgressBar(R.id.versionProgressBasr, totalSize, bytesWritten, false);

            // 显示通知栏
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyId, notify);
        }
    }

    /**
     * 设置通知栏点击安装
     */
    private void setClickNotify() {

        // 显示通知栏
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, getInstallIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
        notify.contentIntent = contentIntent;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyId, notify);
    }

    /**
     * 新建线程刷新通知栏（如果在 主线程里面刷新的话，会出现界面卡死的问题）
     *
     * @author mao
     */
    class VersionRunnable implements Runnable {

        public VersionRunnable() {

        }

        @Override
        public void run() {

            while (true) {
                try {
                    updateNotification(progress, 100);
                    // 停止显示进度
                    if (isStopUpdate) {
                        break;
                    }
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
