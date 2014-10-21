package com.bgood.xn.network;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;

import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.system.SystemConfig.ServerType;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.view.LoadingProgress;

public class HttpRquestAsyncTask extends AsyncTask<Void, Void,HttpResponseInfo > {
	private String serverUrl = null;	//服务器地址
	private HttpRequestInfo mRequest;
	private TaskListenerWithState mListenerWithState;
	private Context context;
	private BaseNetWork bNetWork;
	
	public HttpRquestAsyncTask(ServerType type,BaseNetWork b,TaskListenerWithState listner,Context c) {
		this.bNetWork = b;
		this.context=c;
		this.mRequest = HttpRequestInfo.getHttpRequestInfoInstance();
		
		//1.服务器分配地址 2.登录后的操作要分配session
		if(type == ServerType.LoginServer){
			serverUrl = SystemConfig.HttpServer;
			
		}else if(type == ServerType.BusinessServer){
			serverUrl = SystemConfig.BS_SERVER;
			b.setSessionID(SystemConfig.SessionID);
			
		}else if(type == ServerType.IMServer){
			serverUrl = SystemConfig.IM_SERVER;
			b.setSessionID(SystemConfig.SessionID);
			
		}else if(type == ServerType.FileServer){
			serverUrl = SystemConfig.FILE_SERVER;
			b.setSessionID(SystemConfig.SessionID);
			
		}else{
			serverUrl = SystemConfig.HttpServer;
		}
		mRequest.setRequesUrl("http://"+serverUrl);
		this.mListenerWithState=listner;
	}
	
	
	
	@Override
	protected HttpResponseInfo doInBackground(Void... params) {
		if(!ConfigUtil.isConnect(context)){
			return new HttpResponseInfo(null, HttpTaskState.STATE_NO_NETWORK_CONNECT);
		}
		try {
			if (mRequest != null) {
				if (mRequest.getRequestID() == -2) {                       
					return new HttpResponseInfo(
							HttpManager.postHttpRequest(mRequest,bNetWork),
							HttpTaskState.STATE_OK);
				}
				
				return new HttpResponseInfo(HttpManager.postHttpRequest(mRequest,bNetWork),	HttpTaskState.STATE_OK);		//针对http
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
		if(mListenerWithState!=null){
			mListenerWithState.onTaskOver(mRequest, response);
		}
		LoadingProgress.getInstance().dismiss();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		LoadingProgress.getInstance().show(context);
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
