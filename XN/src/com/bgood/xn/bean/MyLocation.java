package com.bgood.xn.bean;


/**
 * @todo:存储经纬度
 * @date:2015-3-12 下午5:12:09
 * @author:hg_liuzl@163.com
 */
public class MyLocation {
	public double latitude = 0.0;
	public double longitude = 0.0;
	public MyLocation(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public MyLocation() {
		super();
	}
	
}
