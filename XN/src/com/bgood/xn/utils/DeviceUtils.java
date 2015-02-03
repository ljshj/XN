package com.bgood.xn.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 
 * @todo:设备信息类
 * @date:2015-1-21 下午3:06:47
 * @author:hg_liuzl@163.com
 */
public class DeviceUtils {

	
	/**
	 * 
	 * @todo:获取设备高度
	 * @date:2015-1-21 下午3:11:58
	 * @author:hg_liuzl@163.com
	 * @params:@param activity
	 * @params:@return
	 */
	public static int getDeviceHeight(Activity activity){
		DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	/**
	 * 
	 * @todo:获取设备宽度
	 * @date:2015-1-21 下午3:12:31
	 * @author:hg_liuzl@163.com
	 * @params:@param activity
	 * @params:@return
	 */
	public static int getDeviceWidth(Activity activity) {
		DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
}
