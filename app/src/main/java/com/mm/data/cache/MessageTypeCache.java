package com.mm.data.cache;


import android.app.Activity;
import android.content.Context;

import com.mm.R;
import com.mm.data.entity.MsgTypeEntity;
import com.mm.ui.msg.UnReadMsgListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Company：苗苗
 * Class Describe： 消息分类实体缓存
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MessageTypeCache {


    private static Map<String, Class<? extends Activity>> msgTypeMapView = new HashMap<>();

    private static List<MsgTypeEntity> msItems = new ArrayList<>();

    /**
     * 初始化业务类型实体
     *
     * @param context
     */
    public static void initBizTypeItems(Context context) {

        msItems.clear();

        MsgTypeEntity msgTypeBean1 = new MsgTypeEntity(context.getResources().getString(R.string.news_msg),
                R.mipmap.ic_task_notify, "暂无新消息", 1);
        MsgTypeEntity msgTypeBean2 = new MsgTypeEntity(context.getResources().getString(R.string.find_msg),
                R.mipmap.ic_notice_notify, "暂无新消息", 2);
        MsgTypeEntity msgTypeBean3 = new MsgTypeEntity(context.getResources().getString(R.string.sys_msg),
                R.mipmap.ic_warning_notify, "暂无新消息", 3);

        msItems.add(msgTypeBean1);
        msItems.add(msgTypeBean2);
        msItems.add(msgTypeBean3);
    }


    /**
     * msgType匹配的View
     */
    public static void initMsgMapView() {

        msgTypeMapView.clear();

        //新闻消息
        msgTypeMapView.put("1", UnReadMsgListActivity.class);
        //发现消息
        msgTypeMapView.put("2", UnReadMsgListActivity.class);
        //系统消息
        msgTypeMapView.put("3", UnReadMsgListActivity.class);


    }

    /**
     * 根据msgType获取对应的跳转界面
     *
     * @param busType
     */
    public static Class<? extends Activity> getMsgTypeView(String busType) {

        return msgTypeMapView.get(busType);
    }

    public static List<MsgTypeEntity> getMsgTypeBeans() {

        return msItems;
    }
}
