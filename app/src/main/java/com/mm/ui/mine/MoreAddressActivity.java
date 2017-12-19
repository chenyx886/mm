package com.mm.ui.mine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chenyx.libs.utils.Logs;
import com.mm.R;
import com.mm.base.BaseActivity;
import com.mm.data.entity.LocationEnitity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

/**
 * Company：苗苗
 * Class Describe：选择地址
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MoreAddressActivity extends BaseActivity implements OnClickListener, OnGetSuggestionResultListener,
        OnMapStatusChangeListener, BDLocationListener, OnGetGeoCoderResultListener {

    public static String ADDRESS = "";

    public static String CITY = "";

    public static double LON;

    public static double LAT;

    public static final int REQUESTCODE = 0;

    private ImageView iv_left;
    protected static final String TAG = "MoreAddressActivity";
    private ListView lv_near_address;
    private SuggestionSearch mSuggestionSearch = null;
    private BaiduMap mBaiduMap = null;
    /**
     * 搜索关键字输入窗口
     */
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<LocationEnitity> sugAdapter = null;
    private PoiSearchAdapter adapter;
    private MapView map;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private GeoCoder geoCoder;
    private boolean isFirstLoc = true;

    public static void showActivityForResult(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, MoreAddressActivity.class);
        context.startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_more_address);
        setTranslucentStatus(R.color.colorAccent);

        ADDRESS = "";
        CITY = "";
        LON = 0.0;
        LAT = 0.0;
    }


    @Override
    protected void initUI() {
        map = (MapView) findViewById(R.id.map);
        mBaiduMap = map.getMap();
        MapStatus mapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        mLocClient = new LocationClient(this);
        // 注册定位监听
        mLocClient.registerLocationListener(this);
        // 定位选项
        LocationClientOption option = new LocationClientOption();
        /**
         * coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系
         * ：bd09ll
         */
        option.setCoorType("bd09ll");
        // 设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        // 设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"，
        // 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        // 设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式 Battery_Saving 低功耗模式 Device_Sensors 仅设备(Gps)模式 Hight_Accuracy
         * 高精度模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置是否打开gps进行定位
        option.setOpenGps(true);
        // 设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setScanSpan(1000 * 60 * 10);
        // 设置 LocationClientOption
        mLocClient.setLocOption(option);
        // 开始定位
        mLocClient.start();
        lv_near_address = (ListView) findViewById(R.id.lv_near_address);
        // 初始化搜索模块，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.et_search);
        sugAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */

        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                String city = cs.toString();
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                Logs.d(TAG, "==" + city);
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(city));


            }
        });
        keyWorldsView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                ADDRESS = sugAdapter.getItem(arg2).getAddress();
                CITY = sugAdapter.getItem(arg2).getCity();
                LAT = sugAdapter.getItem(arg2).getLat();
                LON = sugAdapter.getItem(arg2).getLng();

                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }
        });
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
    }


    protected void initData() {

    }

	/*
     * 接受周边地理位置结果
	 * 
	 * @param poiResult
	 */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        map.onDestroy();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
        map = null;
        mSuggestionSearch.destroy();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;

            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * 沉浸式状态栏
     */
    protected void setTranslucentStatus(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null && info.pt != null && info.pt.latitude > 0 && info.pt.longitude > 0) {
                LocationEnitity s = new LocationEnitity();
                s.setCity(info.city);
                s.setAddress(info.district);
                s.setLat(info.pt.latitude);
                s.setLng(info.pt.longitude);
                sugAdapter.add(s);
            }
        }
        sugAdapter.notifyDataSetChanged();
    }

    class PoiSearchAdapter extends BaseAdapter {

        private Context context;
        private List<PoiInfo> list;
        private ViewHolder holder;

        public PoiSearchAdapter(Context context, List<PoiInfo> appGroup) {
            this.context = context;
            this.list = appGroup;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int location) {
            return list.get(location);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        public void addObject(List<PoiInfo> mAppGroup) {
            this.list = mAppGroup;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_poi_search_item, null);
                holder.mpoi_name = convertView.findViewById(R.id.mpoiNameT);
                holder.mpoi_address = convertView.findViewById(R.id.mpoiAddressT);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mpoi_name.setText(list.get(position).name);
            holder.mpoi_address.setText(list.get(position).address);

            return convertView;
        }

        public class ViewHolder {
            public TextView mpoi_name;// 名称
            public TextView mpoi_address;// 地址

        }

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        // 地图操作的中心点
        LatLng cenpt = mapStatus.target;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // 如果bdLocation为空或mapView销毁后不再处理新数据接收的位置
        if (bdLocation == null || mBaiduMap == null) {
            return;
        }

        // 定位数据
        MyLocationData data = new MyLocationData.Builder()
                // 定位精度bdLocation.getRadius()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bdLocation.getDirection())
                // 经度
                .latitude(bdLocation.getLatitude())
                // 纬度
                .longitude(bdLocation.getLongitude())
                // 构建
                .build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(data);

        // 是否是第一次定位
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
            mBaiduMap.animateMapStatus(msu);
        }


        // 创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        // 发起反地理编码请求(经纬度->地址信息)
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置反地理编码位置坐标
        reverseGeoCodeOption.location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
        geoCoder.reverseGeoCode(reverseGeoCodeOption);

        // 设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {

    }

    // 拿到变化地点后的附近地址
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        final List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        Log.i(TAG, "这里的值:" + poiInfos);
        if (poiInfos != null && !"".equals(poiInfos)) {
            PoiAdapter poiAdapter = new PoiAdapter(MoreAddressActivity.this, poiInfos);
            lv_near_address.setAdapter(poiAdapter);
            lv_near_address.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ADDRESS = poiInfos.get(position).address;
                    CITY = poiInfos.get(position).city;
                    LAT = poiInfos.get(position).location.latitude;
                    LON = poiInfos.get(position).location.longitude;
                    Intent data = new Intent();
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        }
    }

    class PoiAdapter extends BaseAdapter {
        private Context context;
        private List<PoiInfo> pois;
        private LinearLayout linearLayout;

        PoiAdapter(Context context, List<PoiInfo> pois) {
            this.context = context;
            this.pois = pois;
        }

        @Override
        public int getCount() {
            return pois.size();
        }

        @Override
        public Object getItem(int position) {
            return pois.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.locationpois_item, null);
                linearLayout = convertView.findViewById(R.id.locationpois_linearlayout);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.iv_gps.setImageDrawable(getResources().getDrawable(R.mipmap.ic_map_marker1));
                holder.locationpoi_name.setTextColor(Color.parseColor("#FF9D06"));
                holder.locationpoi_address.setTextColor(Color.parseColor("#FF9D06"));
            }
            if (position == 0 && linearLayout.getChildCount() < 2) {
                ImageView imageView = new ImageView(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(32, 32);
                imageView.setLayoutParams(params);
                imageView.setBackgroundColor(Color.TRANSPARENT);
                imageView.setImageResource(R.mipmap.ic_map_marker1);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                linearLayout.addView(imageView, 0, params);
                holder.locationpoi_name.setTextColor(Color.parseColor("#FF5722"));
            }
            PoiInfo poiInfo = pois.get(position);
            holder.locationpoi_name.setText(poiInfo.name);
            holder.locationpoi_address.setText(poiInfo.address);
            return convertView;
        }

        class ViewHolder {
            ImageView iv_gps;
            TextView locationpoi_name;
            TextView locationpoi_address;

            ViewHolder(View view) {
                locationpoi_name = view.findViewById(R.id.locationpois_name);
                locationpoi_address = view.findViewById(R.id.locationpois_address);
                iv_gps = view.findViewById(R.id.iv_gps);
            }
        }
    }
}
