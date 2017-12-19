package com.mm.data.entity;

import java.util.List;

/**
 * Company：苗苗
 * Class Describe：发现实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class FindEntity extends BaseEntity {

    private int FindID;
    private int UserID;
    private String Title;
    private String NickName;
    private String HeadImg;
    private String Content;
    private int RNum;
    private int Laud;
    private int CNum;//评论次数
    private boolean IsElite;
    private String CTime;
    private int Type;
    private int State;

    private List<ImageEntity> ImgList;

    public int getFindID() {
        return FindID;
    }

    public void setFindID(int findID) {
        FindID = findID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getRNum() {
        return RNum;
    }

    public void setRNum(int RNum) {
        this.RNum = RNum;
    }

    public int getLaud() {
        return Laud;
    }

    public void setLaud(int laud) {
        Laud = laud;
    }

    public int getCNum() {
        return CNum;
    }

    public void setCNum(int CNum) {
        this.CNum = CNum;
    }

    public boolean isElite() {
        return IsElite;
    }

    public void setElite(boolean elite) {
        IsElite = elite;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public List<ImageEntity> getImgList() {
        return ImgList;
    }

    public void setImgList(List<ImageEntity> imgList) {
        ImgList = imgList;
    }

}
