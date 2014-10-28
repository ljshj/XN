package com.bgood.xn.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bgood.xn.view.LoadingProgress;

/**
 * @todo:获取位置类
 * @date:2014-10-28 下午6:56:35
 * @author:hg_liuzl@163.com
 */
public class LocationManager {
	// 定位相关
	public LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private double mLongitude;
	private double mLatitude;
	private LoadingProgress mProgress;
	

	public double getmLongitude() {
		return mLongitude;
	}
	public double getmLatitude() {
		return mLatitude;
	}
	
	public LocationManager(Context context,LoadingProgress pro){
		this.mProgress = pro;
		mProgress.show(context);
		mLocClient = new LocationClient( context );
		mLocClient.registerLocationListener( myListener );
		setLocationOption();
        mLocClient.start();
	}
	//设置相关参数
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//打开gps
		option.setCoorType("bd09ll");		//设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setAddrType("all");
		option.setScanSpan(3000);
		option.setPriority(LocationClientOption.NetWorkFirst);      //设置网络优先
		option.disableCache(true);		
		mLocClient.setLocOption(option);
	}
	
	/**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            	mProgress.dismiss();
            	mLongitude = location.getLongitude(); 
            	mLatitude = location.getLatitude(); 
            }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
}
