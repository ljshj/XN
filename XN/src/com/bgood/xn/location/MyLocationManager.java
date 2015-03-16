package com.bgood.xn.location;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 
 * @todo:定位操作实现
 * @date:2015-3-9 下午10:24:51
 * @author:hg_liuzl@163.com
 */
public class MyLocationManager implements ILocationManager {
	private Context context;
	private ILocationCallback locationCallback;
	private LocationClient mLocationClient = null;
	private MyLocationListener locationListener = new MyLocationListener();
	private long maxLocationTime = 30*1000;  //���λʱ�� Ĭ��30��
	private long startLocationTime = 0; //��ʼ��λʱ��
	private BDLocation lastBDLocation;
	private int locationCount = 0;
	private static int maxCount = 1;
	
	public MyLocationManager(Context context) {
		this.context = context;
		initLocationClient();
	}

	public long getMaxLocationTime() {
		return maxLocationTime;
	}

	public void setMaxLocationTime(long maxLocationTime) {
		this.maxLocationTime = maxLocationTime;
	}

	public void refreshLocation(){
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("msg", "locClient is null or not started");
	}
	
	private void initLocationClient(){
    	mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(locationListener);
        setLocationOption();
    }
	
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
      //  option.setLocationMode(LocationMode.Battery_Saving);
        String packageName = this.context.getPackageName();
        option.setProdName(packageName);
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
	}
	
	@Override
	public void startLocation() {
		startLocationTime = System.currentTimeMillis();
		mLocationClient.start();
	}

	@Override
	public void stopLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()){
			mLocationClient.stop();
		}
	}

	@Override
	public void setLocationCallback(ILocationCallback callback) {
		this.locationCallback = callback;
	}

	@Override
	public boolean isStarted() {
		return mLocationClient.isStarted();
	}
	
	/**
	 * @todo:TODO
	 * @date:2015-3-9 ����4:27:31
	 * @author:hg_liuzl@163.com
	 */
	
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location != null && location.getLocType()==161||location.getLocType()==61||location.getLocType()==65){
				if(locationCallback != null){
					lastBDLocation = location;
					locationCount = locationCount + 1;
				}
			}else{
				int errorCode=0;
				if(location!=null){
					errorCode = location.getLocType();
				}
				if(locationCallback != null){
					locationCallback.locationFail(errorCode,"失败");
				}
				stopLocation();
			}

			if(lastBDLocation != null && (locationCount >= maxCount || System.currentTimeMillis() - startLocationTime > maxLocationTime)){
				locationCount = 0;
				locationCallback.locationSuccess(location);
				stopLocation();
			}
		}

		@Override
		public void onReceivePoi(BDLocation location) {
			if (null==location) {
				return;
			}
		}
	}
}
