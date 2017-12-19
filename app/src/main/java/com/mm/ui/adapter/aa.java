package com.mm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mm.R;
import com.mm.data.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/12/8 上午10:22
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class aa extends BaseAdapter {
    List<UserEntity> mlist = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private Context context;
    private ListView listView;

    public aa(Context context, ListView listView, List<UserEntity> games) {
        this.context = context;
        this.mlist = games;
        this.listView = listView;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_sign_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserEntity u = mlist.get(position);
        viewHolder.itemName.setText(u.getNickName());

        return convertView;

    }


    /**
     * 局部刷新
     *
     * @param mListView
     * @param posi
     */
    public void updateSingleRow(ListView mListView, int posi, String str) {
        if (mListView != null) {
            //获取第一个显示的item
            int visiblePos = mListView.getFirstVisiblePosition();
            //计算出当前选中的position和第一个的差，也就是当前在屏幕中的item位置
            int offset = posi - visiblePos;
            int lenth = mListView.getChildCount();
            // 只有在可见区域才更新,因为不在可见区域得不到Tag,会出现空指针,所以这是必须有的一个步骤
            if ((offset < 0) || (offset >= lenth)) return;
            View convertView = mListView.getChildAt(offset);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            //以下是处理需要处理的控件
            System.out.println("posi = " + posi);
            UserEntity game = mlist.get(posi);
            viewHolder.itemName.setText(str);
        }
    }

    private class ViewHolder {

        TextView itemName;

        public ViewHolder(View convertView) {
            itemName = convertView.findViewById(R.id.tv_title);
        }
    }

}
