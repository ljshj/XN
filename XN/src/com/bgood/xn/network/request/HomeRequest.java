package com.bgood.xn.network.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRquestAsyncTask;
import com.bgood.xn.network.HttpRquestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig.ServerType;
/**
 * @todo:首页网络请求接口
 * @date:2014-10-20 下午5:25:45
 * @author:hg_liuzl@163.com
 */
public class HomeRequest  extends BaseNetWork {
	private static HomeRequest instance = null;
	public  synchronized static HomeRequest getInstance(){
		if(null == instance)
			instance = new HomeRequest();
		
		return instance;
	}
	/**
	 * 
	 * @todo:搜索操作
	 * @date:2014-10-20 下午5:59:13
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param type
	 * @params:@param keyword
	 * @params:@param longitude
	 * @params:@param latitude
	 * @params:@param start
	 * @params:@param end
	 */
	 public void requestSearch(TaskListenerWithState mHttpTaskListener,Context context,int type, String keyword, double longitude, double latitude, int start, int end){
	 	setMessageType(40001);
		JSONObject body = new JSONObject();
		try {
			body.put("type", type);
			body.put("keyword", keyword);
			body.put("longitude", longitude);
			body.put("latitude", latitude);
			body.put("start", start);
			body.put("end", end);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	 
		
 /**
  * 
  * @todo:获取会员分页请求
  * @date:2014-10-20 下午6:01:18
  * @author:hg_liuzl@163.com
  * @params:@param type
  * @params:@param keyword
  * @params:@param start
  * @params:@param end
  */
	public void reqeuestMemberList(TaskListenerWithState mHttpTaskListener,Context context,int type, String keyword, int start, int end)
	{
		setMessageType(40002);
		JSONObject body = new JSONObject();
		try
		{
			body.put("type", type);
			body.put("keyword", keyword);
			body.put("start", start);
			body.put("end", end);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		setBody(body);
		new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	
	
	 /**
	  * 
	  * @todo:获取微墙分页请求
	  * @date:2014-10-20 下午6:01:18
	  * @author:hg_liuzl@163.com
	  * @params:@param type
	  * @params:@param keyword
	  * @params:@param start
	  * @params:@param end
	  */
		public void requestWeiqianList(TaskListenerWithState mHttpTaskListener,Context context,int type, String keyword, int start, int end)
		{
			setMessageType(40003);
			JSONObject body = new JSONObject();
			try
			{
				body.put("keyword", keyword);
				body.put("start", start);
				body.put("end", end);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			setBody(body);
			new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	
	 /**
	  * 
	  * @todo:获取橱窗分页请求
	  * @date:2014-10-20 下午6:01:18
	  * @author:hg_liuzl@163.com
	  * @params:@param type
	  * @params:@param keyword
	  * @params:@param start
	  * @params:@param end
	  */
		public void requestProductList(TaskListenerWithState mHttpTaskListener,Context context,int type, String keyword, int start, int end)
		{
			setMessageType(40004);
			JSONObject body = new JSONObject();
			try
			{
				body.put("keyword", keyword);
				body.put("start", start);
				body.put("end", end);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			setBody(body);
			new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	 /**
	  * 
	  * @todo:获取会员分页请求
	  * @date:2014-10-20 下午6:01:18
	  * @author:hg_liuzl@163.com
	  * @params:@param type
	  * @params:@param keyword
	  * @params:@param start
	  * @params:@param end
	  */
		public void requestAppList(TaskListenerWithState mHttpTaskListener,Context context,int type, String keyword, int start, int end)
		{
			setMessageType(40005);
			JSONObject body = new JSONObject();
			try
			{
				body.put("keyword", keyword);
				body.put("start", start);
				body.put("end", end);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			setBody(body);
			new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	
	 /**
	  * 
	  * @todo:获取热门词
	  * @date:2014-10-20 下午6:01:18
	  * @author:hg_liuzl@163.com
	  * @params:@param type
	  * @params:@param keyword
	  * @params:@param start
	  * @params:@param end
	  */
		public void requestHotWord(TaskListenerWithState mHttpTaskListener,Context context)
		{
			setMessageType(40007);
			new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
}

///**
//* 
//* @todo:获取查询前分类提示词库
//* @date:2014-10-20 下午6:01:18

//* @author:hg_liuzl@163.com
//* @params:@param type
//* @params:@param keyword
//* @params:@param start
//* @params:@param end
//*/
//	public void getMemberList(TaskListenerWithState mHttpTaskListener,Context context,int type, String keyword, int start, int end)
//	{
//		setMessageType(40001);
//		JSONObject body = new JSONObject();
//		try
//		{
//			body.put("type", type);
//			body.put("keyword", keyword);
//			body.put("start", start);
//			body.put("end", end);
//		} catch (JSONException e)
//		{
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRquestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
