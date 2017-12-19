package com.mm.ui.find;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chenyx.libs.utils.Files;
import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.ToastUtils;
import com.chenyx.libs.utils.Toasts;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.mm.MApplication;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.find.IFindAddView;
import com.mm.data.Constant;
import com.mm.data.entity.ImageEntity;
import com.mm.presenter.find.FindAddPresenter;
import com.mm.ui.adapter.ChoicePicAdapter;
import com.mm.widget.MGridView;
import com.mm.widget.MLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;


/**
 * Company：苗苗
 * Class Describe：新增文字发现
 * Create Person：Chenyx
 * Create Time：2017/11/17 上午1:41
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class AddFindTextActivity extends BaseMvpActivity<FindAddPresenter> implements IFindAddView {
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
     * 相机
     */
    private static final int REQUEST_CAMERA_CODE = 11;
    private List<String> selectedImages = new ArrayList<>();
    private ChoicePicAdapter choicePicAdapter;
    /**
     * 描述
     */
    @BindView(R.id.content)
    TextView mContent;
    /**
     * 图片
     */
    @BindView(R.id.mgv_imgs)
    MGridView mMgvImgs;

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
    }

    private int type = 0;

    @Override
    protected void initUI() {
        mTitle.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mTvConfirm.setVisibility(View.VISIBLE);
        mTvConfirm.setText(R.string.release);
        mTitle.setText(R.string.release_resource);

        choicePicAdapter = new ChoicePicAdapter(this, new ChoicePicAdapter.DeleteImgInterface() {
            @Override
            public void show(int position) {
                selectedImages.remove(choicePicAdapter.getItem(position).getPath());
                choicePicAdapter.setData(buildlImgItms(selectedImages));
            }
        });
        mMgvImgs.setAdapter(choicePicAdapter);
        choicePicAdapter.setData(buildlImgItms(selectedImages));
        type = getIntent().getIntExtra("type", 0);
        if (type == 2) {
            selectImgs();
        }

    }


    @OnItemClick(R.id.mgv_imgs)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //添加图片
        if (choicePicAdapter.getItem(position).isAddItem()) {
            List<String> permisson = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permisson.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permisson.add(Manifest.permission.CAMERA);
            }

            if (permisson.size() != 0) {
                String[] p = new String[permisson.size()];
                ActivityCompat.requestPermissions(AddFindTextActivity.this, permisson.toArray(p), Constant.CAMERA_RQESTCODE);
            } else {
                if (ContextCompat.checkSelfPermission(AddFindTextActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddFindTextActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddFindTextActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_RQESTCODE);
                } else {
                    selectImgs();
                }
            }

        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PhotoPreviewActivity.EXTRA_PHOTOS, (ArrayList<String>) selectedImages);
            bundle.putInt(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, 0);
            JumpUtil.startForResult(this, PhotoPreviewActivity.class, PhotoPreviewActivity.REQUEST_PREVIEW, bundle);
        }
    }

    @OnItemLongClick(R.id.mgv_imgs)
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isSelected = choicePicAdapter.getItem(position).isSelected();
        choicePicAdapter.getItem(position).setSelected(!isSelected);
        choicePicAdapter.notifyDataSetChanged();
        return true;

    }


    /**
     * 图片选择
     */
    private void selectImgs() {

        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setMaxTotal(9);
        intent.setSelectedPaths((ArrayList<String>) selectedImages);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == this.RESULT_OK) {
            //图片选择返回
            if (requestCode == REQUEST_CAMERA_CODE) {
                selectedImages = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                choicePicAdapter.setData(buildlImgItms(selectedImages));
            }
            //图片预览
            else if (requestCode == PhotoPreviewActivity.REQUEST_PREVIEW) {
                selectedImages = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                choicePicAdapter.setData(buildlImgItms(selectedImages));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CAMERA_RQESTCODE) {
            selectImgs();
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImgs();
            } else {
                Toasts.showShort(MApplication.getAppContext(), "请为苗苗开启相册权限");
            }
        }
    }

    /**
     * 将选择的图片路径转为ImgItem列表
     *
     * @param selectedImages
     * @return
     */
    private List<ImageEntity> buildlImgItms(List<String> selectedImages) {

        List<ImageEntity> picItems = new ArrayList<>();
        for (String filePath : selectedImages) {
            picItems.add(buildImgItem(filePath, false));
        }
        if (selectedImages.size() < 9) {
            picItems.add(buildImgItem("", true));
        }
        return picItems;
    }

    private ImageEntity buildImgItem(String filePath, boolean isAdd) {
        ImageEntity item = new ImageEntity();
        item.setAddItem(isAdd);
        item.setPath(filePath);
        return item;
    }


    /**
     * 点击操作
     *
     * @param view
     */
    @OnClick({R.id.tv_back, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {

            //返回
            case R.id.tv_back:
                finish();
                break;

            //发布
            case R.id.tv_confirm:

                if (TextUtils.isEmpty(mContent.getText().toString().trim())) {
                    ToastUtils.showShort("说点什么吧");
                    return;
                }

                if (selectedImages.size() > 0)
                    mPresenter.compressImgs(selectedImages);
                else {
                    mPresenter.AddFind(mContent.getText().toString().trim(), new ArrayList<String>(), new ArrayList<String>());
                }
                break;
            default:
                break;

        }
    }


    @Override
    public void returnImage(List<String> filePaths) {
        List<String> typeStrs = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            typeStrs.add("img/jpg");
        }

        mPresenter.AddFind(mContent.getText().toString().trim(), filePaths, typeStrs);
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


