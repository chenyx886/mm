package com.mm.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chenyx.libs.utils.Toasts;
import com.mm.R;
import com.mm.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Company：苗苗
 * Class Describe：意见反馈
 * Create Person：Chenyx
 * Create Time：2017/11/22 上午10:40
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView mBack;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_conent)
    EditText mConent;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTranslucentStatus(R.color.colorAccent);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initUI();
    }

    @Override
    protected void initUI() {
        mBack.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("意见反馈");
    }


    @OnClick({R.id.tv_back, R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(mConent.getText().toString().trim())) {
                    Toasts.showShort(FeedbackActivity.this, "说说点什么吧");
                    mConent.setFocusable(true);
                    mConent.requestFocus();
                    return;
                }
                Toasts.showShort(FeedbackActivity.this, "您的反馈已提交，我们会尽量为什么解答。");
                finish();
                break;
        }
    }
}
