package com.mm.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenyx.libs.utils.JumpUtil;
import com.chenyx.libs.utils.ToastUtils;
import com.chenyx.libs.utils.Toasts;
import com.mm.R;
import com.mm.base.BaseMvpActivity;
import com.mm.contract.news.INewsDetailView;
import com.mm.data.PageEntity;
import com.mm.data.cache.UserCache;
import com.mm.data.entity.CommentEntity;
import com.mm.data.entity.ShareEntity;
import com.mm.presenter.news.NewsDetailPresenter;
import com.mm.ui.LoginActivity;
import com.mm.ui.WebPhotoActivity;
import com.mm.ui.adapter.CommentAdapter;
import com.mm.ui.mine.UserInfoActivity;
import com.mm.ulits.FileUtils;
import com.mm.widget.MLoadingDialog;
import com.mm.widget.ShareDialog;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;


/**
 * Company：苗苗
 * Class Describe： 新闻详情
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class HtmlActivity extends BaseMvpActivity<NewsDetailPresenter> implements INewsDetailView {

    public static final int REQUEST = 1;

    /**
     * 返回
     */
    @BindView(R.id.tv_back)
    TextView mBack;
    /**
     * 标题布局
     */
    @BindView(R.id.headerLayout_top)
    View mHeaderLayout;
    /**
     * 分享
     */
    @BindView(R.id.iv_add)
    ImageView mShare;

    WebView webView;
    private View headerView;

    @BindView(R.id.new_fail_view)
    View netFailView;
    @BindView(R.id.loading_view)
    View loadingView;

    /**
     * 发布
     */
    @BindView(R.id.btn_send)
    Button btnSend;

    @BindView(R.id.panel_root)
    KPSwitchFSPanelLinearLayout panelRoot;
    /**
     * 写评论
     */
    @BindView(R.id.send_edt)
    EditText sendEdt;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String mUrl = "";
    private int ArticleID;

    private CommentAdapter mAdapter;

    private List<CommentEntity> mList = new ArrayList<>();

    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        setTranslucentStatus();
        mHeaderLayout.setBackgroundResource(R.color.colorPrimary);
    }

    @Override
    protected void initUI() {
        mBack.setVisibility(View.VISIBLE);
        mBack.setText("苗苗...");

        mUrl = getIntent().getStringExtra("url");
        ArticleID = getIntent().getIntExtra("ArticleID", 0);


        mAdapter = new CommentAdapter(mList);
        initCommonRecyclerView(recyclerView, mAdapter, null);
        initWeb();
        if (null != headerView) {
            mAdapter.addHeaderView(headerView);
        }
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //判断id
//             if (view.getId() == R.id.iv_img) {
//                 Log.i("tag", "点击了第" + position + "条条目的 图片");
//             } else if (view.getId() == R.id.tv_title) {
//                 Log.i("tag", "点击了第" + position + "条条目的 标题");
//             }
                Bundle bundle = new Bundle();
                bundle.putInt("UserID", mAdapter.getItem(position).getUserID());
                JumpUtil.overlay(HtmlActivity.this, UserInfoActivity.class, bundle);
            }
        });


        mShare.setVisibility(View.VISIBLE);
        mShare.setImageResource(R.mipmap.iv_share);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(REQUEST, intent);
                finish();
            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isWeiXi = false;
                PackageManager packageManager = HtmlActivity.this.getPackageManager();// 获取packagemanager
                List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
                if (pinfo != null) {
                    for (int i = 0; i < pinfo.size(); i++) {
                        String pn = pinfo.get(i).packageName;
                        if (pn.equals("com.tencent.mm")) {
                            isWeiXi = true;
                        }
                    }
                }


                if (isWeiXi) {
                    ShareEntity share = new ShareEntity();
                    share.setLinkUrl(mUrl);
                    share.setActivity(HtmlActivity.this);
                    share.setTitle(mBack.getText().toString());
                    share.setContent(mBack.getText().toString());
                    showShareDialog(share);
                } else {
                    Toasts.showShort(HtmlActivity.this, "当前您未安装微信");
                }


            }
        });

        sendEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    panelRoot.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard();
                    panelRoot.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void initWeb() {
        headerView = View.inflate(this, R.layout.layout_webview, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView = headerView.findViewById(R.id.web);

        WebSettings webSettings = webView.getSettings();

        //解决图片损坏的问题，待测
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示

        webSettings.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片

        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setBackgroundColor(0);
        webView.loadUrl(mUrl);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                netFailView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            }

            //在页面加载结束时调用
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() {\n" +
                        "\tvar imgs = document.getElementsByClassName(\"content\")[0].getElementsByTagName(\"img\");\n" +
                        "        var strs = new Array();\n" +
                        "        var img;\n" +
                        "        for (var i = 0; i < imgs.length; i++) {\n" +
                        "            img = imgs[i];\n" +
                        "            if (img.parentNode.nodeName.toLowerCase() != 'a') {\n" +
                        "            strs.push(img.src);\n" +
                        "            img.onclick = function () {\n" +
                        "                mainActivity.startPhotoActivity(strs, this.src);\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "var content =  document.getElementsByClassName( \"content\" )[0];" +
                        "mainActivity.setContent(content.innerHTML);" +
                        "})()");


                super.onPageFinished(view, url);
                webView.setOnTouchListener(null);
                if (netFailView != null)
                    netFailView.setVisibility(View.GONE);
                if (loadingView != null)
                    loadingView.setVisibility(View.GONE);
                view.getSettings().setBlockNetworkImage(false);
                Page = 0;
                mPresenter.CommentList(ArticleID, Page);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new JavascriptInterface(), "mainActivity");

    }


    String mContentInnerHtml;

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String[] imageUrl, String currentUrl) {
            Intent intent = new Intent();
            intent.putExtra("imageUrls", imageUrl);
            intent.putExtra("curImageUrl", currentUrl);
            intent.setClass(HtmlActivity.this, WebPhotoActivity.class);
            int size = FileUtils.findSize(imageUrl, currentUrl);
            if (size == -1) {
                return;
            }
            startActivity(intent);
        }

        public void setContent(String contentInnerHtml) {
            mContentInnerHtml = contentInnerHtml;
        }
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sendEdt.clearFocus();
        imm.hideSoftInputFromWindow(sendEdt.getWindowToken(), 0);
        btnSend.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (panelRoot.getVisibility() == View.VISIBLE) {
                panelRoot.setVisibility(View.GONE);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @OnClick({R.id.btn_send})
    public void onClick(View v) {
        if (UserCache.get() != null) {
            String CommentContent = sendEdt.getText().toString();
            if (TextUtils.isEmpty(CommentContent)) {
                ToastUtils.showShort("请输入内容");
                return;
            }
            hideKeyboard();
            mPresenter.CommentAdd(ArticleID, UserCache.get().getUserID(), CommentContent);

        } else {
            JumpUtil.overlay(HtmlActivity.this, LoginActivity.class);
        }

    }


    public RecyclerView initCommonRecyclerView(RecyclerView recyclerView, BaseQuickAdapter adapter, RecyclerView.ItemDecoration decoration) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            setResult(REQUEST, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void CommentList(PageEntity<CommentEntity> list) {
        if (Page == 0) {
            mList.clear();
            mList.addAll(list.getRows());
        } else {
            mList.addAll(list.getRows());
        }

        mAdapter.notifyDataSetChanged();
        ++Page;

    }

    private int Page = 0;

    @Override
    public void AddCommentSuccess() {
        ToastUtils.showShort("评论成功");
        sendEdt.setText("");
        Page = 0;
        mPresenter.CommentList(ArticleID, Page);
    }


    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    private void showShareDialog(ShareEntity entity) {

        ShareDialog shareDialog = new ShareDialog(this, entity, R.style.showDialog);
        shareDialog.showDialog();
    }
}
