package com.bgood.xn.network.http;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequestInfo {
	
	public static final String base_url = "123";
	
	private String requesUrl;
	private int requestID;
	private  Map<String, String> requestParams;
	
	private HttpRequestInfo() {
		
	}
	
	private static class SigleInstance {
		 private static HttpRequestInfo httpRequestInfo = new HttpRequestInfo();
	}
	
	/**
	 * 创建一个HttpRequestInfo单例
	 * @return  HttpRequestInfo实例
	 */
	public static  HttpRequestInfo getHttpRequestInfoInstance (){
		return SigleInstance.httpRequestInfo;
	}
	
	/**
	 * 设置url并且实例化请求参数集合
	 */
	public void getUrl(String url) {
		this.setRequesUrl(url);
		requestParams=new HashMap<String, String>();
	}
	
	public HttpRequestInfo(String url) {
		this.setRequesUrl(url);
		requestParams=new HashMap<String, String>();
	}
	
	public HttpRequestInfo(String url,Map<String, String> params) {
		this.setRequesUrl(url);
		this.setRequestParams(params);
	}
	public String getRequestUrl() {
		return requesUrl;
	}
	public void setRequesUrl(String requesUrl) {
		this.requesUrl = requesUrl;
	}
	public Map<String, String> getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}
	
	@SuppressWarnings("deprecation")
	public String getParamsStr(){
		String str="";
		if(requestParams!=null){
			Iterator<Entry<String, String>> iter = requestParams.entrySet().iterator();  
			while (iter.hasNext()) { 
				Entry<String, String> entry = iter.next();  
				String key = entry.getKey();  
				String val = entry.getValue(); 
				if (val != null) {
//					key=URLEncoder.encode(key);
//					val=URLEncoder.encode(val);//此行
					str += key + "=" + val + "&";
				}
			}
		}
		if(str.equals("")){
			return null;
		}
		return str;
	}
	
	public HttpRequestInfo putParam(String key,String value){
		this.requestParams.put(key, value);
		return this;
	}
	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

}
