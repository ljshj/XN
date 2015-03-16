package com.bgood.xn.ui.welcome;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.IndexActivity;
import com.bgood.xn.ui.MainActivity;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @todo:欢迎页
 * @date:2014-10-28 下午6:44:10
 * @author:hg_liuzl@163.com
 */
public class NavigateActivity extends BaseActivity implements TaskListenerWithState {
	private ViewPager viewpager;
	private NavigatePagerAdapter adapter;
	
	private int[] ids = {
			R.drawable.help_01,
			R.drawable.help_02,
			R.drawable.help_03,
			R.drawable.help_04,
	};
	
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
	
	/**
	 * 进入首页
	 */
	private void gotoMain(){
		Intent i = new Intent();
		i.setClass(NavigateActivity.this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
		
//		  Animation fadeOutAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.fade_out);
//		  
//		  
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
//				i.setClass(NavigateActivity.this, MainActivity.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);
//				finish();
//			}
//		});
	}
	
	public void requestUnLogin(View v){
		UserCenterRequest.getInstance().requestUnLoginBSServer(this, this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_navigate);
		
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setCurrentItem(0);
		
		adapter = new NavigatePagerAdapter(this, ids,load);
		viewpager.setAdapter(adapter);
		
//		IntentFilter mFilter = new IntentFilter();
//        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(receiverNetWork, mFilter);
	}
	
//	/**监听网络变化*/
//	public BroadcastReceiver receiverNetWork = new BroadcastReceiver(){
//		private ConnectivityManager connectivityManager;
//	    private NetworkInfo info;
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			//监听网络变化
//			if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) && pUitl.getShowWelcomePage()){	
//				connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//				info = connectivityManager.getActiveNetworkInfo();
//				if (info != null && info.isAvailable()) {//重新请求一下
//					UserCenterRequest.getInstance().requestUnLoginBSServer(NavigateActivity.this, mActivity);
//				} else {
//					BToast.show(mActivity, "没有连接网络");
//				}
//			}
//		}
//	};
//	
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		try {
//			unregisterReceiver(receiverNetWork);
//		} catch (Exception e) {
//		}
//	}
	
	private ILoadCommpletListener load = new ILoadCommpletListener(){

		@Override
		public void loadCommplet() {
			pUitl.setShowWelcomePage(true);	//已经向导完成了
			UserCenterRequest.getInstance().requestUnLoginBSServer(NavigateActivity.this, NavigateActivity.this);
		}
	};
	
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
}




