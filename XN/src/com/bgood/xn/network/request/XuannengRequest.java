package com.bgood.xn.network.request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestAsyncTask;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig.ServerType;

/**
 * @todo:TODO
 * @date:2014-10-20 下午5:26:18
 * @author:hg_liuzl@163.com
 */
public class XuannengRequest extends BaseNetWork {
	private static XuannengRequest instance = null;
	public  synchronized static XuannengRequest getInstance(){
		if(null == instance)
			instance = new XuannengRequest();
		
		return instance;
	}
	 /**
		 * 
		 * @todo: 获取笑话列表
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestJokeList(TaskListenerWithState mHttpTaskListener,Context context,int itemid,int type,int start,int end,double longitude,double latitude){
		 	setMessageType(70001);
			JSONObject body = new JSONObject();
			try {
				body.put("itemid", itemid);
				body.put("type", type);
				body.put("start", start);
				body.put("end", end);
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
		 * @todo: 获取炫墙排行榜
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanRank(TaskListenerWithState mHttpTaskListener,Context context,int itemid,int rank_type,int start,int end,double longitude,double latitude){
		 	setMessageType(70002);
			JSONObject body = new JSONObject();
			try {
				body.put("itemid", itemid);
				body.put("rank_type", rank_type);
				body.put("start", start);
	            body.put("end", end);
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
		 * @todo: 炫能投稿
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanPublish(TaskListenerWithState mHttpTaskListener,Context context,String content, String[] imgs, String[] smallImgs,double longitude,double latitude){
		 	setMessageType(70003);
			JSONObject body = new JSONObject();
			try {
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
					body.put("longitude", longitude);
					body.put("latitude", latitude);
					body.put("content", content);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	
	 /**
		 * 
		 * @todo:  炫能转发到微博
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanTransport(TaskListenerWithState mHttpTaskListener,Context context,String itemid,String comment){
		 	setMessageType(70004);
			JSONObject body = new JSONObject();
			try {
				body.put("xnid", itemid);
				body.put("comments", comment);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	
	 /**
		 * 
		 * @todo: 炫能分享
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanShare(TaskListenerWithState mHttpTaskListener,Context context,String itemid){
		 	setMessageType(70005);
			JSONObject body = new JSONObject();
			try {
				body.put("xnid", itemid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 /**
		 * 
		 * @todo: 炫能评论
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanComment(TaskListenerWithState mHttpTaskListener,Context context,String itemid,String comments){
		 	setMessageType(70006);
			JSONObject body = new JSONObject();
			try {
				body.put("xnid", itemid);
				body.put("comments", comments);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 /**
		 * 
		 * @todo: 炫能赞
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanZan(TaskListenerWithState mHttpTaskListener,Context context,String itemid){
		 	setMessageType(70007);
			JSONObject body = new JSONObject();
			try {
				body.put("xnid", itemid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	 /**
		 * 
		 * @todo: 获取炫墙投搞列表
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanPublishList(TaskListenerWithState mHttpTaskListener,Context context,String userid,int type,int start,int end){
		 	setMessageType(70008);
			JSONObject body = new JSONObject();
			try {
				body.put("userid", userid);
				body.put("type", type);
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
		 * @todo: 查看笑话详情
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestJokeContent(TaskListenerWithState mHttpTaskListener,Context context,String itemid,String start,String end){
		 	setMessageType(70009);
			JSONObject body = new JSONObject();
			try {
				body.put("xnid", itemid);
				body.put("comment_start", start);
				body.put("comment_end", end);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	 /**
		 * 
		 * @todo: 获取幽默秀广告
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestJokeAd(TaskListenerWithState mHttpTaskListener,Context context,String itemid,String start,String end){
		 	setMessageType(70010);
			new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	
	 /**
		 * 
		 * @todo:  删除投稿
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanDel(TaskListenerWithState mHttpTaskListener,Context context,String xnid){
		 	setMessageType(70011);
			JSONObject body = new JSONObject();
			try {
				 body.put("xnid", xnid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
		 
		 /**、
		  * 
		  * @todo:炫能与我相关
		  * @date:2014-10-20 下午6:25:50
		  * @author:hg_liuzl@163.com
		  * @params:@param mHttpTaskListener
		  * @params:@param context
		  * @params:@param phone
		  */
		 public void requestXuanWithMe(TaskListenerWithState mHttpTaskListener,Context context,int start,int end){
			 	setMessageType(70012);
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
		 public void requestXuanUpdate(TaskListenerWithState mHttpTaskListener,Context context,String info,String xnid){
			 	setMessageType(70018);
				JSONObject body = new JSONObject();
				try {
					body.put("info", info);
					body.put("xnid", xnid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				setBody(body);
				 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
			 }
		 
		 
	 /**
		 * 
		 * @todo: 获取炫能榜单
		 * @date:2014-10-20 下午6:21:41
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param phone
		 */
		 public void requestXuanRecord(TaskListenerWithState mHttpTaskListener,Context context,int itemId,int type,int start,int end,double longitude,double latitude){
		 	setMessageType(70019);
			JSONObject body = new JSONObject();
			try {
				body.put("type", type);
				body.put("start", start);
				body.put("end", end);
				body.put("timestart", "");
				body.put("timeend", "");
				body.put("itemid", itemId);
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
		 * @todo:TODO
		 * @date:2015-3-10 下午6:49:06
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param itemId 炫能ID
		 * @params:@param type 0，不审核；1审核
		 */
			 public void requestXuanCheck(TaskListenerWithState mHttpTaskListener,Context context,String itemId,int type,boolean showLoading){
			 	setMessageType(70021);
				JSONObject body = new JSONObject();
				try {
					body.put("itemid", itemId);
					body.put("type", type);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				setBody(body);
				 new HttpRequestAsyncTask(showLoading,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
			 }
			 
			 
			/** 
			 * 
			 * @todo:未审核列表
			 * @date:2015-3-11 上午9:53:44
			 * @author:hg_liuzl@163.com
			 * @params:@param mHttpTaskListener
			 * @params:@param context
			 * @params:@param itemId 炫能类型，如幽默秀
			 * @params:@param type 0，未审核；1：审核，2所有
			 * @params:@param start 请求从第几条
			 * @params:@param end 到第几条
			 */
		 public void requestXuanUnCheckList(TaskListenerWithState mHttpTaskListener,Context context,int itemId,int type,int start,int end,int maxxnid){
		 	setMessageType(70022);
			JSONObject body = new JSONObject();
			try {
				body.put("itemid", itemId);
				body.put("type", type);
				body.put("start", start);
				body.put("end", end);
				body.put("maxxnid", maxxnid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
}
