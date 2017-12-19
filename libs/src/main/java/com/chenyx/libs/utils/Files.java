package com.chenyx.libs.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 文件工具类
 *
 * @author ycf
 */
public class Files {
    // 获取扩展SD卡设备状态
    private static String SDStateString = Environment.getExternalStorageState();
    // 得到当前外部存储设备的目录
    private static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + SysConfig.PATH_ROOT + File.separator;

    /**
     * 在SD卡上创建文件
     *
     * @param path 目录路径
     * @return
     * @throws IOException
     */
    public static File makeFile(String path) {
        Logs.d("makeFile-" + path);
        if (!isAvailable()) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            try {
                if (path.contains("/")) {
                    String dir = path.substring(0, path.lastIndexOf('/'));
                    makeDir(dir);
                }
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }
        Logs.d("makeFile-" + path);
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dir
     * @return
     */
    public static File makeDir(String dir) {
        Logs.d("makeDir-" + dir);
        if (!isAvailable()) {
            return null;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        Logs.d("makeDir-" + dir);
        return dirFile;
    }

    /***
     * 获取SD卡的剩余容量,单位是Byte
     *
     * @return
     */
    public static long getAvailableSize() {
        if (isAvailable()) {
            // 取得sdcard文件路径
            File pathFile = Environment.getExternalStorageDirectory();
            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
            // 获取SDCard上每个block的SIZE
            long nBlocSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();
            // 计算 SDCard 剩余大小Byte
            long nSDFreeSize = nAvailaBlock * nBlocSize;
            return nSDFreeSize;
        }
        return 0;
    }

    /***
     * 获取SD卡是否可用
     *
     * @return
     */
    public static boolean isAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /***
     * 从sd卡中读取文件，并且以InputStream返回
     *
     * @param path
     * @return
     */
    public static InputStream readISFrom(String path) {
        File file = new File(path);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return is;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 从InputStream中读取文件，并且以字节流返回
     *
     * @param is
     * @return
     */
    public static byte[] readFrom(InputStream is) {
        try {
            byte[] data = new byte[is.available()];
            is.read(data);
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[]{};
    }

    /***
     * 从sd卡中读取文件，并且以字节流返回
     *
     * @param path
     * @return
     */
    public static byte[] readFrom(String path) {
        return readFrom(readISFrom(path));
    }

    /**
     * 将一个byte[]写入到SD卡中
     */
    public static void writeTo(String path, byte[] bytes) {
        OutputStream output = null;
        try {
            int size = bytes.length;
            // 拥有可读可写权限，并且有足够的容量
            if (isAvailable() && size < getAvailableSize()) {
                File file = makeFile(path);
                output = new FileOutputStream(file);
                output.write(bytes);
                output.flush();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static void writeTo(String path, InputStream is) {
        writeTo(path, readFrom(is));
    }

    /***
     * 获取文件的路径
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static String getFilePath(String dir, String fileName) {
        return dir + File.separator + fileName;
    }

    /**
     * 删除单个文件
     *
     * @param file
     */
    public static void deleteSingleFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    public static String getRandomFileName(String fileSuffix) {

        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return rannum + str + "." + fileSuffix;

    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean SDAvailable() {

        return SDStateString.equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 在SD卡上创建文件
     *
     * @param dir      目录路径
     * @param fileName
     * @return
     */
    public static File createFileInSDCard(String dir, String fileName) {
        if (!SDAvailable()) {
            return null;
        }
        File file = null;
        if (creatSDDir(dir) != null) {
            try {
                file = new File(SDCardRoot + dir, fileName);
                if (file.exists()) {
                    return file;
                } else {
                    if (!file.createNewFile()) {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dir
     * @return
     */

    public static File creatSDDir(String dir) {
        if (!SDAvailable()) {
            return null;
        }
        File dirFile = new File(SDCardRoot + dir);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        return dirFile;
    }

    /**
     * 写入文件
     *
     * @param in
     * @param file
     */
    public static void writeFile(InputStream in, File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (file != null && file.exists())
            file.delete();

        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024 * 128];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();

    }

    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }

    public static String FormetFileSize(double fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = "0.0" + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format(fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    public static void openFile(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            String type = getMIMEType(file);
            // 设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            // 跳转
            context.startActivity(intent);
            // Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            Toasts.showShort(context, "sorry附件不能打开，请下载相关软件！");
        }
    }

    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "")
            return type;
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {

            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    // 可以自己随意添加
    private static String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"}, {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"}, {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"}, {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"}, {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, {".xls", "application/vnd.ms-excel"}, {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"},
            {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"}, {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"}, {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"}, {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"}, {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"}, {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"}, {".ppt", "application/vnd.ms-powerpoint"}, {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, {".prop", "text/plain"}, {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"}, {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"}, {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"}, {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".z", "application/x-compress"}, {".zip", "application/x-zip-compressed"}, {"", "*/*"}};


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
