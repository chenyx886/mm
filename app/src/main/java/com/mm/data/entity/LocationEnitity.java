package com.mm.data.entity;


/**
 * Company：苗苗
 * Class Describe：定位实体
 * Create Person：Chenyx
 * Create Time：2017/11/24 上午10:52
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class LocationEnitity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 设备id
     */
    private String devId;

    /**
     * 网络运营商
     */
    private String netOprt;

    /**
     * 省名称
     */
    private String province;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 区名称
     */
    private String district;

    /**
     * 街道地址
     */
    private String street;

    /**
     * 街道名称
     */
    private String streetnumber;

    /**
     * 经度
     */
    private double lng;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态: N 正常 E 异常
     */
    private String status;

    /**
     * 定位时间
     */
    private String lctTime;


    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the devId
     */
    public String getDevId() {
        return devId;
    }

    /**
     * @param devId the devId to set
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * @return the netOprt
     */
    public String getNetOprt() {
        return netOprt;
    }

    /**
     * @param netOprt the netOprt to set
     */
    public void setNetOprt(String netOprt) {
        this.netOprt = netOprt;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the cityCode
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * @param cityCode the cityCode to set
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the streetnumber
     */
    public String getStreetnumber() {
        return streetnumber;
    }

    /**
     * @param streetnumber the streetnumber to set
     */
    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the lctTime
     */
    public String getLctTime() {
        return lctTime;
    }

    /**
     * @param lctTime the lctTime to set
     */
    public void setLctTime(String lctTime) {
        this.lctTime = lctTime;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserCoorReportEnitity [userId=" + userId + ", devId=" + devId + ", netOprt=" + netOprt + ", province="
                + province + ", cityCode=" + cityCode + ", city=" + city + ", district=" + district + ", street="
                + street + ", streetnumber=" + streetnumber + ", lng=" + lng + ", lat=" + lat + ", address=" + address
                + ", status=" + status + ", lctTime=" + lctTime + "]";
    }
}
