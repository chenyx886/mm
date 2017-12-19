package com.mm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.chenyx.libs.utils.Apps;
import com.chenyx.libs.utils.DateUtil;
import com.chenyx.libs.utils.Logs;
import com.chenyx.libs.utils.WakeLockUtils;
import com.location.LocationInterface;
import com.location.LocationService;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.cache.DBHelper;
import com.mm.data.cache.PrefCache;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.LocationEnitity;
import com.mm.data.gen.UserCoorEntity;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Company：苗苗
 * Class Describe： 上传位置信息到服务器
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */

public class UploadLocationService extends Service implements LocationInterface {

    private static final String TAG = "UploadLocationService";
    private static final String UPLOAD_WAKE = "upload_wake";
    private static long LOCATION_DELAY = 12 * 60 * 1000;
    /**
     * 定位操作类
     */
    private LocationService locationService;
    /**
     * 定时定位
     */
    private HandlerThread sendLocatioinHT;
    /**
     * 是否在上传缓存的位置信息
     */
    private boolean isUploadCache;
    private Handler mHandler;
    private BroadcastReceiver connectivityReceiver;
    private PhoneStateChangeListener phoneStateListener;
    private TelephonyManager telephonyManager;

    public UploadLocationService() {
        super();
        connectivityReceiver = new ConnectivityReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, UploadLocationService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "into onCreate()");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        locationService = new LocationService(this);
        locationService.registerLocationInterface(this);
        registerConnectivityReceiver();
        WakeLockUtils.getInstance().acquire(getApplicationContext(), this, UPLOAD_WAKE);
        MApplication.getInstance().onUploadServiceCreate();
        sendLocatioinHT = new HandlerThread("send-location");
        sendLocatioinHT.start();
        mHandler = new Handler(sendLocatioinHT.getLooper()) {
            @Override
            public void handleMessage(Message msg) {

                Logs.d(TAG, "into handleMessage()");

                Log.d(TAG, DateUtil.getCurrentDateHHMMSS());

                // 获取当前的网络时间
                Date cdate = getNetWorkDate();

                // 本地手机系统时间
                Date date = DateUtil.gainCurrentDate();

                if (cdate != null) {
                    Log.d(TAG, "当前网络时间 ：" + DateUtil.dateToStr(cdate));
                    date = cdate;
                }
                String startDateStr = (String) PrefCache.getData("startDateStr", "");

                // 定位起始时间
                Date startDate = null;
                if (!TextUtils.isEmpty(startDateStr)) {
                    startDate = DateUtil.strToDate(DateUtil.DF_HH_MM, startDateStr);
                    startDate.setYear(date.getYear());
                    startDate.setMonth(date.getMonth());
                    startDate.setDate(date.getDate());
                }

                // 定位结束时间
                Date endDate = null;
                String endDateStr = (String) PrefCache.getData("endDateStr", "");
                if (!TextUtils.isEmpty(endDateStr)) {
                    endDate = DateUtil.strToDate(DateUtil.DF_HH_MM, endDateStr);
                    endDate.setYear(date.getYear());
                    endDate.setMonth(date.getMonth());
                    endDate.setDate(date.getDate());
                }

                if (startDate != null && endDate != null) {
                    Log.d(TAG, "定位开始时间 ：" + DateUtil.dateToStr(startDate));
                    Log.d(TAG, "定位结束时间 ：" + DateUtil.dateToStr(endDate));
                    if (date.getTime() - startDate.getTime() > 0) {
                        if (date.getTime() - endDate.getTime() < 0) {
                            // 定位操作
                            Log.d(TAG, "定位操作");
                            location();
                            // 检查本地缓存位置信息，上传服务器
                            if (!isUploadCache) {
                                checkCacheLocation();
                            }
                        }
                    }
                } else {
                    // 定位操作
                    location();
                    // 检查本地缓存位置信息，上传服务器
                    if (!isUploadCache) {
                        checkCacheLocation();
                    }
                }
                Log.d(TAG, "sendEmptyMessageDelayed()");
                mHandler.sendEmptyMessageDelayed(0, LOCATION_DELAY);

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "into onStartCommand()");
        try {
            LOCATION_DELAY = Long.valueOf(PrefCache.getData("TimeInterval", "").toString()) * 1000;
        } catch (NumberFormatException e) {

        }
        // 启动定位
        mHandler.sendEmptyMessage(0);

        // 当服务被异常终止时，会重启服务
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MApplication.getInstance().onUploadServiceDestroy();
        WakeLockUtils.getInstance().release(UPLOAD_WAKE);
        unregisterConnectivityReceiver();

        // 重启Service
        startService(createIntent(MApplication.getInstance().getApplicationContext()));
        Log.e(TAG, "into onDestroy()");
    }

