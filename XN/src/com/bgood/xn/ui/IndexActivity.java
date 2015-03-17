package com.bgood.xn.ui;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import com.baidu.location.BDLocation;
import com.bgood.xn.R;
import com.bgood.xn.bean.Location;
import com.bgood.xn.location.ILocationCallback;
import com.bgood.xn.location.ILocationManager;
import com.bgood.xn.location.LocationManagerFactory;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.service.SyncIMDataService;
import com.bgood.xn.service.TimerSendLocationService;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.welcome.NavigateActivity;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.view.BToast;
import com.umeng.analytics.MobclickAgent;

/**
 * @todo:加载页
 * @date:2014-10-28 下午6:49:41
 * @author:hg_liuzl@163.com
 */
public class IndexActivity extends BaseActivity implements TaskListenerWithState {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_index);
		MobclickAgent.onEvent(IndexActivity.this,"sys_start");
		initLoacation();
		//定时上传位置
		Intent intent = new Intent(TimerSendLocationService.MY_SERVICE);
		startService(intent);
	}
	/**
	 * 
	 * @todo:获取当前地址
	 * @date:2015-3-12 下午8:55:48
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void initLoacation() {
		final ILocationManager manager = LocationManagerFactory.getLoactionManager(mActivity);
		manager.setLocationCallback(new ILocationCallback() {
			@Override
			public void locationSuccess(BDLocation location) {
				final Location l = new Location();
				l.longitude = location.getLongitude();
				l.latitude = location.getLatitude();
				BGApp.location = l;
				manager.stopLocation();
				initPage();
			}
			
			@Override
			public void locationFail(int errorCode, String errorMessage) {
				BGApp.location = new Location();
				manager.stopLocation();
				initPage();
			}
		});
		manager.startLocation();
	}
	
	private void initPage() {
		if(!pUitl.getShowWelcomePage()){	//如果没有向导过
			gotoNavigate();
		}else{//已经向导过了
			requestServer();
		}
	}
	
	/**
	 * 
	 * @todo:获取服务器地址
	 * @date:2015-3-13 上午10:10:02
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void requestServer() {
		//先把业务器地址与文件服务器地址赋值.,如有需要会重新赋值的
		SystemConfig.BS_SERVER = pUitl.getBSServerUrl();
		SystemConfig.FILE_SERVER = pUitl.getFileServerUrl();
		if (ConfigUtil.isConnect(mActivity)) {
			UserCenterRequest.getInstance().requestUnLoginBSServer(this, this);
		}else{
			gotoMain();
		}
	}
	
	/**
	 * 进入主界面
	 */
	private void gotoMain(){
		
		Intent i = new Intent();
		i.setClass(IndexActivity.this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
//		  Animation fadeOutAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.push_top_out2);
//		  v.setAnimation(fadeOutAnimation);
//		  fadeOutAnimation.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation arg0) {
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation arg0) {
//			}
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				Intent i = new Intent();
//				i.setClass(IndexActivity.this, MainActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);
//				finish();
//			}
//		});
//		  v.startAnimation(fadeOutAnimation);
	}
	/**
	 * 进入使用向导
	 */
	private void gotoNavigate(){
		Intent i = new Intent();
		i.setClass(IndexActivity.this, NavigateActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				SystemConfig.BS_SERVER = body.optString("bserver");
				SystemConfig.FILE_SERVER = body.optString("fserver");
				
				pUitl.setBSServerUrl(SystemConfig.BS_SERVER);
				pUitl.setFileServerUrl(SystemConfig.FILE_SERVER);
				
				gotoMain();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
