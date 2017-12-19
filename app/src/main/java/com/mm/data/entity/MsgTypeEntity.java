package com.mm.data.entity;


/**
 * Company：苗苗
 * Class Describe：消息类型实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MsgTypeEntity {

    public String msgName;

    public int resId;

    public String newMsgContent;

    public String recTime;

    public int totalNum;

    public int msgType;

    public String title;

    public MsgTypeEntity(String msgName, int resId, String title, int msgType) {

        this.msgName = msgName;
        this.resId = resId;
        this.title = title;
        this.msgType = msgType;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getNewMsgContent() {
        return newMsgContent;
    }

    public void setNewMsgContent(String newMsgContent) {
        this.newMsgContent = newMsgContent;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
