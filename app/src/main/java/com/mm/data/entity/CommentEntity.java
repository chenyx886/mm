package com.mm.data.entity;


import java.util.List;
/**
 * Company：苗苗
 * Class Describe：评论实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class CommentEntity extends BaseEntity {


    private int ID;
    private int ArticleID;
    private int FindID;
    private int PID;
    private String Content;
    private String CTime;
    private int State;
    private int UserID;
    private String NickName;
    private String Phone;
    private String HeadImg;
    private String BRUserID;//被回复人ID
    private String BRNickName;//被回复人昵称
    private String Laud; //点赞数量
    /**
     * 回复人列表
     */
    private List<CommentEntity> RInfo;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getArticleID() {
        return ArticleID;
    }

    public void setArticleID(int articleID) {
        ArticleID = articleID;
    }

    public int getFindID() {
        return FindID;
    }

    public void setFindID(int findID) {
        FindID = findID;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public String getBRUserID() {
        return BRUserID;
    }

    public void setBRUserID(String BRUserID) {
        this.BRUserID = BRUserID;
    }

    public String getBRNickName() {
        return BRNickName;
    }

    public void setBRNickName(String BRNickName) {
        this.BRNickName = BRNickName;
    }

    public String getLaud() {
        return Laud;
    }

    public void setLaud(String laud) {
        Laud = laud;
    }

    public List<CommentEntity> getRInfo() {
        return RInfo;
    }

    public void setRInfo(List<CommentEntity> RInfo) {
        this.RInfo = RInfo;
    }
}