    /**
     * 获取网络时间
     *
     * @return
     */
    private Date getNetWorkDate() {

        Log.d(TAG, "into getNetWorkDate()");
        URL url;
        Date date = null;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect(); // 发出连接
            long ld = uc.getDate(); // 取得网站日期时间
            date = new Date(ld); // 转换为标准时间对象
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "out getNetWorkDate()");
        return date;
    }

    /**
     * 定位成功
     */
    @Override
    public void onReLocationSucess(LocationEnitity location) {

        // 停止定位
        locationService.stop();
        Log.d(TAG, "into onReLocationSucess()");

        if (UserCache.get() != null) {
            // 标识位置信息正常
            location.setStatus("N");
            //设置用户ID
            location.setUserId(UserCache.get().getUserID());
            //设置设备ID
            location.setDevId(Apps.getDeviceId(MApplication.getInstance().getApplicationContext()));

            // 上传位置信息到服务器
            uploadLocation(location);
        }
        Log.d(TAG, "location :" + location.toString());

    }

    /**
     * 定位失败
     */
    @Override
    public void onReLocationFail(String mesSage) {

        // 停止定位
        locationService.stop();
        Log.d(TAG, "into onReLocationFail()");
        LocationEnitity userCoorReportEnitity = new LocationEnitity();
        userCoorReportEnitity.setLctTime(DateUtil.dateToStr(DateUtil.DF_YYYY_MM_DD_HH_MM_SS, new Date()));

        if (UserCache.get() != null) {
            // 标识位置信息异常
            userCoorReportEnitity.setStatus("E");
            //设置用户ID
            userCoorReportEnitity.setUserId(UserCache.get().getUserID());
            //设置设备ID
            userCoorReportEnitity.setDevId(Apps.getDeviceId(MApplication.getInstance().getApplicationContext()));
            // 上传位置信息到服务器:异常位置信息
            uploadLocation(userCoorReportEnitity);
        }
    }

    /**
     * 启动百度定位服务
     */
    private void location() {

        Log.d(TAG, "into location()");
        if (locationService != null) {
            locationService.start();
        }
    }

    /**
     * 数据库缓存位置信息实体转为 后台服务坐标实体
     *
     * @param EmpCoorEntity 数据库缓存实体
     * @param urp           服务器坐标实体
     */
    private void UE2URP(UserCoorEntity EmpCoorEntity, LocationEnitity urp) {

        urp.setUserId(EmpCoorEntity.getUserId());
        urp.setDevId(EmpCoorEntity.getDevId());
        urp.setAddress(EmpCoorEntity.getAddress());
        urp.setCity(EmpCoorEntity.getCity());
        urp.setCityCode(EmpCoorEntity.getCityCode());
        urp.setProvince(EmpCoorEntity.getProvince());
        urp.setDistrict(EmpCoorEntity.getDistrict());
        urp.setLat(EmpCoorEntity.getLat());
        urp.setLng(EmpCoorEntity.getLng());
        urp.setLctTime(EmpCoorEntity.getLctTime());
        urp.setStatus(EmpCoorEntity.getStatus());
        urp.setNetOprt(EmpCoorEntity.getNetOprt());
        urp.setStreet(EmpCoorEntity.getStreet());
        urp.setStreetnumber(EmpCoorEntity.getStreetnumber());

    }

    /**
     * 后台服务坐标实体转为数据库缓存位置信息实体
     *
     * @param EmpCoorEntity 数据库缓存实体
     * @param urp           服务器坐标实体
     */
    private void URP2UE(UserCoorEntity EmpCoorEntity, LocationEnitity urp) {

        EmpCoorEntity.setUserId(urp.getUserId());
        EmpCoorEntity.setDevId(urp.getDevId());
        EmpCoorEntity.setAddress(urp.getAddress());
        EmpCoorEntity.setCity(urp.getCity());
        EmpCoorEntity.setCityCode(urp.getCityCode());
        EmpCoorEntity.setProvince(urp.getProvince());
        EmpCoorEntity.setDistrict(urp.getDistrict());
        EmpCoorEntity.setLat(urp.getLat());
        EmpCoorEntity.setLng(urp.getLng());
        EmpCoorEntity.setStatus(urp.getStatus());
        EmpCoorEntity.setLctTime(urp.getLctTime());
        EmpCoorEntity.setNetOprt(urp.getNetOprt());
        EmpCoorEntity.setStreet(urp.getStreet());
        EmpCoorEntity.setStreetnumber(urp.getStreetnumber());

    }

    /**
     * 上传位置信息到服务器
     */
    private void uploadLocation(final LocationEnitity item) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS);

        OkHttpClient mOkHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiFactory.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//对转换后的数据进行再包装
                .build();

