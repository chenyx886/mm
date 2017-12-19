package com.mm.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.chenyx.libs.utils.ToastUtils;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.ILoginView;
import com.mm.data.entity.UserEntity;
import com.mm.presenter.UserPresenter;
import com.mm.widget.ClearEditText;
import com.mm.widget.MLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Company：苗苗
 * Class Describe：注册
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class RegisterActivity extends BaseMvpActivity<UserPresenter> implements ILoginView {

    /**
     * 手机号
     */
    @BindView(R.id.et_phone)
    EditText mPhone;
    /**
     * 昵称
     */
    @BindView(R.id.et_nickname)
    EditText mNickName;
    /**
     * 密码1
     */
    @BindView(R.id.et_password_one)
    ClearEditText mPasswordOne;
    /**
     * 密码2
     */
    @BindView(R.id.et_password_two)
    ClearEditText mPasswordTwo;

    @Override
    protected UserPresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new UserPresenter(this);
        }
        return mPresenter;
    }

    /**
     * 创建 绑定布局
     *
     * @param bundle
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_register);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTranslucentStatus(R.color.white);

    }

    @Override
    protected void initUI() {

    }


    /**
     * 键盘上回车键 事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }

            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 注册操作
     *
     * @param view
     */
    @OnClick({R.id.btn_register, R.id.go_login})
    public void onClick(View view) {
        switch (view.getId()) {
            //注册
            case R.id.btn_register:
                if (TextUtils.isEmpty(mPhone.getText().toString()) || mPhone.getText().toString().trim().length() != 11) {
                    ToastUtils.showShort("请输入手机号");
                    mPhone.setFocusable(true);
                    mPhone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mPasswordOne.getText().toString().trim())) {
                    ToastUtils.showShort("请输入密码");
                    mPasswordOne.setFocusable(true);
                    mPasswordOne.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mPasswordTwo.getText().toString().trim())) {
                    ToastUtils.showShort("请再次输入密码");
                    mPasswordTwo.setFocusable(true);
                    mPasswordTwo.requestFocus();
                    return;
                }
                if (!TextUtils.equals(mPasswordOne.getText().toString().trim(), mPasswordTwo.getText().toString().trim())) {
                    ToastUtils.showShort("两次密码不一致");
                    return;

                }

                mPresenter.Register(mPhone.getText().toString(), mPasswordOne.getText().toString(), mNickName.getText().toString());
                break;
            //去登录
            case R.id.go_login:
                finish();
                break;
            default:
                break;

        }
    }

    /**
     * 注册成功返回登录
     */
    @Override
    public void LoginSuccess(UserEntity user) {
        //注册 JPush
        JMessageClient.register(mPhone.getText().toString().trim(), mPasswordOne.getText().toString().trim(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    ToastUtils.showShort("注册成功");
                    finish();
                } else {
                    ToastUtils.showShort("注册失败");

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