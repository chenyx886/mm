package com.mm.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.chenyx.libs.utils.JumpUtil;
import com.mm.R;
import com.mm.base.BaseActivity;

/**
 * Company：苗苗
 * Class Describe：启动界面
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

    }


    @Override
    protected void initUI() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JumpUtil.overlay(SplashActivity.this, MainActivity.class);
                finish();
            }
        }, 1500);

    }


}
