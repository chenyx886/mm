package com.mm.ulits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.chenyx.libs.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 描述：图片保存工具类
 * 创建人：陈永祥
 * 创建时间：2017-07-23
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class FileUtils {


    public static int findSize(String[] imageUrl, String currentUrl) {
        for (int i = 0; i < imageUrl.length; i++) {
            if (imageUrl[i].equals(currentUrl)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 保存图片
     *
     * @param context
     * @param bmp
     * @param saveResultCallback
     */
    public static void savePhoto(final Context context, final Bitmap bmp, final SaveResultCallback saveResultCallback) {
        final File sdDir = getSDPath();
        if (sdDir == null) {
            ToastUtils.showShort("设备自带的存储不可用");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(sdDir, "news_photo");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }

                //设置以当前时间格式为图片名称
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String fileName = df.format(new Date()) + ".png";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess(file);
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }

    /**
     * 获取跟目录
     *
     * @return
     */
    public static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;
    }


    /**
     * 回调函数
     */
    public interface SaveResultCallback {
        /**
         * 保存成功
         *
         * @param file
         */
        void onSavedSuccess(File file);

        /**
         * 保存失败
         */
        void onSavedFailed();
    }
}
