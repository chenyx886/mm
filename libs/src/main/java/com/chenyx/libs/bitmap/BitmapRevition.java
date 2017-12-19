package com.chenyx.libs.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.chenyx.libs.utils.BitmapHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩操作工具类
 *
 * @author mao
 */
public class BitmapRevition {

    private static final String LOG_TAG = "BitmapRevition";

    private static BitmapRevition instance;

    private Context mContext;

    private BitmapRevition(Context context) {

        this.mContext = context;
    }

    public static BitmapRevition getInstance(Context context) {
        if (instance == null) {
            instance = new BitmapRevition(context);
        }
        return instance;
    }

    public void startCompress(String originalPath, String newFilePath, BitmapRevitionListener mBitmapCompressListener) {

        BitmapCoAysTask coAysTask = new BitmapCoAysTask(mBitmapCompressListener);
        coAysTask.execute(new String[]{originalPath, newFilePath});

    }

    class BitmapCoAysTask extends AsyncTask<String[], String, String> {

        private BitmapRevitionListener mBitmapCompressListener;

        public BitmapCoAysTask(BitmapRevitionListener bitmapCompressListener) {

            this.mBitmapCompressListener = bitmapCompressListener;
        }

        /**
         * 图片处理之前
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (BitmapRevitionConfig.BITMAP_LOGOUT) {
                Log.d(LOG_TAG, "into onPreExecute()");
            }
            if (mBitmapCompressListener != null) {
                mBitmapCompressListener.beforeBitmapRevition();
            }
        }

        /**
         * 异步执行中，进行图片处理
         */
        @Override
        protected String doInBackground(String[]... params) {
            if (BitmapRevitionConfig.BITMAP_LOGOUT) {
                Log.d(LOG_TAG, "into doInBackground()");
                Log.d(LOG_TAG, "原文件路径：" + params[0][0]);
                Log.d(LOG_TAG, "新文件路径：" + params[0][1]);
            }
            String compBitmapPath = "";
            if (!new File((params[0][0])).exists()) {
                throw new UnsupportedOperationException("原文件不存在，无法进行压缩...");
            } else {
                Bitmap resultBitmap = BitmapHelper.getBitmapByFileDe(params[0][0]);
                String fileName = new File(params[0][0]).getName();
                try {
                    // 对图片进行压缩和旋转处理
                    resultBitmap = BitmapHelper.revitionImage(params[0][0]);
                    // 将处理后的图片保存到本地
                    compBitmapPath = saveImg(resultBitmap, fileName, params[0][1]);
                    if (BitmapRevitionConfig.BITMAP_LOGOUT) {
                        Log.d(LOG_TAG, "原文件大小：" + new File(params[0][0]).length() / 1024 + "KB");
                        Log.d(LOG_TAG, "新文件大小：" + new File(params[0][1]).length() / 1024 + "KB");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return compBitmapPath;
        }

        /**
         * 异步执行后,返回压缩后的图片保存路径
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mBitmapCompressListener != null) {
                mBitmapCompressListener.afterBitmapRevition(result);
            }
        }
    }

    /**
     * 将处理 后的图片保存到本地
     *
     * @param b
     * @param name
     * @param newFilePath
     * @return
     * @throws Exception
     */
    private String saveImg(Bitmap b, String name, String newFilePath) throws Exception {
        if (BitmapRevitionConfig.BITMAP_LOGOUT) {
            Log.d(LOG_TAG, "into saveImg()");
            Log.d(LOG_TAG, "新文件路径：" + newFilePath);
        }
        File mediaFile = new File(newFilePath);
        if (mediaFile != null && mediaFile.exists() && mediaFile.isFile()) {
            FileOutputStream fos = new FileOutputStream(mediaFile);
            b.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            b.recycle();
            b = null;
            return mediaFile.getAbsolutePath();
        } else {
            return "";
        }
    }


    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromFile(String filePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(filePath, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 获取尺寸压缩倍数
        newOpts.inSampleSize = getRatioSize(w, h);
        newOpts.inJustDecodeBounds = false;//读取所有内容
        newOpts.inDither = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        newOpts.inTempStorage = new byte[32 * 1024];
        Bitmap bitmap = null;
        File file = new File(filePath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
                //旋转图片
                int photoDegree = readPictureDegree(filePath);
                if (photoDegree != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(photoDegree);
                    // 创建新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 计算缩放比
     *
     * @param bitWidth  当前图片宽度
     * @param bitHeight 当前图片高度
     * @return int 缩放比
     * @author XiaoSai
     * @date 2016年3月21日 下午3:03:38
     * @version V1.0.0
     */
    public static int getRatioSize(int bitWidth, int bitHeight) {
        // 图片最大分辨率
        int imageHeight = 1280;
        int imageWidth = 960;
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static void compressBitmap(Bitmap bmp, File file) {


        // 0-100 100为不压缩
        int options = 80;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, options, stream);//质量压缩方法，100代表不压缩，数据传到stream


        while (stream.toByteArray().length / 1024 > 150) {//如果图片大于150kb继续压缩

            stream.reset();//清空输出流中的数据
            bmp.compress(Bitmap.CompressFormat.JPEG, options, stream);
            options -= 10;

        }

        Log.e("size", stream.toByteArray().length / 1024 + "kb");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
