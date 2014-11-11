package com.bgood.xn.utils.location;

public interface LocationManager {
	public void startLocation();
	public void stopLocation();
	public void setLocationCallback(LocationCallback lc);
	public boolean isStarted();
	public void setLocationMode(int mode);
}
