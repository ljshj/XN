package com.bgood.xn.network.request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestAsyncTask;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.SystemConfig.ServerType;

/**
 * @todo:微墙部分网络模块 
 * @date:2014-10-20 下午5:20:36
 * @author:hg_liuzl@163.com
 */
public class WeiqiangRequest extends BaseNetWork {
	private static WeiqiangRequest instance = null;
	public  synchronized static WeiqiangRequest getInstance(){
		if(null == instance)
			instance = new WeiqiangRequest();
		
		return instance;
	}
	/**
	 * 
	 * @todo: 获取炫墙列表
	 * @date:2014-10-20 下午6:21:41
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param phone
	 */
	 public void requestWeiqiangList(TaskListenerWithState mHttpTaskListener,Context context,String type,String userid,String start,String end){
	 	setMessageType(60001);
		JSONObject body = new JSONObject();
		try {
			body.put("type", type);
			body.put("userid", userid);
			body.put("start", start);
			body.put("end", end);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	/**
	 * 
	 * @todo: 获取内容详情
	 * @date:2014-10-20 下午6:23:36
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param phone
	 */
	 public void requestWeiqiangContent(TaskListenerWithState mHttpTaskListener,Context context,String weiboid,String comment_start,String comment_end){
		 	setMessageType(60002);
			JSONObject body = new JSONObject();
			try {
				body.put("weiboid", weiboid);
				body.put("comment_start", comment_start);
				body.put("comment_end", comment_end);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	 /**
	  * 
	  * @todo:发布微博
	  * @date:2014-10-20 下午6:24:02
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangSend(TaskListenerWithState mHttpTaskListener,Context context,String content, String[] imgs, String[] smallImgs, String date_time, String longitude, String latitude){
		 	setMessageType(60003);
			JSONObject body = new JSONObject();
			try
			{
				body.put("content", content);
				if (imgs != null && imgs.length > 0)
				{
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < imgs.length; i++)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("img", imgs[i]);
						jsonObject.put("img_thum", smallImgs[i]);
						jsonArray.put(jsonObject);
					}
					body.put("imgs", jsonArray);
				}
				body.put("date_time", date_time);
				body.put("longitude", longitude);
				body.put("latitude", latitude);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	
	 /**
	  * 
	  * @todo: 删除微博
	  * @date:2014-10-20 下午6:24:11
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangDel(TaskListenerWithState mHttpTaskListener,Context context,String weiboid){
		 	setMessageType(60004);
			JSONObject body = new JSONObject();
			try {
				body.put("weiboid", weiboid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	/**
	 * 
	 * @todo: 转发微博
	 * @date:2014-10-20 下午6:24:26
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param phone
	 */
	 public void requestWeiqiangTranspond(TaskListenerWithState mHttpTaskListener,Context context,String weiboid,String comments){
		 	setMessageType(60005);
			JSONObject body = new JSONObject();
			try {
				body.put("weiboid", weiboid);
				body.put("comments", comments);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 /**
	  * 
	  * @todo:TODO
	  * @date:2014-10-20 下午6:25:13
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangShare(TaskListenerWithState mHttpTaskListener,Context context,String weiboid){
		 	setMessageType(60006);
			JSONObject body = new JSONObject();
			try {
				body.put("weiboid", weiboid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 /**
	  * 
	  * @todo:评价微墙
	  * @date:2014-10-20 下午6:25:27
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangReply(TaskListenerWithState mHttpTaskListener,Context context,String weiboid,String comments){
		 	setMessageType(60007);
			JSONObject body = new JSONObject();
			try {
				body.put("weiboid", weiboid);
				body.put("comments", comments);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	 /**、
	  * 
	  * @todo:赞微墙
	  * @date:2014-10-20 下午6:25:50
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangZan(TaskListenerWithState mHttpTaskListener,Context context,String weiboid){
		 	setMessageType(60008);
			JSONObject body = new JSONObject();
			try {
				body.put("weiboid", weiboid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	 /**、
	  * 
	  * @todo:微墙与我相关
	  * @date:2014-10-20 下午6:25:50
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangWithMe(TaskListenerWithState mHttpTaskListener,Context context,int start,int end){
		 	setMessageType(60009);
			JSONObject body = new JSONObject();
			try {
				body.put("start", start);
				body.put("end", end);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	 
	 /**、
	  * 
	  * @todo:修改炫能
	  * @date:2014-10-20 下午6:25:50
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param phone
	  */
	 public void requestWeiqiangUpdate(TaskListenerWithState mHttpTaskListener,Context context,String info,String wbid){
		 	setMessageType(60011);
			JSONObject body = new JSONObject();
			try {
				body.put("info", info);
				body.put("wbid", wbid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
}
