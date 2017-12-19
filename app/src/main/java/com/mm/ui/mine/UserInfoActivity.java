package com.mm.ui.mine;

import android.os.Bundle;

import com.mm.R;
import com.mm.base.BaseActivity;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/11/24 下午12:34
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UserInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void initUI() {

    }
}
