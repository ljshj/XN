package com.bgood.xn.network.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.bean.response.WeiqiangResponse;
import com.bgood.xn.db.PreferenceUtil;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.system.SystemConfig.ServerType;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.utils.LogUtils;
import com.bgood.xn.view.LoadingProgress;

public class HttpRequestAsyncTask extends AsyncTask<Void, Void,HttpResponseInfo > {
	private String serverUrl = null;	//服务器地址
	private HttpRequestInfo mRequest;
	private TaskListenerWithState mListenerWithState;
	private Context context;
	private BaseNetWork bNetWork;
	private boolean mCommonLoading = true;	//进度条是通用的
	private ServerType type;
	private PreferenceUtil pUtil;
	
	public HttpRequestAsyncTask(boolean commonLoading,ServerType type,BaseNetWork b,TaskListenerWithState listner,Context c) {
		this.mCommonLoading = commonLoading;
		this.bNetWork = b;
		this.context=c;
		this.type = type;
		this.mRequest = HttpRequestInfo.getHttpRequestInfoInstance();
		setRequestUrl(type, b, listner);
	}
	
	public HttpRequestAsyncTask(ServerType type,BaseNetWork b,TaskListenerWithState listner,Context c) {
		this.mCommonLoading = true;
		this.bNetWork = b;
		this.context=c;
		this.type = type;
		this.mRequest = HttpRequestInfo.getHttpRequestInfoInstance();
		setRequestUrl(type, b, listner);
	}
	

	
	private void setRequestUrl(ServerType type,BaseNetWork b,TaskListenerWithState listner) {
			pUtil = new PreferenceUtil(context, PreferenceUtil.PREFERENCE_FILE);
			//1.服务器分配地址 2.登录后的操作要分配session
			if(type == ServerType.LoginServer){
				serverUrl = SystemConfig.HttpServer;
				
			}else if(type == ServerType.BusinessServer){
				serverUrl = null!=SystemConfig.BS_SERVER ? SystemConfig.BS_SERVER:pUtil.getBSServerUrl() ;
				b.setSessionID(SystemConfig.SessionID);
				
			}else if(type == ServerType.IMServer){
				serverUrl = SystemConfig.IM_SERVER;
				b.setSessionID(SystemConfig.SessionID);
				
			}else if(type == ServerType.FileServer){
				serverUrl = (null!=SystemConfig.FILE_SERVER ? SystemConfig.FILE_SERVER:pUtil.getFileServerUrl())+"Upload.ashx"+b.getConnUrl();	//注意这里是文件类型
				b.setSessionID(SystemConfig.SessionID);
				
			}else{
				serverUrl = SystemConfig.HttpServer;
			}
			
			if(!serverUrl.contains("http://")){
				mRequest.setRequesUrl("http://"+serverUrl);
			}else{
				mRequest.setRequesUrl(serverUrl);
			}
			
			this.mListenerWithState=listner;
	}
	
	@Override
	protected HttpResponseInfo doInBackground(Void... params) {
		if(!ConfigUtil.isConnect(context)){
			return new HttpResponseInfo(null, HttpTaskState.STATE_NO_NETWORK_CONNECT);
		}
		try {
			if (mRequest != null) {
			
				if(type == ServerType.FileServer){	//如果是上传文件，请注意一下。
					return new HttpResponseInfo(HttpManager.getHttpRequest(mRequest,bNetWork),	HttpTaskState.STATE_OK);		
				}else{
					return new HttpResponseInfo(HttpManager.postHttpRequest(mRequest,bNetWork),	HttpTaskState.STATE_OK);		
				}
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return new HttpResponseInfo(null, HttpTaskState.STATE_TIME_OUT);
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
			return new HttpResponseInfo(null, HttpTaskState.STATE_UNKNOWN);
//			return new HttpResponseInfo(null, HttpTaskState.STATE_NO_NETWORK_CONNECT);
		} catch (IOException e) {
			e.printStackTrace();
			return new HttpResponseInfo(null, HttpTaskState.STATE_ERROR_SERVER);
		}

		return null;
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(HttpResponseInfo response) {
		super.onPostExecute(response);
		if(mCommonLoading){
			LoadingProgress.getInstance().dismiss();
		}
		
		if(null == response){
			return;
		}
		
		
		if(mListenerWithState!=null){
			mListenerWithState.onTaskOver(mRequest, response);
		}
		
		switch (response.getState()) {
		case STATE_ERROR_SERVER:
			Toast.makeText(context, "服务器发生故障", Toast.LENGTH_SHORT).show();
			break;
		case STATE_NO_NETWORK_CONNECT:
			Toast.makeText(context, "没有网络，请检查您的网络连接", Toast.LENGTH_SHORT).show();
			break;
		case STATE_TIME_OUT:
			Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		case STATE_UNKNOWN:
			Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
			break;
		case STATE_OK:
			if(type == ServerType.FileServer){
				return;
			}
			BaseNetWork bNetWork = response.getmBaseNetWork();
			switch (bNetWork.getReturnCode()) {
			case RETURNCODE_OK:
				LogUtils.i("-----------------"+bNetWork.getStrJson());
				break;
			case RETURNCODE_FAIL:
				Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
				break;
			case RETURNCODE_MESSAGETYPE_ERROR:
				Toast.makeText(context, "消息类型错误", Toast.LENGTH_SHORT).show();
				break;
			case RETURNCODE_DATA_ERROR:
				Toast.makeText(context, "数据格式错误", Toast.LENGTH_SHORT).show();
				break;
			case RETURNCODE_SESSION_OVER:
				Toast.makeText(context, "会话过期", Toast.LENGTH_SHORT).show();
				break;
			case RETURNCODE_CHAT_DISCONNECT:
				Toast.makeText(context, "聊天连接已断开", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
				break;
			}
			break;
		default:
			Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mCommonLoading){
			LoadingProgress.getInstance().show(context);
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	public interface TaskListener{
		void onTaskOver(HttpRequestInfo request,String response);
	}
	
	public interface TaskListenerWithState{
		void onTaskOver(HttpRequestInfo request,HttpResponseInfo info);
	}
}