//        retrofit.create(UserApi.class)
//                .insertNormalCoord(item)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BaseData>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // 缓存位置信息
//                        Log.d(TAG, "缓存位置信息");
//                        Log.d(TAG, "location :" + item.toString());
//                        UserCoorEntity EmpCoorEntity = new UserCoorEntity();
//                        URP2UE(EmpCoorEntity, item);
//                        EmpCoorEntity.setUpload(false);
//                        cacheUnLoadLocation(EmpCoorEntity);
//                    }
//
//                    @Override
//                    public void onNext(BaseData data) {
//                        Log.d(TAG, "insertCoord 执行成功");
//                        if (data.getResult() == 1) {
//                            // 缓存位置信息
//                            Log.d(TAG, "缓存位置信息");
//                            Log.d(TAG, "location :" + item.toString());
//                            UserCoorEntity EmpCoorEntity = new UserCoorEntity();
//                            URP2UE(EmpCoorEntity, item);
//                            cacheUnLoadLocation(EmpCoorEntity);
//
//                        }
//                    }
//                });
//        Log.d(TAG, "上传位置信息");


    }

    /**
     * 上传本地 缓存位置信息到后台服务器
     */
    private void insertErrorCoords() {

        Log.d(TAG, "上传缓存位置信息");
        final List<UserCoorEntity> empCoorEntities =
                DBHelper.getInstance(MApplication.getInstance()).queryUserCoorPage(10, UserCache.get().getUserID());
        Log.d(TAG, "cache coor sieze :" + empCoorEntities.size());
        List<LocationEnitity> userCoorReportEnitities = new ArrayList<>();
        for (UserCoorEntity empCoorEntity : empCoorEntities) {
            LocationEnitity userCoorReportEnitity = new LocationEnitity();
            UE2URP(empCoorEntity, userCoorReportEnitity);
            userCoorReportEnitities.add(userCoorReportEnitity);
        }


        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS);

        OkHttpClient mOkHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiFactory.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//对转换后的数据进行再包装
                .build();

//        retrofit.create(UserApi.class)
//                .insertErrorCoord(userCoorReportEnitities)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BaseData>() {
//                    @Override
//                    public void onCompleted() {
//                        isUploadCache = false;
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        isUploadCache = false;
//                    }
//
//                    @Override
//                    public void onNext(BaseData data) {
//                        if (data.getResult() == 1) {
//                            Log.d(TAG, "缓存位置信息上传成功");
//
//                            MApplication.getInstance().runInBackground(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // 删除本地 缓存的位置信息
//                                    Log.d(TAG, "删除本地缓存位置信息");
//                                    Log.d(TAG, "coor size :" + empCoorEntities.size());
//                                    for (int i = 0; i < empCoorEntities.size(); i++) {
//                                        Log.d(TAG, "i :" + i);
//                                        DBHelper.getInstance(MApplication.getInstance()).deleteUserCoors(empCoorEntities.get(i));
//                                    }
//                                }
//                            });
//                        } else {
//                            Log.d(TAG, "缓存位置信息上传成功");
//                        }
//                    }
//                });


    }

    /**
     * 检查本地是否存在缓存位置数据
     */
    private void checkCacheLocation() {

        Logs.d(TAG, "是否存在缓存位置数据");
        boolean hasUserCoorCahche = DBHelper.getInstance(MApplication.getInstance()).hasUserCoorCahche();
        if (hasUserCoorCahche && UserCache.get() != null) {
            insertErrorCoords();
        }
    }

    /**
     * 缓存未上传的位置信息
     */
    private void cacheUnLoadLocation(final UserCoorEntity coorEntity) {

        MApplication.getInstance().runInBackground(new Runnable() {

            @Override
            public void run() {
                DBHelper.getInstance(MApplication.getInstance()).inertUserCoor(coorEntity);
            }
        });
    }

    /**
     * 注册手机网络连接状态广播
     */
    private void registerConnectivityReceiver() {

        Log.d(TAG, "registerConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);

    }

    /**
     * 取消注册手机网络连接状态广播
     */
    private void unregisterConnectivityReceiver() {

        Log.d(TAG, "unregisterConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);

    }
}
