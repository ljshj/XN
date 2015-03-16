package com.bgood.xn.location;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.bgood.xn.R;

/**
 * @todo:定位测试
 * @date:2015-3-9 ����4:47:31
 * @author:hg_liuzl@163.com
 */
public class MyLocationActivity extends Activity implements OnClickListener {
	
	private ILocationManager mLocationManager = null;
	private TextView tvShowLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_alocation);
		tvShowLocation = (TextView) findViewById(R.id.tv_location);
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_again).setOnClickListener(this);
		findViewById(R.id.btn_stop).setOnClickListener(this);
		initLocation();
	}
	
	
	private void initLocation() {
		mLocationManager = LocationManagerFactory.getLoactionManager(getApplicationContext());
		mLocationManager.setLocationCallback(new ILocationCallback() {
			
			@Override
			public void locationSuccess(BDLocation location) {
				StringBuffer sb = new StringBuffer(256);
				sb.append("time : ");
				sb.append(location.getTime());
				sb.append("\nerror code : ");
				sb.append(location.getLocType());
				sb.append("\nlatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nradius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation){
					sb.append("\nspeed : ");
					sb.append(location.getSpeed());
					sb.append("\nsatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
					/**
					 * 格式化显示地址信息
					 */
					sb.append("\naddr : ");
					sb.append(location.getAddrStr());
				}
				sb.append("\nisCellChangeFlag : ");
				sb.append(location.isCellChangeFlag());
				tvShowLocation.setText(sb.toString());
			}
			
			@Override
			public void locationFail(int errorCode, String errorMessage) {
				Toast.makeText(MyLocationActivity.this, "��λʧ��", Toast.LENGTH_SHORT).show();
			}
		});

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			mLocationManager.startLocation();
			break;
					
		case R.id.btn_stop:
			mLocationManager.stopLocation();
			break;

		default:
			break;
		}
		
	}
	
	
}
