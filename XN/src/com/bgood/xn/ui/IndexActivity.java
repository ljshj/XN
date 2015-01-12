package com.bgood.xn.ui;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.welcome.NavigateActivity;
import com.bgood.xn.view.BToast;

/**
 * 
 * @todo:加载页
 * @date:2014-10-28 下午6:49:41
 * @author:hg_liuzl@163.com
 */
public class IndexActivity extends BaseActivity implements TaskListenerWithState {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_index);
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiverNetWork, mFilter);
		UserCenterRequest.getInstance().requestUnLoginBSServer(this, this);
	}
	
	private void startIndex(){
		if(pUitl.getShowWelcomePage()){//已经向导过
			gotoMain();
		}else{
			gotoNavigate();
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
	}
	/**
	 * 进入使用向导
	 */
	private void gotoNavigate(){
		Intent i = new Intent();
		i.setClass(IndexActivity.this, NavigateActivity.class);
		i.putExtra("from_index", true);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiverNetWork);
	}
	

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				SystemConfig.BS_SERVER = body.optString("bserver");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startIndex();
			}
		}
	}
	
	/**监听网络变化*/
	public BroadcastReceiver receiverNetWork = new BroadcastReceiver(){
		private ConnectivityManager connectivityManager;
	    private NetworkInfo info;
		@Override
		public void onReceive(Context context, Intent intent) {
			//监听网络变化
			if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){	
				connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {//重新请求一下
					UserCenterRequest.getInstance().requestUnLoginBSServer(IndexActivity.this, mActivity);
				} else {
					BToast.show(mActivity, "没有连接网络");
				}
			}
		}
	};
	
}
