package com.mm.data.entity;

/**
 * Company：苗苗
 * Class Describe： 用户实体
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class UserEntity extends BaseEntity {


    /**
     * 用户ID
     */
    private String UserID;
    /**
     * 手机号
     */
    private String Phone;
    /**
     * 真实姓名
     */
    private String TrueName;
    /**
     * 昵称
     */
    private String NickName;
    /**
     * 性别
     */
    private String Sex;
    /**
     * 学校
     */
    private String SchoolTag;
    /**
     * 出生年月
     */
    private String BirthDate;
    /**
     * 头像
     */
    private String HeadImg;
    /**
     * 状态
     */
    private String State;
    /**
     * token 值
     */
    private String Token;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getSchoolTag() {
        return SchoolTag;
    }

    public void setSchoolTag(String schoolTag) {
        SchoolTag = schoolTag;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
