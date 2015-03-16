package com.bgood.xn.location;


/**
 * @todo: 定位操作接口
 * @date:2015-3-9 ����4:22:41
 * @author:hg_liuzl@163.com
 */
public interface ILocationManager {
	
	public void startLocation();
	public void stopLocation();
	public void refreshLocation();
	public void setLocationCallback(ILocationCallback callback);
	public boolean isStarted();
	
}
