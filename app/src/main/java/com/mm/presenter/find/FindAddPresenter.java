package com.mm.presenter.find;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import com.chenyx.libs.bitmap.BitmapRevition;
import com.chenyx.libs.utils.Files;
import com.chenyx.libs.utils.Logs;
import com.chenyx.libs.utils.ToastUtils;
import com.mm.contract.find.IFindAddView;
import com.mm.data.BaseData;
import com.mm.data.Constant;
import com.mm.data.SubscriberCallBack;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.cache.UserCache;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Company：苗苗
 * Class Describe：发现列表
 * Create Person：Chenyx
 * Create Time：2017/11/12 下午8:13
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FindAddPresenter extends BasePresenter<IFindAddView> {

    private static final String TAG = "FindAddPresenter";


    public FindAddPresenter(IFindAddView MvpView) {
        super(MvpView);
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
                        MvpView.returnImage(filsPaths);
                        Logs.d(TAG, "onNext thread name:" + Thread.currentThread().getName());
                    }
                });
    }

    public void AddFind(String content, List<String> filePaths, List<String> fileTypes) {
        MvpView.showProgress("发布中");

        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserCache.get().getUserID());
        param.put("Title", "");
        param.put("Content", content);

        List<File> files = new ArrayList<>();

        for (String path : filePaths) {
            files.add(new File(path));
        }

        List<MultipartBody.Part> multiPartFiles = FilesUploadUtils.multiPartFiles(files, fileTypes);

        addSubscription(RetrofitApiFactory.getArticleApi().AddFind(param, multiPartFiles), new SubscriberCallBack<BaseData>() {
            @Override
            protected void onSuccess(BaseData data) {
                ToastUtils.showShort("发布成功");
                ((Activity) MvpView).finish();
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }

    /**
     * 发布视频 发现
     *
     * @param content
     * @param filePaths
     * @param fileTypes
     */
    public void AddVideoFind(String content, List<String> filePaths, List<String> fileTypes) {
        MvpView.showProgress("发布中");

        Map<String, Object> param = new HashMap<>();
        param.put("UserID", UserCache.get().getUserID());
        param.put("Content", content);

        List<File> files = new ArrayList<>();
        for (String path : filePaths) {
            files.add(new File(path));
        }
        List<MultipartBody.Part> multiPartFiles = FilesUploadUtils.multiPartFiles(files, fileTypes);

        addSubscription(RetrofitApiFactory.getArticleApi().AddVideoFind(param, multiPartFiles), new SubscriberCallBack<BaseData>() {
            @Override
            protected void onSuccess(BaseData data) {
                ToastUtils.showShort("发布成功");
                ((Activity) MvpView).finish();
            }

            @Override
            public void onCompleted() {
                MvpView.hideProgress();
            }
        });
    }
}
