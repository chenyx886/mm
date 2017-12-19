package com.mm.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.chenyx.libs.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import com.mm.R;
import com.mm.ulits.FileUtils;

import java.io.File;

/**
 * Company：苗苗
 * Class Describe： 图片点击放大
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */


public class WebPhotoActivity extends Activity implements View.OnClickListener {
    private ImageView crossIv;
    private ViewPager mPager;
    private ImageView loadingIv;
    private TextView photoOrderTv;
    private TextView saveTv;
    private String curImageUrl = "";
    private String[] imageUrls = new String[]{};

    private int curPosition = -1;
    private int[] initialedPositions = null;
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_photo);
        imageUrls = getIntent().getStringArrayExtra("imageUrls");
        curImageUrl = getIntent().getStringExtra("curImageUrl");
        initialedPositions = new int[imageUrls.length];
        initInitialedPositions();

        photoOrderTv = findViewById(R.id.photo_order_tv);
        saveTv = findViewById(R.id.photo_save_tv);
        saveTv.setOnClickListener(this);

        loadingIv = findViewById(R.id.photo_loading_iv);
        crossIv = findViewById(R.id.photo_cross_iv);
        crossIv.setOnClickListener(this);

        mPager = findViewById(R.id.photo_browser_pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                if (imageUrls[position] != null && !"".equals(imageUrls[position])) {
                    final PhotoView view = new PhotoView(WebPhotoActivity.this);
                    view.enable();
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    Picasso.with(WebPhotoActivity.this)
                            .load(imageUrls[position]).config(Bitmap.Config.RGB_565)
                            .into(view, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    occupyOnePosition(position);
                                    if (position == curPosition) {
                                        hideLoadingAnimation();
                                    }
                                }

                                @Override
                                public void onError() {
                                    if (position == curPosition) {
                                        hideLoadingAnimation();
                                    }
                                    showErrorLoading();
                                }
                            });

                    container.addView(view);
                    return view;
                }
                return null;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                releaseOnePosition(position);
                container.removeView((View) object);
            }

        });

        curPosition = returnClickedPosition() == -1 ? 0 : returnClickedPosition();
        mPager.setCurrentItem(curPosition);
        mPager.setTag(curPosition);
        if (initialedPositions[curPosition] != curPosition) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
            showLoadingAnimation();
        }
        photoOrderTv.setText((curPosition + 1) + "/" + imageUrls.length);//设置页面的编号

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (initialedPositions[position] != position) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
                    showLoadingAnimation();
                } else {
                    hideLoadingAnimation();
                }
                curPosition = position;
                photoOrderTv.setText((position + 1) + "/" + imageUrls.length);//设置页面的编号
                mPager.setTag(position);//为当前view设置tag
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int returnClickedPosition() {
        if (imageUrls == null || curImageUrl == null) {
            return -1;
        }
        for (int i = 0; i < imageUrls.length; i++) {
            if (curImageUrl.equals(imageUrls[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.photo_cross_iv) {
            this.finish();
        }
        if (id == R.id.photo_save_tv) {
            savePhotoToLocal();
        }

    }

    private void showLoadingAnimation() {
        loadingIv.setVisibility(View.VISIBLE);
        loadingIv.setImageResource(R.mipmap.loading);
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(loadingIv, "rotation", 0f, 360f);
            objectAnimator.setDuration(2000);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                objectAnimator.setAutoCancel(true);
            }
        }
        objectAnimator.start();
    }

    private void hideLoadingAnimation() {
        releaseResource();
        loadingIv.setVisibility(View.GONE);
    }

    private void showErrorLoading() {
        loadingIv.setVisibility(View.VISIBLE);
        releaseResource();
        loadingIv.setImageResource(R.mipmap.load_error);
    }

    private void releaseResource() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (loadingIv.getAnimation() != null) {
            loadingIv.getAnimation().cancel();
        }
    }

    private void occupyOnePosition(int position) {
        initialedPositions[position] = position;
    }

    private void releaseOnePosition(int position) {
        initialedPositions[position] = -1;
    }

    private void initInitialedPositions() {
        for (int i = 0; i < initialedPositions.length; i++) {
            initialedPositions[i] = -1;
        }
    }

    private void savePhotoToLocal() {
        ViewGroup containerTemp = mPager.findViewWithTag(mPager.getCurrentItem());
        if (containerTemp == null) {
            return;
        }
        PhotoView photoViewTemp = (PhotoView) containerTemp.getChildAt(0);
        if (photoViewTemp != null) {


            BitmapDrawable bitmapDrawable = (BitmapDrawable) photoViewTemp.getDrawable();
            if (bitmapDrawable == null) {
                return;
            }
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap == null) {
                return;
            }
            FileUtils.savePhoto(this, bitmap, new FileUtils.SaveResultCallback() {
                @Override
                public void onSavedSuccess(final File file) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WebPhotoActivity.this, "已保存至SD卡news_photo文件夹", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onSavedFailed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showShort("保存失败");
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        releaseResource();
        if (mPager != null) {
            mPager.removeAllViews();
            mPager = null;
        }
        super.onDestroy();
    }
}
