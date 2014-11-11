package com.bgood.xn.utils.location;



public interface LocationCallback {
	void locationSuccess(KDLocation location);
	void locationFail(int errorCode,String errorMessage);
}
