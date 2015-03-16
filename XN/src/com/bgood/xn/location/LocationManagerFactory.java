package com.bgood.xn.location;

import android.content.Context;


/**
 * 
 * @todo:定位工厂类
 * @date:2015-3-10 上午9:48:33
 * @author:hg_liuzl@163.com
 */
public class LocationManagerFactory {

	private LocationManagerFactory() {
	}
	
	public static ILocationManager getLoactionManager(Context context){
		return new MyLocationManager(context);
	}

}
