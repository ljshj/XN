package com.bgood.xn.ui;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.service.TimerSendLocationService;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.welcome.NavigateActivity;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.LoadingProgress;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @todo:加载页
 * @date:2014-10-28 下午6:49:41
 * @author:hg_liuzl@163.com
 */
public class IndexActivity extends BaseActivity implements TaskListenerWithState,OnClickListener {
	
	/**请求查看欢迎页*/
	private static final int REQUEST_WELCOME = 100;
	
	private String mAccountNumber;
	private String mPassWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_index);
		findViewById(R.id.ll_view).setOnClickListener(this);
		MobclickAgent.onEvent(IndexActivity.this,"sys_start");
		mAccountNumber = pUitl.getAccountNumber();
		mPassWord = pUitl.getAccountPassword();
		
		if(ToolUtils.hasUpdate(pUitl.getHistoryVersion(), mActivity)){	//如果当前版本大于历史版本
			pUitl.setHistoryVersion(ConfigUtil.getVersionName(mActivity));
			pUitl.setShowWelcomePage(false);	//重新展开引导页
		}
		
	//	initLoacation();
		
		//获取当前的位置
		BGApp.location = pUitl.getLocation();
		//定时上传位置
		Intent intent = new Intent(TimerSendLocationService.MY_SERVICE);
		startService(intent);
		initPage();
	}
	
//	/**
//	 * 
//	 * @todo:获取当前地址
//	 * @date:2015-3-12 下午8:55:48
//	 * @author:hg_liuzl@163.com
//	 * @params:
//	 */
//	private void initLoacation() {
//		final ILocationManager manager = LocationManagerFactory.getLoactionManager(mActivity);
//		manager.setLocationCallback(new ILocationCallback() {
//			@Override
//			public void locationSuccess(BDLocation location) {
//				final Location l = new Location();
//				l.longitude = location.getLongitude();
//				l.latitude = location.getLatitude();
//				BGApp.location = l;
//				manager.stopLocation();
//				initPage();
//			}
//			
//			@Override
//			public void locationFail(int errorCode, String errorMessage) {
//				BGApp.location = new Location();
//				manager.stopLocation();
//				initPage();
//			}
//		});
//		manager.startLocation();
//	}
	
	/**
	 * 
	 * @todo:初始化页面
	 * @date:2015-4-3 上午11:09:27
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
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
		if (ConfigUtil.isConnect(mActivity)) {	//如果联网了，先联网请求一下服务器地址
			if(!TextUtils.isEmpty(mAccountNumber) && !TextUtils.isEmpty(mPassWord)){//如果用户已经登录过了，则直接登录
				UserCenterRequest.getInstance().requestLogin(this, this, mAccountNumber, mPassWord,false);
			}else{//用户没有登录的情况
				UserCenterRequest.getInstance().requestUnLoginBSServer(this, this);
			}
		}else{//如果没有联网，直接进入
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
	}
	/**
	 * 进入使用向导
	 */
	private void gotoNavigate(){
		Intent i = new Intent();
		i.setClass(IndexActivity.this, NavigateActivity.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(i, REQUEST_WELCOME);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   super.onActivityResult(requestCode, resultCode, data);
		   if(requestCode == REQUEST_WELCOME && resultCode == RESULT_OK){
			   requestServer();
		   }else{
			   initPage(); //重头再来
		   }
		}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			switch (bNetWork.getMessageType()) {
			case 810005:	//会员登录
				LoadingProgress.getInstance().dismiss();
				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
					if(TextUtils.isEmpty(strJson)){
						return;
					}
					MemberLoginBean login = JSON.parseObject(strJson, MemberLoginBean.class);
					BGApp.isUserLogin = login.success.equalsIgnoreCase("True")?true:false;
					if(BGApp.isUserLogin){
						SystemConfig.FILE_SERVER = login.fserver;
						SystemConfig.BS_SERVER = login.bserver;
						SystemConfig.SessionID = login.userid;
						BGApp.mLoginBean = login;
						BGApp.mUserId = String.valueOf(login.userid);
						
						pUitl.setFileServerUrl(login.fserver);
						pUitl.setBSServerUrl(login.bserver);
						
						UserCenterRequest.getInstance().requestPersonInfo(IndexActivity.this, IndexActivity.this, BGApp.mUserId, false);
						LoadingProgress.getInstance().show(IndexActivity.this, "正在获取用户资料...");

					}else{
						BToast.show(IndexActivity.this, "登录失败");
					}
				}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_ERROR_PASSWORD){
					BToast.show(IndexActivity.this, "密码错误");
				}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_USER_NOTFOUND){
					BToast.show(IndexActivity.this, "手机号或能能号不存在");
				}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_USER_KILL){
					BToast.show(IndexActivity.this, "您已经被管理员禁用");
				}else{
					BToast.show(IndexActivity.this, "登录失败");
				}
				break;
			case 810009://未登录时获取业务服务器的地址,也不管成功或者失败
				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
					if(null!=body){
						SystemConfig.BS_SERVER = body.optString("bserver");
						SystemConfig.FILE_SERVER = body.optString("fserver");
						pUitl.setBSServerUrl(SystemConfig.BS_SERVER);
						pUitl.setFileServerUrl(SystemConfig.FILE_SERVER);
					}
				}
				gotoMain();
				break;
			case 820001:	//获取用户的个人资料
				LoadingProgress.getInstance().dismiss();
				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
					if(TextUtils.isEmpty(strJson)){
						return;
					}
					UserInfoBean userBean = JSON.parseObject(strJson, UserInfoBean.class);
					BGApp.mUserBean = userBean;
					doIMLogin();
				}else{
					BToast.show(IndexActivity.this, "获取个人资料失败");
				}
				break;

			default:
				break;
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_view:
			requestServer();
			break;

		default:
			break;
		}
		
	}
	
    private void doIMLogin() {
		final String username = "bg"+BGApp.mUserId;
		final String password = "banggood123";

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			LoadingProgress.getInstance().show(this,"正在登录聊天服务器");
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(username, password, new EMCallBack() {

				@Override
				public void onSuccess() {
					// 登陆成功，保存用户名密码
					BGApp.getInstance().setUserName(username);
					BGApp.getInstance().setPassword(password);
					runOnUiThread(new Runnable() {
						public void run() {
							//LoadingProgress.getInstance().show(MainActivity.this,"登录聊天服务器成功...");
							LoadingProgress.getInstance().dismiss();
							gotoMain();
						}
					});
					//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
					//boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(username+"#########");
					if(null!=BGApp.mUserBean){
						boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(BGApp.mUserBean.nickn);
					}
				}

				@Override
				public void onProgress(int progress, String status) {

				}

				@Override
				public void onError(final int code, final String message) {
					runOnUiThread(new Runnable() {
						public void run() {
							LoadingProgress.getInstance().dismiss();
							BToast.show(IndexActivity.this, "登录聊天服务器失败");
							gotoMain();
						}
					});
				}
			});
		}
	}
}
