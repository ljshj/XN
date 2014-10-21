package com.bgood.xn.network;


public class HttpResponseInfo {
	public enum HttpTaskState{
		STATE_OK,
		STATE_NO_NETWORK_CONNECT,
		STATE_TIME_OUT,
		STATE_UNKNOWN,
		STATE_ERROR_SERVER,
	}

	public HttpResponseInfo(BaseNetWork baseNetWork,HttpTaskState state) {
		this.mBaseNetWork = baseNetWork;
		this.state=state;
	}
	
	private HttpTaskState state;
	public HttpTaskState getState() {
		return state;
	}
	public void setState(HttpTaskState state) {
		this.state = state;
	}
	
	private BaseNetWork mBaseNetWork;
	
	public BaseNetWork getmBaseNetWork() {
		return mBaseNetWork;
	}
	public void setmBaseNetWork(BaseNetWork mBaseNetWork) {
		this.mBaseNetWork = mBaseNetWork;
	}
	
}
