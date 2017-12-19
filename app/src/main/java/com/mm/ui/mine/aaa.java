package com.mm.ui.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mm.R;
import com.mm.data.entity.UserEntity;
import com.mm.ui.adapter.aa;

import java.util.ArrayList;
import java.util.List;

/**
 * Company：苗苗
 * Class Describe：
 * Create Person：Chenyx
 * Create Time：2017/12/8 上午10:33
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class aaa extends Activity {
    List<UserEntity> mist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.aaaa);

        for (int i = 0; i < 6; i++) {

            UserEntity u = new UserEntity();
            u.setNickName("q" + i);
            mist.add(u);
        }

        final ListView mlist = findViewById(R.id.list);
        final aa a = new aa(this, mlist, mist);
        mlist.setAdapter(a);

        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                a.updateSingleRow(mlist, position,"123");
            }
        });
    }
}
