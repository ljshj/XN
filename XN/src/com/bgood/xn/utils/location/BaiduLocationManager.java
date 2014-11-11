package com.bgood.xn.utils.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiduLocationManager implements LocationManager {
	private Context context;
	private LocationCallback locationCallback;
	private LocationClient mLocationClient = null;
	private KdweiboLocationListener locationListener = new KdweiboLocationListener();
	private long maxLocationTime = 30*1000;  //最大定位时间 默认30秒
	private long startLocationTime = 0; //开始定位时间
	private BDLocation lastBDLocation;
	private int locationCount = 0;
	private static int maxCount = 1;
	
	public BaiduLocationManager(Context context) {
		this.context = context;
		initLocationClient();
	}

	public long getMaxLocationTime() {
		return maxLocationTime;
	}

	public void setMaxLocationTime(long maxLocationTime) {
		this.maxLocationTime = maxLocationTime;
	}

	@Override
	public void setLocationCallback(LocationCallback lc) {
		this.locationCallback = lc;
	}

	@Override
	public void startLocation() {
			startLocationTime = System.currentTimeMillis();
			mLocationClient.start();
			//Log.d("BaiduLocationManager", "is start:"+isStarted());
	}

	@Override
	public void stopLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()){
			mLocationClient.stop();
		}
	}
	
	@Override
	public boolean isStarted() {
		return mLocationClient.isStarted();
	}
	
	private void initLocationClient(){
    	mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(locationListener);
        setLocationOption();
    }
	
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationMode.Battery_Saving);
        String packageName = this.context.getPackageName();
        option.setProdName(packageName);
        option.setCoorType("gcj02");//返回的定位结果是国测经纬度,gcj02
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
        mLocationClient.setLocOption(option);
	}
	
	public class KdweiboLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			//Log.d("BaiduLocationManager", "start："+locationCount);

			if(location != null && location.getLocType()==161||location.getLocType()==61||location.getLocType()==65){
				if(locationCallback != null){
					//Log.d("BaiduLocationManager", "定位成功"+location.getLatitude()+","+location.getLongitude());
					lastBDLocation = location;
					locationCount = locationCount + 1;
				}
				
			}else{
				int errorCode=0;
				if(location!=null){
					errorCode = location.getLocType();
				}
				if(locationCallback != null){
					locationCallback.locationFail(errorCode,"定位");
				}
				stopLocation();
			}
				

			if(lastBDLocation != null && (locationCount >= maxCount || System.currentTimeMillis() - startLocationTime > maxLocationTime)){
				//Log.d("BaiduLocationManager", "locationCount："+locationCount);
				locationCount = 0;
				locationCallback.locationSuccess(BDLocationToKDLocation(lastBDLocation));
				stopLocation();
			}
			
		}
		
//		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	public void setLocationMode(int mode){
		if(mLocationClient != null){
			LocationClientOption option = mLocationClient.getLocOption();
			if(option != null){
				if(mode == 1){
					option.setLocationMode(LocationMode.Hight_Accuracy);
		        }else if(mode == 0){
		        	option.setLocationMode(LocationMode.Battery_Saving);
		        }
			}
		}
	}
	
	public static KDLocation BDLocationToKDLocation(BDLocation bdLocation){
		//TODO
		KDLocation kdLocation = new KDLocation();
		kdLocation.setLatitude(bdLocation.getLatitude());
		kdLocation.setLongitude(bdLocation.getLongitude());
		return kdLocation;
	}

	

}
