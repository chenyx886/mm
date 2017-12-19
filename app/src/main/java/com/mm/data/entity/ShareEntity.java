package com.mm.data.entity;

import android.app.Activity;

/**
 * Company：苗苗
 * Class Describe：分享内容的实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class ShareEntity {
    /**
     * 分享的界面
     */
    private Activity activity;
    /**
     * 分享图片的url
     */
    private String imgUrl;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 链接
     */
    private String linkUrl;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

}
