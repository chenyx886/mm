package com.location;

/**
 * @ClassName: LocationInfo
 * @Description: 定位信息实体
 * @author Chenyx
 * @date 2016年6月10日 下午4:17:12
 * 
 */
public class LocationInfo {

	private double Longitude;
	private double Latitude;
	private String Province = "";
	private String City = "";
	private String District = "";
	private String Street = "";
	private String Address = "";

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

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
}
