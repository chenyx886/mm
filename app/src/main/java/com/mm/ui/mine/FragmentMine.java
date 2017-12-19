package com.mm.ui.mine;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenyx.libs.glide.GlideShowImageUtils;
import com.chenyx.libs.utils.Apps;
import com.chenyx.libs.utils.Files;
import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.ToastUtils;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.mm.MApplication;
import com.mm.R;
import com.mm.base.BaseMvpFragment;
import com.mm.contract.mine.IUploadView;
import com.mm.data.Constant;
import com.mm.data.api.RetrofitApiFactory;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.ImageEntity;
import com.mm.data.entity.VersionEntity;
import com.mm.presenter.mine.UploadPresenter;
import com.mm.ui.LoginActivity;
import com.mm.ui.adapter.PersonaltemAdapter;
import com.mm.ulits.CatchUtil;
import com.mm.widget.IconTextItem;
import com.mm.widget.MLoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Company：苗苗
 * Class Describe：我的
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FragmentMine extends BaseMvpFragment<UploadPresenter> implements IUploadView {

    public static String TAG = FragmentMine.class.getName();


    /**
     * 相机
     */
    private static final int REQUEST_CAMERA_CODE = 11;
    /**
     * 图片裁剪
     */
    private static final int REQUST_CODE_CROP = 33;
    /**
     * 剪裁路径
     */
    private String cropPath;

    @BindView(R.id.backdrop)
    ImageView mBackdrop;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout mCollapsing;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;

    /**
     * 头像
     */
    @BindView(R.id.iv_login)
    ImageView mIvLogin;

    /**
     * 登录
     */
    @BindView(R.id.tv_login)
    TextView mTvLogin;

    /**
     * 管理项
     */
    @BindView(R.id.personal_item)
    RecyclerView mPersonalItem;
    /**
     * 清除缓存
     */
    @BindView(R.id.rl_mine_cleanData)
    IconTextItem mCleanData;
    /**
     * 检查更新
     */
    @BindView(R.id.rl_mine_update)
    IconTextItem mUpdate;
    /**
     * 帮助中心
     */
    @BindView(R.id.rl_mine_help)
    IconTextItem mHelp;
    /**
     * 意见反馈
     */
    @BindView(R.id.rl_mine_feedback)
    IconTextItem mFeedback;

    private String HeadImg;


    @Override
    protected UploadPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new UploadPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    protected void initUI() {

        mPersonalItem.setAdapter(new PersonaltemAdapter(getContext()));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        mPersonalItem.setLayoutManager(staggeredGridLayoutManager);

        mUpdate.setRightTextView("V" + Apps.getVersionName(MApplication.getInstance()));
        mCleanData.setRightTextView(CatchUtil.getInstance().getGlideCacheSize(getActivity()) + "");


    }


    @OnClick({R.id.iv_login, R.id.tv_login, R.id.rl_mine_update, R.id.rl_mine_cleanData, R.id.rl_mine_help, R.id.rl_mine_feedback})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login:
                if (UserCache.get() != null) {
                    if (ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        FragmentMine.this.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_RQESTCODE);
                    } else {
                        selectImgs();
                    }
                } else {
                    ToastUtils.showShort("请先登录");
                }


                break;
            case R.id.tv_login:
                if (UserCache.get() != null) {
                    showDialog();
                } else {
                    JumpUtil.overlay(getActivity(), LoginActivity.class);
                }
                break;
            case R.id.rl_mine_update:
                mPresenter.checkVersion(Apps.getVersionCode(MApplication.getAppContext()));
