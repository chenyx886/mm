package com.mm.data.cache;

import com.chenyx.libs.utils.SPs;
import com.mm.MApplication;

/**
 * Company：苗苗
 * Class Describe： SharedPreferences 数据管理类
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class PrefCache {

    public static void putData(String key, Object object) {

        SPs.put(MApplication.getAppContext(), key, object);
    }

    public static Object getData(String key, Object defaultObject) {

        return SPs.get(MApplication.getAppContext(), key, defaultObject);
    }

    public static void removeData(String key) {
        SPs.remove(MApplication.getAppContext(), key);
    }

    public static void clearData() {
        SPs.clear(MApplication.getAppContext());
    }

    public boolean contains(String key) {

        return SPs.contains(MApplication.getAppContext(), key);
    }
}
