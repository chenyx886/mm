package com.mm.data.api;


import com.chenyx.libs.utils.Apps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mm.MApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 公司：苗苗
 * 类描述：Api 工厂类
 * 创建人：陈永祥
 * 创建时间：2016/10/13 11:30
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class RetrofitApiFactory {

    /**
     * 服务器Api地址
     */

    public static String BASE_URL = "http://app.ztmzw.com";
    /**
     * 代理接口构建类
     */
    public static Retrofit mRetrofit;
    /**
     * Http连接超时
     */
    private static int CONNECT_TIMEOUT = 20;
    /**
     * Http 读取超时
     */
    private static int READ_TIMEOUT = 20;
    /**
     * Http 写入超时
     */
    private static int WRITE_TIMEOUT = 20;
    /**
     * 缓存大小
     */
    private static int CACHE_SIZE = 10 * 1024 * 1024;

    /**
     * @return
     */
    public static Retrofit retrofit() {


        if (mRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()// json宽松
                    .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                    .serializeNulls() //智能null
                    .setPrettyPrinting()// 调教格式
                    .disableHtmlEscaping() //默认是GSON把HTML 转义的
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();


            Retrofit.Builder builder = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//对转换后的数据进行再包装
                    .baseUrl(BASE_URL);
            //日志拦截器
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            httpBuilder.cache(new Cache(MApplication.getAppContext().getCacheDir(), CACHE_SIZE))
                    .addInterceptor(logging)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();

                            //添加通用头部 登录返回TOKEN后要刷新这里
                            Request.Builder builder = request.newBuilder();
                            builder.addHeader("Content-Type", "application/json");
                            builder.addHeader("Accept", "application/json");
                            builder.addHeader("deviceId", Apps.getDeviceId(MApplication.getInstance()));
                            builder.addHeader("deviceType", android.os.Build.MODEL);
                            builder.addHeader("platform", "android");
                            builder.addHeader("osVersion", android.os.Build.VERSION.RELEASE);

                            return chain.proceed(builder.build());
                        }
                    });
            OkHttpClient httpClient = httpBuilder.build();
            builder.client(httpClient);
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }

    public static ArticleApi getArticleApi() {
        return retrofit().create(ArticleApi.class);
    }

    public static NodeApi getNodeApi() {
        return retrofit().create(NodeApi.class);
    }

    public static UserApi getUserApi() {
        return retrofit().create(UserApi.class);
    }

    public static MessageApi getMessageApi() {
        return retrofit().create(MessageApi.class);
    }

}