//                NovateApi.getUserApi().checkVersion(Apps.getVersionCode(MApplication.getAppContext()))

                break;
            case R.id.rl_mine_cleanData:
                UserCache.clear();
                if (CatchUtil.getInstance().clearImageAllCache(MApplication.getAppContext()))
                    ToastUtils.showShort("清除成功");
                mCleanData.setRightTextView(CatchUtil.getInstance().getGlideCacheSize(getActivity()) + "");
                break;
            case R.id.rl_mine_help:
                if (UserCache.get() != null) {
                    JumpUtil.overlay(getActivity(), HelpActivity.class);
                } else {
                    JumpUtil.overlay(getActivity(), LoginActivity.class);
                }
                break;
            case R.id.rl_mine_feedback:
                if (UserCache.get() != null) {
                    JumpUtil.overlay(getActivity(), FeedbackActivity.class);
                } else {
                    JumpUtil.overlay(getActivity(), LoginActivity.class);
                }
                break;

        }
    }


    @Override
    public void checkVersion(final VersionEntity data) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.version_update));
        String message = String.format(getResources().getString(R.string.version_number), data.getName(), data.getContent());
        builder.setMessage(message);
        builder.setPositiveButton("下载更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downApk(data.getPath());
            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    /**
     * 下载Apk
     *
     * @param url
     */
    private void downApk(String url) {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri weibo_url = Uri.parse(RetrofitApiFactory.BASE_URL + url);
        intent.setData(weibo_url);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (UserCache.get() != null) {
            GlideShowImageUtils.displayCircleNetImage(getActivity(), RetrofitApiFactory.BASE_URL + UserCache.get().getHeadImg(),
                    mIvLogin, R.mipmap.iv_header_resource_ico);
            mTvLogin.setText("退出");
        } else {

            GlideShowImageUtils.displayCircleNetImage(getActivity(), "", mIvLogin, R.mipmap.iv_header_resource_ico);
            mTvLogin.setText("请登录");
        }
    }

    /**
     * 退出系统
     */
    // 弹出的对话框
    private Dialog dialog;
    private Button mDialogSubmit, mDialogCancel;

    private void showDialog() {
        dialog = new Dialog(getActivity(), R.style.showDialog);
        dialog.setContentView(getDialogView());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        // 增加一些配置
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = Apps.getScreenWidth(getActivity()) * 85 / 100; // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
        lp.alpha = 1.0f; // 透明度

        dialog.show();
    }

    private View getDialogView() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.appint_dialog_sheet, null);
        final EditText content = dialogView.findViewById(R.id.content);
        content.setVisibility(View.GONE);
        TextView title = dialogView.findViewById(R.id.appoint_dialog_center_name);
        title.setText("您确定要退出吗？");
        mDialogSubmit = dialogView.findViewById(R.id.appoint_dialog_submit);
        mDialogCancel = dialogView.findViewById(R.id.appoint_dialog_cancel);
        mDialogSubmit.setText("确定");
        mDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCache.clear();
                dialog.dismiss();
                onResume();
            }
        });

        return dialogView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            //图片选择返回
            if (requestCode == REQUEST_CAMERA_CODE) {
                String filePath = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT).get(0);
                cropImage(filePath);
            }

            //图片剪裁返回
            else if (requestCode == REQUST_CODE_CROP) {
                List<String> filePaths = new ArrayList<>();
                List<String> typeStrs = new ArrayList<>();
                typeStrs.add("img/jpg");
                filePaths.add(cropPath);
                mPresenter.uploadFiles(filePaths, typeStrs);
            }
        }
    }

    @Override
    public void compressImgs(List<String> filePaths) {
        List<String> typeStrs = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            typeStrs.add("img/jpg");
        }
        if (filePaths.size() > 0) {
            mPresenter.uploadFiles(filePaths, typeStrs);
        }
    }

    @Override
    public void UpdateSucces() {
        GlideShowImageUtils.displayCircleNetImage(getActivity(), RetrofitApiFactory.BASE_URL + HeadImg,
                mIvLogin, R.mipmap.iv_header_resource_ico);
    }


    public void UploadSuccess(List<ImageEntity> list) {

        if (list.size() > 0) {
            HeadImg = list.get(0).getPath();
            mPresenter.UpdateHeadImg(HeadImg);
        }
    }

    /**
     * 图片选择
     */
    private void selectImgs() {

        PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    /**
     * 图片剪裁
     *
     * @param filePath
     */
    private void cropImage(String filePath) {

        Uri imageUri;
        Uri outputUri;
        String newFileName = Files.getRandomFileName("jpg");
        File file = Files.makeFile(Constant.SDCardRoot + Constant.COMPRESS_IMAGE_CACHE_DIR + File.separator + newFileName);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(getActivity(), "com.ztmzw.fprovider", new File(filePath));
            outputUri = Uri.fromFile(file);
        } else {
            imageUri = Uri.fromFile(new File(filePath));
            outputUri = Uri.fromFile(file);
        }
        cropPath = file.getAbsolutePath();
        buildIntent(intent, imageUri, outputUri, 1, 1, 600, 600);
        startActivityForResult(intent, REQUST_CODE_CROP);
    }

    /**
     * 配置剪裁参数
     *
     * @param intent
     * @param resourceUri
     * @param dataUri
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     * @return
     */
    private Intent buildIntent(Intent intent, Uri resourceUri, Uri dataUri, int aspectX, int aspectY, int outputX, int outputY) {

        intent.setDataAndType(resourceUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, dataUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }


    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(getActivity(), message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
