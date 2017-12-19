package com.mm.data.entity;

/**
 * Company：苗苗
 * Class Describe：签到实体
 * Create Person：Chenyx
 * Create Time：2017/11/29 下午7:44
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class SignEntity {
    private int ID;
    private int UserID;
    private double Longitude;
    private double Latitude;
    private String Adderss;
    private String Explain;
    private String CTime;
    private String Remark;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getAdderss() {
        return Adderss;
    }

    public void setAdderss(String adderss) {
        Adderss = adderss;
    }

    public String getExplain() {
        return Explain;
    }

    public void setExplain(String explain) {
        Explain = explain;
    }

    public String getCTime() {
        return CTime;
    }

    public void setCTime(String CTime) {
        this.CTime = CTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
