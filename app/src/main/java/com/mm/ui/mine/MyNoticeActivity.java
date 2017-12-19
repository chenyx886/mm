package com.mm.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mm.R;
import com.mm.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Company：苗苗
 * Class Describe：我的消息
 * Create Person：Chenyx
 * Create Time：2017/11/25 下午10:54
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MyNoticeActivity extends BaseActivity {
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

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_my_find);
    }

    @Override
    protected void initUI() {
        mTitle.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.VISIBLE);
        mTitle.setText("我的消息");
    }

    @OnClick({R.id.tv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
