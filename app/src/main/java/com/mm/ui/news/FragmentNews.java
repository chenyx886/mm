package com.mm.ui.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.mm.MApplication;
import com.mm.R;
import com.mm.base.BaseMvpFragment;
import com.mm.contract.news.INodeView;
import com.mm.data.cache.DBHelper;
import com.mm.data.entity.NodeEntity;
import com.mm.presenter.news.NodePresenter;
import com.mm.widget.MLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 类描述：政讯
 * 创建人：Chenyx
 * 创建时间：2017/1/18 9:35
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class FragmentNews extends BaseMvpFragment<NodePresenter> implements INodeView {

    public static String TAG = FragmentNews.class.getName();
    /**
     * 导航
     */
    @BindView(R.id.st_news_tab)
    SmartTabLayout mStNewsTab;
    /**
     * ViewPager
     */
    @BindView(R.id.vp)
    ViewPager mVp;


    private ArrayList<FragmentAppNews> mFragments = new ArrayList<>();

    private MyPagerAdapter mAdapter;

    /**
     * 获取新闻节点
     */
    private List<NodeEntity> mNodeList = new ArrayList<>();


    @Override
    protected NodePresenter createPresenter() {
        if (null == mPresenter) {
            mPresenter = new NodePresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    protected void initUI() {
        //获取栏目
        DBHelper dbHelper = DBHelper.getInstance(MApplication.getInstance());
        mNodeList = dbHelper.queryNodeCEntyties();
        if (mNodeList.size() > 0) {
            BindViewColumn();
        } else {
            mPresenter.NodeList(0);
        }
    }

    private void BindViewColumn() {
        mFragments.clear();
        for (NodeEntity node : mNodeList) {
            mFragments.add(FragmentAppNews.getInstance(node.getNodeID()));
        }
        mAdapter = new MyPagerAdapter(getFragmentManager());
        mVp.setAdapter(mAdapter);
        mStNewsTab.setViewPager(mVp);
    }

    @Override
    public void showNode(List<NodeEntity> data) {
        mNodeList = data;
        BindViewColumn();
        DBHelper dbHelper = DBHelper.getInstance(MApplication.getInstance());
        for (NodeEntity node : mNodeList) {
            dbHelper.insertNodeEntity(node);
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mNodeList.get(position).getNodeName();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(getActivity(), "加载中");
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}

