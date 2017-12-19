package com.mm.presenter.mine;

import android.graphics.Bitmap;
import android.os.Environment;

import com.chenyx.libs.bitmap.BitmapRevition;
import com.chenyx.libs.utils.Files;
import com.chenyx.libs.utils.Logs;
import com.chenyx.libs.utils.ToastUtils;
import com.mm.contract.mine.IUploadView;
import com.mm.data.BaseData;
import com.mm.data.Constant;
import com.mm.data.JsonRequestBody;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.ImageEntity;
import com.mm.data.entity.VersionEntity;
import com.mm.presenter.BasePresenter;
import com.mm.ulits.FilesUploadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Company：苗苗
 * Class Describe：上传文件
 * Create Person：Chenyx
 * Create Time：2017/11/29 下午6:20
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UploadPresenter extends BasePresenter<IUploadView> {

    private static final String TAG = "UploadPresenter";

    public UploadPresenter(IUploadView MvpView) {
        super(MvpView);
    }


    /**
     * 文件上传
     *
     * @param filePaths 需要上传的文件路径
     * @param fileTypes 对应 文件的格式
     */
    public void uploadFiles(List<String> filePaths, List<String> fileTypes) {

        Logs.d("uploadFiles=", filePaths.get(0).toString());

        List<File> files = new ArrayList<>();
        for (String path : filePaths) {
            files.add(new File(path));
        }

        List<MultipartBody.Part> multiPartFiles = FilesUploadUtils.multiPartFiles(files, fileTypes);
        addSubscription(RetrofitApiFactory.getArticleApi().MyUpLoad(multiPartFiles), new SubscriberCallBack<List<ImageEntity>>() {
            @Override
            protected void onSuccess(List<ImageEntity> data) {
                MvpView.UploadSuccess(data);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }


    public void compressImgs(final List<String> filePaths) {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                Logs.d(TAG, "call thread name:" + Thread.currentThread().getName());
                List<String> compressFilePaths = new ArrayList<>();
                for (String sourceFilePath : filePaths) {
                    if (new File(sourceFilePath).exists()) {
                        String comFileName = Files.getRandomFileName("jpg");
                        File comFile = Files.makeFile(Environment.getExternalStorageDirectory().getPath() + Constant.COMPRESS_IMAGE_CACHE_DIR + File.separator + comFileName);
                        Bitmap bit = BitmapRevition.getBitmapFromFile(sourceFilePath);
                        BitmapRevition.compressBitmap(bit, comFile);
                        compressFilePaths.add(comFile.getAbsolutePath());
                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = new FileInputStream(new File(sourceFilePath));
                            Logs.d(TAG, "origpath:" + sourceFilePath);
                            Logs.d(TAG, "origpath size :" + fileInputStream.available() / 1024 + "kb");
                            Logs.d(TAG, "compressPath:" + comFile.getAbsolutePath());
                            Logs.d(TAG, "compressPath size:" + new FileInputStream(comFile).available() / 1024 + "kb");
                        } catch (Exception e) {

                        } finally {
                            if (null != fileInputStream) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e) {

                                }
                            }
                        }
                    }
                }
                subscriber.onNext(compressFilePaths);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        if (MvpView != null)
                            MvpView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onNext(filePaths);
                        if (MvpView != null)
                            MvpView.hideProgress();
                    }

                    @Override
                    public void onNext(List<String> filsPaths) {
                        MvpView.compressImgs(filsPaths);
                        Logs.d(TAG, "onNext thread name:" + Thread.currentThread().getName());
                    }
                });
    }


    public void UpdateHeadImg(String HeadImg) {

        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserCache.get().getUserID());
        param.put("HeadImg", HeadImg);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("更新中");

        addSubscription(RetrofitApiFactory.getUserApi().UpdateHeadImg(body), new SubscriberCallBack<BaseData>() {
            @Override
            protected void onSuccess(BaseData data) {
                MvpView.UpdateSucces();
                ToastUtils.showShort("更新成功");
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
    public void checkVersion(int versionCode) {

        Map<String, Object> param = new HashMap<>();
        param.put("Code", versionCode);

        RequestBody body = JsonRequestBody.createJsonBody(param);
        MvpView.showProgress("检测中");

        addSubscription(RetrofitApiFactory.getUserApi().checkVersion(body), new SubscriberCallBack<VersionEntity>() {
            @Override
            protected void onSuccess(VersionEntity data) {
                MvpView.checkVersion(data);
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }

}
