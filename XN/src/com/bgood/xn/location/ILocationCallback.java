package com.bgood.xn.location;

import com.baidu.location.BDLocation;

/**
 * 
 * @todo:定位状态回调
 * @date:2015-3-9 下午10:25:17
 * @author:hg_liuzl@163.com
 */
public interface ILocationCallback {
	
	void locationSuccess(BDLocation location);
	
	void locationFail(int errorCode,String errorMessage);
	
}
