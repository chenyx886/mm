package com.mm.data.api;

import android.content.Context;

import com.chenyx.libs.utils.Apps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mm.MApplication;
import com.tamic.novate.Novate;

import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/12/7 下午3:54
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class NovateApi {

    /**
     * 服务器Api地址
     */

    public static String BASE_URL = "http://app.ztmzw.com";

    private static Novate mNovate;

    /**
     * @return
     */
    public static Novate novate(Context context) {
        if (mNovate == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()// json宽松
                    .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                    .serializeNulls() //智能null
                    .setPrettyPrinting()// 调教格式
                    .disableHtmlEscaping() //默认是GSON把HTML 转义的
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();


            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            headers.put("deviceId", Apps.getDeviceId(MApplication.getInstance()));
            headers.put("deviceType", android.os.Build.MODEL);
            headers.put("platform", "android");
            headers.put("osVersion", android.os.Build.VERSION.RELEASE);


            mNovate = new Novate.Builder(context)
                    .connectTimeout(20)
                    .readTimeout(20)
                    .baseUrl(BASE_URL)
                    .addHeader(headers)
                    .addLog(true)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//对转换后的数据进行再包装
                    .build();
        }
        return mNovate;
    }

    public static UserApi getUserApi() {
        return novate(MApplication.getInstance()).create(UserApi.class);
    }

}
