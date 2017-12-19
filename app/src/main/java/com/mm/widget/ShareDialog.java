package com.mm.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chenyx.libs.utils.Logs;
import com.share.ShareUtils;
import com.mm.R;
import com.mm.data.entity.ShareEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：分享对话框
 * 创建人：Chenyx
 * 创建时间：2017/5/21 21:08
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ShareDialog extends Dialog {
    /**
     * 微信朋友
     */
    @BindView(R.id.tv_wechat_friends)
    TextView tvWechatFriends;
    /**
     * 朋友圈
     */
    @BindView(R.id.tv_wechat_circle)
    TextView tvWechatCircle;

    private ShareEntity entity;

    private Context mContext;

    public ShareDialog(Context context, ShareEntity entity, int themeResId) {
        super(context, themeResId);
        this.entity = entity;
        this.mContext = context;
        initUI();
    }

    /**
     * 对话框布局初始化
     */
    private void initUI() {
        setContentView(R.layout.dialog_share_layout);
        ButterKnife.bind(this);
        tvWechatFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logs.d("ShareDialog", entity.getLinkUrl());
                ShareUtils.shareWebPage(mContext, entity.getLinkUrl(), entity.getTitle(), entity.getContent(), ShareUtils.WECHAT_SHARE_TYPE_FRENDS);
                dismiss();
            }
        });
        tvWechatCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareWebPage(mContext, entity.getLinkUrl(), entity.getTitle(), entity.getContent(), ShareUtils.WECHAT_SHARE_TYPE_TALK);
                dismiss();
            }
        });
    }


    /**
     * 显示
     */
    public void showDialog() {

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.share_dialog_anim_style);
        dialogWindow.setGravity(Gravity.BOTTOM);
        show();
    }
}
