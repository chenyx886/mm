package com.mm.data;

import android.os.Environment;

import java.io.File;

/**
 * 类描述：常量数据定义
 * 创建人：毛化磊
 * 创建时间：2016/11/5 11:13
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class Constant {
    /**
     * 项目文件缓存文件夹
     */
    private final static String FILE_PATH = "mm";

    /**
     * 得到当前外部存储设备的目录
     */
    public static final String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + File.separator;

    /**
     * 压缩图片缓存文件夹
     */
    public static final String COMPRESS_IMAGE_CACHE_DIR = "/compress_images";


    /**
     * 相机权限
     */
    public static final int CAMERA_RQESTCODE = 0x13;

    /**
     * 位置权限
     */
    public static final int POSTION_RQESTCODE = 0x14;


    /**
     * 微信分享
     */
    public static final String AppID = "wx71185e5be1ac5843";

}
