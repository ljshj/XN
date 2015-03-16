package com.bgood.xn.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.bgood.xn.location.ILocationCallback;
import com.bgood.xn.location.ILocationManager;
import com.bgood.xn.location.LocationManagerFactory;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.request.UserCenterRequest;

/**
 * @todo:定时发送位置到后台
 * @date:2015-3-9 下午10:46:09
 * @author:hg_liuzl@163.com
 */
public class TimerSendLocationService extends Service implements TaskListenerWithState {

	/**
	 * 定时获取数据的时间
	 */
	public static final int POSTDELAYED_TIME = 5*60 * 1000;		//5分钟发送一下经纬度
	private String longitude;	//经度
	private String latitude;    //纬度
	private ILocationManager mLocationManager = null;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		initLocation();
		mLocationManager.startLocation();
		handler.postDelayed(run, POSTDELAYED_TIME);
	}
	
	private Runnable run = new Runnable(){
		@Override
		public void run() {
			handler.postDelayed(this, POSTDELAYED_TIME);
			mLocationManager.refreshLocation();
			UserCenterRequest.getInstance().requestUpdateLocation(TimerSendLocationService.this, TimerSendLocationService.this, longitude, latitude);
		}
	};
	
	Handler handler = new Handler();
	
	private void initLocation() {
		mLocationManager = LocationManagerFactory.getLoactionManager(getApplicationContext());
		mLocationManager.setLocationCallback(new ILocationCallback() {
			@Override
			public void locationSuccess(BDLocation location) {
				longitude = String.valueOf(location.getLongitude());
				latitude = String.valueOf(location.getLatitude());
				mLocationManager.stopLocation();	//定位成功后，停止定位
			}
			
			@Override
			public void locationFail(int errorCode, String errorMessage) {
			}
		});

	}
	
	
	
	

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		
	}
	
}
