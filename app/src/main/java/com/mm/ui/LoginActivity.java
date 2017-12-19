package com.mm.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.Logs;
import com.chenyx.libs.utils.ToastUtils;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.ILoginView;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.UserEntity;
import com.mm.presenter.UserPresenter;
import com.mm.widget.MLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Company：苗苗
 * Class Describe： 登录
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class LoginActivity extends BaseMvpActivity<UserPresenter> implements ILoginView {

    /**
     * 手机号
     */
    @BindView(R.id.et_phone)
    EditText tvPhone;
    /**
     * 密码
     */
    @BindView(R.id.et_pwd)
    EditText tvPwd;


    @Override
    protected UserPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new UserPresenter(this);
        }
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setTranslucentStatus(R.color.white);
    }

    @Override
    protected void initUI() {
        tvPhone.setText("18285011583");
        tvPwd.setText("123456");
    }

    @OnClick({R.id.loginClose, R.id.tv_login, R.id.go_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginClose:
                finish();
                break;
            case R.id.go_register:
                JumpUtil.overlay(LoginActivity.this, RegisterActivity.class);
                break;
            case R.id.tv_login:
                String phone = tvPhone.getText().toString();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    ToastUtils.showShort("请输入手机号");
                    tvPhone.requestFocus();
                    return;
                }
                String pwd = tvPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShort("请输入密码");
                    tvPwd.requestFocus();
                    return;
                }
                mPresenter.Login(phone, pwd);
                break;
        }


    }

    @Override
    public void LoginSuccess(UserEntity user) {
        UserCache.put(user);
        Logs.d(TAG, "mm_LoginSuccess");

        JMessageClient.login(user.getPhone(), tvPwd.getText().toString(), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {

                if (responseCode == 0) {
                    UserInfo myInfo = JMessageClient.getMyInfo();
//                    File avatarFile = myInfo.getAvatarFile();
                    //登陆成功,如果用户有头像就把头像存起来,没有就设置null

                    String username = myInfo.getUserName();
                    String appKey = myInfo.getAppKey();

                    Logs.d(TAG, "JPush_LoginSuccess");
                    finish();
                } else {
                    Logs.d(TAG, "JPush_Loginf");
                }
            }
        });


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