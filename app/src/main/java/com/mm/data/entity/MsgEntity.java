package com.mm.data.entity;

/**
 * Company：苗苗
 * Class Describe：消息推送基础表
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class MsgEntity extends BaseEntity {
    /**
     * 主键
     */
    private int ID;
    /**
     * 主键
     */
    private int MsgID;

    /**
     * 标题
     */
    private String Title;

    /**
     * 内容
     */
    private String Content;

    /**
     * 发送人id
     */
    private int UserID;
    /**
     * 业务id
     */
    private int BusId;

    /**
     * 1,新闻消息；2，发现消息；3，系统消息
     */
    private int MsgType;

    /**
     * 创建时间
     */
    private String CTime;

    /**
     * 消息状态：1，正常；2，禁用
     */
    private int Status;

    /**
     * 是否查看:1是；2，否
     */
    private int IsLook;
    /**
     * 消息数量
     */
    private int MsgCount;

    /**
     * 消息接收id
     */
    private int RUserID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getMsgID() {
        return MsgID;
    }

    public void setMsgID(int msgID) {
        MsgID = msgID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getBusId() {
        return BusId;
    }

    public void setBusId(int busId) {
        BusId = busId;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getIsLook() {
        return IsLook;
    }

    public void setIsLook(int isLook) {
        IsLook = isLook;
    }

    public int getMsgCount() {
        return MsgCount;
    }

    public void setMsgCount(int msgCount) {
        MsgCount = msgCount;
    }

    public int getRUserID() {
        return RUserID;
    }

    public void setRUserID(int RUserID) {
        this.RUserID = RUserID;
    }
}