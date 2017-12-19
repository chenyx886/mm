package com.mm.data.cache;

import android.text.TextUtils;

import com.mm.data.entity.UserEntity;


/**
 * Company：苗苗
 * Class Describe： 用户实体缓存
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UserCache {


    /**
     * 缓存用户信息
     */
    private static UserEntity user;
    /**
     * 用户ID
     */
    public static final String USERID = "UserID";
    /**
     * 手机号
     */
    public static final String PHONE = "Phone";
    /**
     * 真实姓名
     */
    public static final String TRUENAME = "TrueName";
    /**
     * 昵称
     */
    public static final String NICKNAME = "NickName";
    /**
     * 头像
     */
    public static final String HEADIMG = "HeadImg";
    /**
     * 登录保存的Token
     */
    public static final String TOKEN = "token";


    /**
     * 保存登陆用户信息
     *
     * @param user
     */
    public static void put(UserEntity user) {

        PrefCache.putData(USERID, user.getUserID());
        PrefCache.putData(PHONE, user.getPhone());
        PrefCache.putData(TRUENAME, user.getTrueName());
        PrefCache.putData(NICKNAME, user.getNickName());
        PrefCache.putData(HEADIMG, user.getHeadImg());
        PrefCache.putData(TOKEN, user.getToken());

    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static UserEntity get() {

        String id = (String) (PrefCache.getData(USERID, ""));
        if (!TextUtils.isEmpty(id)) {
            if (user == null) {
                user = new UserEntity();
                user.setUserID(id);
                user.setPhone((String) PrefCache.getData(PHONE, ""));
                user.setTrueName((String) PrefCache.getData(TRUENAME, ""));
                user.setNickName((String) PrefCache.getData(NICKNAME, ""));
                user.setHeadImg((String) PrefCache.getData(HEADIMG, ""));
                user.setToken((String) PrefCache.getData(TOKEN, ""));
            }
        }
        return user;
    }

    public static void clear() {
        PrefCache.putData(USERID, "");
        PrefCache.putData(PHONE, "");
        PrefCache.putData(TRUENAME, "");
        PrefCache.putData(NICKNAME, "");
        PrefCache.putData(HEADIMG, "");
        PrefCache.putData(TOKEN, "");
        user = null;
    }

}
