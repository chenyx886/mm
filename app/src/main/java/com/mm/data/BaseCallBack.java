package com.mm.data;

import android.net.ParseException;

import com.chenyx.libs.utils.Logs;
import com.chenyx.libs.utils.ToastUtils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Company：苗苗
 * Class Describe：错误逻辑onFailure的判断
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public abstract class BaseCallBack<T> extends Subscriber<T> {


    private static final String TAG = "BaseCallBack";

    /**
     * Unauthorized/未授权
     */
    private static final int UNAUTHORIZED = 401;

    /**
     * Forbidden/禁止
     */
    private static final int FORBIDDEN = 403;

    /**
     * Not Found/未找到
     */
    private static final int NOT_FOUND = 404;

    /**
     * Method Not Allowed/方法未允许
     */
    private static final int METHOD_NOT_ALLOWED = 405;

    /**
     * Request Timeout/请求超时
     */
    private static final int REQUEST_TIMEOUT = 408;

    /**
     * Internal Server Error/内部服务器错误
     */
    private static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Bad Gateway/错误的网关
     */
    private static final int BAD_GATEWAY = 502;

    /**
     * Service Unavailable/服务无法获得
     */
    private static final int SERVICE_UNAVAILABLE = 503;

    /**
     * Gateway Timeout/网关超时
     */
    private static final int GATEWAY_TIMEOUT = 504;


    @Override
    public void onError(Throwable e) {

        if (e != null) {
            Logs.d(TAG, "into onError()" + e.getMessage());
            e.printStackTrace();
        }


        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {

                //未授权
                case UNAUTHORIZED:
                    ex = new ApiException(UNAUTHORIZED, ApiConstant.UNAUTHORIZED);
                    onError(ex);
                    break;

                //请求方法不允许
                case METHOD_NOT_ALLOWED:
                    ex = new ApiException(METHOD_NOT_ALLOWED, ApiConstant.METHOD_NOT_ALLOWED);
                    onError(ex);
                    break;

                //禁止访问
                case FORBIDDEN:
                    ex = new ApiException(FORBIDDEN, ApiConstant.FORBIDDEN);
                    onError(ex);
                    break;

                //无法找到资源
                case NOT_FOUND:
                    ex = new ApiException(NOT_FOUND, ApiConstant.NOT_FOUND);
                    onError(ex);
                    break;

                //请求超时
                case REQUEST_TIMEOUT:
                    ex = new ApiException(REQUEST_TIMEOUT, ApiConstant.REQUEST_TIMEOUT);
                    onError(ex);
                    break;

                //服务器内部错误
                case INTERNAL_SERVER_ERROR:
                    ex = new ApiException(INTERNAL_SERVER_ERROR, ApiConstant.INTERNAL_SERVER_ERROR);
                    onError(ex);
                    break;

                //网络请求错误
                case GATEWAY_TIMEOUT:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    ex = new ApiException(1, ApiConstant.BAD_NET_WORK);
                    onError(ex);
                    break;
                default:
                    break;
            }
        }

        //数据解析出错
        else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(0, ApiConstant.PARSE_ERROR);
            onError(ex);
        }  else if (e instanceof ConnectException) {
            ex = new ApiException(0, ApiConstant.NETWORD_ERROR);
            onError(ex);
        } else if (e instanceof IOException) {
            ex = new ApiException(0, ApiConstant.BAD_NET_WORK);
            onError(ex);
        } else {
            ex = new ApiException(0, ApiConstant.UNKNOW_ERROR);
            onError(ex);
        }
    }


    protected void onError(String msg) {
        ToastUtils.showShort(msg);
    }

    protected void onError(ApiException ex) {
        ToastUtils.showShort(ex.getMsg());
    }


    protected void onFailure(BaseData response) {
        //TOKEN失效发通知
        onError(response.getMsg());
    }
}