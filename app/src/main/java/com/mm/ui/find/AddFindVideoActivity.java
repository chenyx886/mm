package com.mm.ui.find;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenyx.libs.utils.Files;
import com.chenyx.libs.utils.ToastUtils;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.find.IFindAddView;
import com.mm.data.Constant;
import com.mm.presenter.find.FindAddPresenter;
import com.mm.widget.MGridView;
import com.mm.widget.MLoadingDialog;
import com.werb.permissionschecker.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * Company：苗苗
 * Class Describe：新增视频发现
 * Create Person：Chenyx
 * Create Time：2017/11/17 上午1:41
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class AddFindVideoActivity extends BaseMvpActivity<FindAddPresenter> implements IFindAddView {
    /**
     * 返回
     */
    @BindView(R.id.tv_back)
    TextView mBack;
    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView mTitle;
    /**
     * 发布
     */
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    /**
     * 视频
     */
    @BindView(R.id.iv_video)
    ImageView mVideo;
    /**
     * 图片
     */
    @BindView(R.id.mgv_imgs)
    MGridView mMgvImgs;

    private List<String> selectedImages = new ArrayList<>();
    /**
     * 描述
     */
    @BindView(R.id.content)
    TextView mContent;

    private String path = "";
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private PermissionChecker permissionChecker;

    @Override
    protected FindAddPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new FindAddPresenter(this);
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_find_text);
        setTranslucentStatus(R.color.colorAccent);
        Files.makeDir(Constant.SDCardRoot + Constant.COMPRESS_IMAGE_CACHE_DIR);
        permissionChecker = new PermissionChecker(this); // initialize，must need
    }

    @Override
    protected void initUI() {
        mTitle.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mTvConfirm.setVisibility(View.VISIBLE);
        mTvConfirm.setText(R.string.release);
        mTitle.setText(R.string.release_resource);
        mMgvImgs.setVisibility(View.GONE);
        mVideo.setVisibility(View.VISIBLE);
        path = getIntent().getStringExtra("path");
        selectedImages.clear();
        selectedImages.add(path);
    }


    /**
     * 点击操作
     *
     * @param view
     */
    @OnClick({R.id.tv_back, R.id.iv_video, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {

            //返回
            case R.id.tv_back:
                finish();
                break;
            //视频
            case R.id.iv_video:
                if (permissionChecker.isLackPermissions(PERMISSIONS)) {
                    permissionChecker.requestPermissions();
                } else {
                    VideoRecorderActivity.showActivityForResult(AddFindVideoActivity.this);
                }

                break;

            //发布
            case R.id.tv_confirm:


                if (selectedImages.size() > 0) {
                    List<String> typeStrs = new ArrayList<>();
                    typeStrs.add("video/mp4");
                    mPresenter.AddVideoFind(mContent.getText().toString().trim(), selectedImages, typeStrs);
                } else
                    ToastUtils.showShort("视频为空");
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoRecorderActivity.REQUESTCODE) {
            path = VideoRecorderActivity.path;
            selectedImages.add(path);
        }

    }

    @Override
    public void returnImage(List<String> filePaths) {
    }

    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }
}


