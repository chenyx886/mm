package com.mm.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mm.R;
import com.mm.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Company：苗苗
 * Class Describe：帮助中心
 * Create Person：Chenyx
 * Create Time：2017/11/22 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class HelpActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView mBack;
    @BindView(R.id.tv_title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        initUI();
    }

    @Override
    protected void initUI() {
        mBack.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("帮助中心");
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