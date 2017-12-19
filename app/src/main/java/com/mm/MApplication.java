package com.mm;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.chenyx.libs.app.AppManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mm.data.cache.MessageTypeCache;
import com.mm.data.gen.DaoMaster;
import com.mm.data.gen.DaoSession;
import com.tencent.smtt.sdk.QbSdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cn.jpush.im.android.api.JMessageClient;


/**
 * Company：苗苗
 * Class Describe：APP 应用
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MApplication extends Application {

    private static MApplication instance;

    /**
     * 线程池
     */
    private ExecutorService backgroundExecutor;

    private final Handler handler;

    /**
     * App Activity 自定义栈管理
     */
    public AppManager appManager;
    /**
     * 数据库 管理 Master
     */
    public DaoMaster daoMaster;

    /**
     * 数据库管理Session
     */
    public DaoSession daoSession;
    /**
     * 上传位置信息Service
     */
    public static boolean uploadClosing = false;


    public MApplication() {
        super();
        instance = this;
        handler = new Handler();
        backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Background executor service");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });

    }


    public static MApplication getInstance() {
        if (instance == null)
            throw new IllegalStateException();
        return instance;
    }

    public static Context getAppContext() {
        if (instance == null)
            throw new IllegalStateException();
        return instance.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appManager = AppManager.getAppManager();
        setupDatabase();
        initMsgTypeData();
        SDKInitializer.initialize(getApplicationContext());

        JMessageClient.setDebugMode(true);// 设置开启日志,发布时请关闭日志
        JMessageClient.init(getApplicationContext(), true); // 初始化 JMessage

        InitX5();
        //初始化fresco
        Fresco.initialize(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


    /**
     * 业务消息数据初始化
     */
    private void initMsgTypeData() {

        MessageTypeCache.initMsgMapView();
        MessageTypeCache.initBizTypeItems(getApplicationContext());
    }

    /**
     * 数据库初始化
     */
    private void setupDatabase() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mm-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }


    private void InitX5() {

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean b) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("MApplication", "加载内核是否成功:" + b);
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("MApplication", "加载内核完成");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * Submits request to be executed in background.
     *
     * @param runnable
     */
    public void runInBackground(final Runnable runnable) {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public boolean uploadIsClosing() {
        return uploadClosing;
    }

    /**
     * UploadLocationService 关闭时调用
     */
    public void onUploadServiceDestroy() {
        uploadClosing = true;
    }

    /**
     * UploadLocationService 关闭时调用
     */
    public void onUploadServiceCreate() {
        uploadClosing = false;
    }

}