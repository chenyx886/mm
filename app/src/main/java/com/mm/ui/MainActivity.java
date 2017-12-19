package com.mm.ui;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.chenyx.libs.utils.Apps;
import com.chenyx.libs.utils.Logs;
import com.mm.MApplication;
import com.mm.R;
import com.mm.UploadLocationService;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.news.IMainView;
import com.mm.data.cache.UserCache;
import com.mm.presenter.news.MainPresenter;
import com.mm.ui.find.FragmentFind;
import com.mm.ui.mine.FragmentMine;
import com.mm.ui.msg.FragmentMsg;
import com.mm.ui.news.FragmentNews;
import com.mm.widget.MLoadingDialog;
import com.mm.widget.MoreWindow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * Company：苗苗
 * Class Describe：主页
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MainActivity extends BaseMvpActivity<MainPresenter> implements IMainView {
    /**
     * 政讯
     */
    @BindView(R.id.rb_news)
    RadioButton mNews;
    /**
     * 发现
     */
    @BindView(R.id.rb_find)
    RadioButton mFind;
    /**
     * 消息
     */
    @BindView(R.id.rb_msg)
    RadioButton mMsg;
    /**
     * 我的
     */
    @BindView(R.id.rb_mine)
    RadioButton mMine;
    /**
     * 显示菜单
     */
    @BindView(R.id.show_menu)
    ImageView mShowMenu;

    private CompoundButton selectView;

    @Override
    protected MainPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new MainPresenter(this);
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 启动位置上传Service
        if (MApplication.getInstance().uploadIsClosing()) {
            startService(UploadLocationService.createIntent(this));
        }
    }


    @Override
    protected void initUI() {
        mNews.setTag(FragmentNews.TAG);
        mFind.setTag(FragmentFind.TAG);
        mMsg.setTag(FragmentMsg.TAG);
        mMine.setTag(FragmentMine.TAG);
        mNews.setChecked(true);//默认选中第一个

        getPersimmions();
        setAlias();
    }


    /**
     * showFragment
     *
     * @param curView
     */

    private void showFragment(CompoundButton curView) {

        if (selectView != null) {
            selectView.setChecked(false);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = (String) curView.getTag();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.fl_relcontent, Fragment.instantiate(this, tag), tag);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.show(fragmentManager.findFragmentByTag(tag));
            fragmentTransaction.commit();
        }
        selectView = curView;
    }

    /**
     * hideFragment
     *
     * @param lastView
     */
    private void hideFragment(CompoundButton lastView) {

        if (lastView != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            String tag = (String) lastView.getTag();
            if (fragmentManager.findFragmentByTag(tag) != null) {
                fragmentTransaction.hide(fragmentManager.findFragmentByTag(tag));
                fragmentTransaction.commit();
            }
        }
    }

    /**
     * 界面上单选按钮 OnCheckedChanged 事件
     *
     * @param buttonView
     * @param isChecked
     */
    @OnCheckedChanged({R.id.rb_news, R.id.rb_find, R.id.rb_msg, R.id.rb_mine})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showFragment(buttonView);
        } else {
            hideFragment(buttonView);
        }
    }


    private void setAlias() {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Apps.getDeviceId(this)));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Logs.i(TAG, logs);
                    String JPushID = JPushInterface.getRegistrationID(getApplicationContext());
                    Logs.e("JPush", "极光ID：" + JPushID);
                    //向后台转入 注册极光ID
                    String UserID = "";
                    if (UserCache.get() != null)
                        UserID = UserCache.get().getUserID();
                    mPresenter.RJPushID(UserID, JPushID, Apps.getDeviceId(getApplicationContext()));
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Logs.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Logs.e(TAG, logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_FINISH = 1002;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Set<String> tagSet = new HashSet<>();
            tagSet.add("android");
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Logs.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, tagSet, mAliasCallback);
                    break;
                case MSG_FINISH:

                default:
                    Logs.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


    @OnClick({R.id.show_menu})
    public void onClick(View v) {
        showMoreWindow(v);
    }

    /**
     * 中间图标 点击弹框
     */
    private MoreWindow mMoreWindow;

    private void showMoreWindow(View view) {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }

        mMoreWindow.showMoreWindow(view, 100);
    }

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    @TargetApi(23)
    private void getPersimmions() {

        ArrayList<String> permissions = new ArrayList<>();
        /***
         * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
         */
        // 定位精确位置
//        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        }
//
//        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//
//        //读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
//        // 读写权限
//        if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
//        }
//        // 读取电话状态权限
//        if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
//            permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
//        }
//
//        if (permissions.size() > 0) {
//            requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
//        }
    }


    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
