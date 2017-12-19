package com.chenyx.libs.utils;


import android.text.TextUtils;

/**
 * 系统常量配置
 *
 * @author Chenyx
 */
public class SysConfig {

    /**
     * 每一页条数
     */
    public static int rows = 20;


    public static final String PATH_ROOT = "/mm/"; // 根目录
    public static final String PATH_DOWNLOAD = PATH_ROOT + "download/"; // 下载文件


    public static String nullToString(String param) {
        String str = "";
        if (!TextUtils.isEmpty(param)) {
            str = (param == null || param.equals("") || param.equals("null") || param.equals("None")) ? "" : param;
        }
        return str;
    }

    public static int nullToInt(String param) {
        return (param == null || param.equals("") || param.equals("null") || param.equals("None")) ? 0 : Integer.parseInt(param);
    }

    public static double nullToDouble(String param) {
        return (param == null || param.equals("") || param.equals("null") || param.equals("None")) ? 0 : Double.parseDouble(param);
    }

    public static long nullToLong(String param) {
        return (param == null || param.equals("") || param.equals("null") || param.equals("None")) ? 0 : Long.parseLong(param);
    }

    public static float nullToFloat(String param) {
        return (param == null || param.equals("") || param.equals("null") || param.equals("None")) ? 0 : Float.parseFloat(param);
    }

    public static Boolean nullToBoolean(String param) {
        return !(param == null || param.equals("") || param.equals("null") || param.equals("None")) && Boolean.parseBoolean(param);
    }

}
