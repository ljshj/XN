package com.bgood.xn.utils.location;

import android.content.Context;

public class LocationManagerFactory {

	private LocationManagerFactory() {
	}
	
	public static LocationManager getLoactionManager(Context context){
		return new BaiduLocationManager(context);
	}

}
