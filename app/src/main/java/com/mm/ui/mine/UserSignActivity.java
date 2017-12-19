package com.mm.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.location.LocationInterface;
import com.location.LocationService;
import com.mm.MApplication;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.mine.IUserSignView;
import com.mm.data.Constant;
import com.mm.data.entity.ImageEntity;
import com.mm.data.entity.LocationEnitity;
import com.mm.presenter.mine.UserSginPresenter;
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
 * Class Describe：签到
 * Create Person：Chenyx
 * Create Time：2017/11/29 下午7:40
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UserSignActivity extends BaseMvpActivity<UserSginPresenter> implements IUserSignView, LocationInterface {
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
     * 地址
     */
    @BindView(R.id.tv_addr)
    TextView mAddr;
    /**
     * 发布
     */
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;
    /**
     * 内容
     */
    private String Explain;
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

    private LocationService locationService;
    /**
     * 经纬度
     */
    private double latitude, longitude;
    /**
     * 地址
     */
    private String Adderss;

    /**
     * 是否定位成功
     */
    private boolean isLocation = false;

    @Override
    protected UserSginPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new UserSginPresenter(this);
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_user_sign);
        Files.makeDir(Constant.SDCardRoot + Constant.COMPRESS_IMAGE_CACHE_DIR);
    }

    @Override
    protected void initUI() {
        mTitle.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mTvConfirm.setVisibility(View.VISIBLE);
        mTvConfirm.setText("签到");
        mTitle.setText("今日签到");

        choicePicAdapter = new ChoicePicAdapter(this, new ChoicePicAdapter.DeleteImgInterface() {
            @Override
            public void show(int position) {
                selectedImages.remove(choicePicAdapter.getItem(position).getPath());
                choicePicAdapter.setData(buildlImgItms(selectedImages));
            }
        });
        mMgvImgs.setAdapter(choicePicAdapter);
        choicePicAdapter.setData(buildlImgItms(selectedImages));
        initLocation();
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
                ActivityCompat.requestPermissions(UserSignActivity.this, permisson.toArray(p), Constant.CAMERA_RQESTCODE);
            } else {
                if (ContextCompat.checkSelfPermission(UserSignActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(UserSignActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserSignActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.CAMERA_RQESTCODE);
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
            } else if (requestCode == MoreAddressActivity.REQUESTCODE) {
                latitude = MoreAddressActivity.LAT;
                longitude = MoreAddressActivity.LON;
                Adderss = MoreAddressActivity.ADDRESS;
                mAddr.setText(Adderss);
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


    @Override
    public void returnImage(List<String> filePaths) {
        List<String> typeStrs = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            typeStrs.add("img/jpg");
        }
        mPresenter.UserSign(longitude, latitude, Adderss, Explain, filePaths, typeStrs);
    }

    /**
     * 点击操作
     *
     * @param view
     */
    @OnClick({R.id.tv_back, R.id.tv_addr, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {

            //返回
            case R.id.tv_back:
                finish();
                break;
            //选择地址
            case R.id.tv_addr:
                MoreAddressActivity.showActivityForResult(UserSignActivity.this);
                break;

            //发布
            case R.id.tv_confirm:
                Explain = mContent.getText().toString().trim();
                if (TextUtils.isEmpty(Explain)) {
                    ToastUtils.showShort("说点什么吧");
                    return;
                }

                if (selectedImages.size() > 0)
                    mPresenter.compressImgs(selectedImages);
                else {
                    mPresenter.UserSign(longitude, latitude, Adderss, Explain, null, null);
                }
                break;
            default:
                break;

        }
    }


    private void initLocation() {
        locationService = new LocationService(this);
        locationService.registerLocationInterface(this);
        getLocationPersimmions();
    }


    private void getLocationPersimmions() {

        ArrayList<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MApplication.getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), Constant.POSTION_RQESTCODE);
        } else {
            startLocation();
        }
    }

    private void startLocation() {
        locationService.start();
        handler.obtainMessage(0, "定位中...").sendToTarget();
    }

    private void stopLocation() {
        locationService.stop();
    }

    // 百度定位信息实体
    private LocationEnitity location;

    /**
     * 百度定位成功
     *
     * @param locationInfo
     */
    @Override
    public void onReLocationSucess(LocationEnitity locationInfo) {

        stopLocation();
        location = locationInfo;
        latitude = location.getLat();
        longitude = location.getLng();
        Adderss = location.getAddress();
        isLocation = true;
        handler.obtainMessage(1).sendToTarget();

    }

    /**
     * 百度定位失败
     *
     * @param mesSage
     */
    @Override
    public void onReLocationFail(String mesSage) {
        isLocation = false;
        stopLocation();
    }

    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mAddr.setText(msg.obj.toString());
                    break;
                case 1:
                    mAddr.setText(location.getAddress());
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.stop();
    }
}
