package com.bgood.xn.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.welcome.NavigateActivity;

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
}
