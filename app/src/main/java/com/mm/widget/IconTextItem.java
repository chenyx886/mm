package com.mm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.MApplication;
import com.mm.R;


/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class IconTextItem extends LinearLayout {

    private View mRootView;
    private ImageView mIcon, mImgPoint;
    private TextView mLeftTextView, mRightTextView;
    private View mLine, mLineFull;

    public IconTextItem(Context context) {
        super(context);
        init(context, null, 0);
    }

    public IconTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public IconTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_icon_text_item, this, true);

        mIcon = findViewById(R.id.imageView);
        mImgPoint = findViewById(R.id.img_point);
        mLeftTextView = findViewById(R.id.leftTextView);
        mRightTextView = findViewById(R.id.rightTextView);
        mLine = findViewById(R.id.line);
        mLineFull = findViewById(R.id.lineFull);

        // Load attributes
        if (attrs != null) {
            TypedArray a = null;
            try {
                a = getContext().obtainStyledAttributes(attrs, R.styleable.IconTextItem, defStyleAttr, 0);
                int icon = a.getResourceId(R.styleable.IconTextItem_iconDrawable, 0);
                if (icon > 0) mIcon.setImageResource(icon);
                else {
                    findViewById(R.id.view_h).setVisibility(GONE);
                }
                String txt = a.getString(R.styleable.IconTextItem_textItem);
                if (!TextUtils.isEmpty(txt)) mLeftTextView.setText(txt);
                String rightTxt = a.getString(R.styleable.IconTextItem_rightText);
                if (!TextUtils.isEmpty(rightTxt)) {
                    mRightTextView.setVisibility(VISIBLE);
                    mRightTextView.setText(rightTxt);
                }
                boolean lineVisible = a.getBoolean(R.styleable.IconTextItem_lineVisible, true);
                mLine.setVisibility(lineVisible ? VISIBLE : GONE);
                if (lineVisible) {
                    boolean full = a.getBoolean(R.styleable.IconTextItem_lineFullScreen, false);
                    if (full) {
                        mLine.setVisibility(GONE);
                        mLineFull.setVisibility(VISIBLE);
                    }
                }

                boolean rightIco_visible = a.getBoolean(R.styleable.IconTextItem_rightIcoVisible, true);
                mImgPoint.setVisibility(rightIco_visible ? VISIBLE : INVISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (a != null) a.recycle();
            }
        }
    }

    public void setRightTextView(String str) {
        mRightTextView.setVisibility(VISIBLE);
        mRightTextView.setText(str);
    }

    public void setRightTextView(int res) {
        mRightTextView.setVisibility(VISIBLE);
        mRightTextView.setText(MApplication.getAppContext().getString(res));
    }
}
