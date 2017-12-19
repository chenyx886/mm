package com.location;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mm.data.entity.LocationEnitity;

/**
 * 类描述：
 * 创建人：Chenyx
 * 创建时间：2016/11/9 10:25
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class LocationService {
    private static final String TAG = "LocationService";
    public static double mLongitude;
    public static double mLatitude;
    public String mProvince = "";
    public String mCity = "";
    public String mDistrict = "";
    public String mStreet = "";
    public String mAddress = "";
    private LocationClient client = null;
    private LocationClientOption mOption, DIYoption;
    private Object objLock = new Object();
    private LocationInterface locationInterface;


    /***
     * @param locationContext
     */
    public LocationService(Context locationContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
                registerListener(new LocationListener());
            }
        }
    }

    /**
     * 注册定位监听回调
     *
     * @param locationInterface
     */
    public void registerLocationInterface(LocationInterface locationInterface) {

        if (locationInterface != null) {
            this.locationInterface = locationInterface;
        }
    }

    /***
     * @param listener
     * @return
     */

    private boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption() {
        return DIYoption;
    }

    /***
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(12000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
            mOption.setOpenGps(true);// 可选，默认false,设置是否使用gps
            mOption.setIsNeedLocationDescribe(true);// 可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);// 可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
            mOption.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        }
        return mOption;
    }


    public boolean isLocation() {
        return client != null && client.isStarted();
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    /**
     * 实现实时位置回调监听
     */
    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null == location) {
                if (locationInterface != null) {
                    locationInterface.onReLocationFail("定位失败");
                }
                return;
            }
            LocationInfo loca = new LocationInfo();
            LocationEnitity userCoorReportEnitity = new LocationEnitity();
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            mLatitude = location.getLatitude();
            sb.append(mLatitude);
            sb.append("\nlontitude : ");
            mLongitude = location.getLongitude();
            sb.append(mLongitude);

            mProvince = location.getAddress().province;
            mCity = location.getAddress().city;
            mDistrict = location.getAddress().district;
            mStreet = location.getAddress().street;
            mAddress = location.getAddrStr();

            StringBuilder sbAddress = new StringBuilder();
            if (!TextUtils.isEmpty(mProvince)) {
                sbAddress.append(mProvince);
                // 省份
                userCoorReportEnitity.setProvince(mProvince);
            }
            if (!TextUtils.isEmpty(mCity)) {
                sbAddress.append(mCity);
                // 城市
                userCoorReportEnitity.setCity(mCity);
                userCoorReportEnitity.setCityCode(location.getCityCode());
            }
            if (!TextUtils.isEmpty(mDistrict)) {
                sbAddress.append(mDistrict);
                // 区域
                userCoorReportEnitity.setDistrict(mDistrict);
            }
            if (!TextUtils.isEmpty(mStreet)) {
                sbAddress.append(mStreet);
                // 街道
                userCoorReportEnitity.setStreet(mStreet);
                userCoorReportEnitity.setStreetnumber(location.getStreetNumber());
            }

            // 经纬度
            loca.setLongitude(mLongitude);
            loca.setLatitude(mLatitude);

            // 经纬度
            userCoorReportEnitity.setLng(mLongitude);
            userCoorReportEnitity.setLat(mLatitude);

            // 运营商的信息
            userCoorReportEnitity.setNetOprt(String.valueOf(location.getOperators()));

            // 状态
            userCoorReportEnitity.setStatus("N");

            // 定位时间
            userCoorReportEnitity.setLctTime(location.getTime());

            // 地址
            userCoorReportEnitity.setAddress(mAddress);

            // 地址信息
            loca.setAddress(sbAddress.toString());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(mAddress);
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

                loca.setAddress(mAddress);
                if (locationInterface != null) {
                    locationInterface.onReLocationSucess(userCoorReportEnitity);
                }

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(mAddress);
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());

                // 运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

                loca.setAddress(mAddress);
                if (locationInterface != null) {
                    locationInterface.onReLocationSucess(userCoorReportEnitity);
                }
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                if (locationInterface != null) {
                    locationInterface.onReLocationSucess(userCoorReportEnitity);
                }

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                if (locationInterface != null) {
                    locationInterface.onReLocationFail("服务端网络定位失败");
                }

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                if (locationInterface != null) {
                    locationInterface.onReLocationFail("服务端网络定位失败");
                }
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                if (locationInterface != null) {
                    locationInterface.onReLocationFail("服务端网络定位失败");
                }
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            Log.d(TAG, "sb :" + sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
