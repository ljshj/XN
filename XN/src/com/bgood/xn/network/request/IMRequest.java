package com.bgood.xn.network.request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestAsyncTask;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig.ServerType;

/**
 * @todo:IM请求
 * @date:2014-12-8 上午10:45:17
 * @author:hg_liuzl@163.com
 */
public class IMRequest extends BaseNetWork {
	
	private static IMRequest instance = null;
	public  synchronized static IMRequest getInstance(){
		if(null == instance)
			instance = new IMRequest();
		
		return instance;
	}
	
	
	
	/**
	 * 
	 * @todo:获取联系人列表
	 * @date:2014-12-10 下午4:52:37
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param sendername
	 * @params:@param sender
	 * @params:@param userid
	 * @params:@param vertify
	 */
	public void requestContactsList(TaskListenerWithState mHttpTaskListener,Context context) {
		setMessageType(50006);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}

	/**
	 * 
	 * @todo:TODO 请求添加好友 
	 * @date:2014-12-22 上午11:54:35
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param recver  对方的ID
	 * @params:@param msg 是否同意 true ,false
	 * @params:@param userid 自己的ID
	 */
	@SuppressLint("NewApi")
	public void requestFriendADD(TaskListenerWithState mHttpTaskListener,Context context,String userid,String[] recver) {
		setMessageType(50027);
		JSONObject body = new JSONObject();
		try {
			
			JSONArray array = new JSONArray();
			for(String uid:recver){
				array.put(uid);
			}
			body.put("userid", userid);
			body.put("recver", array);
			body.put("msg", "true");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	
		/**
		 * 
		 * @todo:TODO 删除好友
		 * @date:2014-12-10 下午4:55:46
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param userid
		 */
	@SuppressLint("NewApi")
	public void requestFriendDelete(TaskListenerWithState mHttpTaskListener,Context context,String[] userid) {
		setMessageType(50008);
		JSONObject body = new JSONObject();
		try {
			
			JSONArray array = new JSONArray();
			for(String uid:userid){
				array.put(uid);
			}
			body.put("userid", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(false,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	
		/**
		 * 
		 * @todo:搜索要添加的好友
		 * @date:2014-12-10 下午4:56:49
		 * @author:hg_liuzl@163.com
		 * @params:@param mHttpTaskListener
		 * @params:@param context
		 * @params:@param keyword
		 * @params:@param start
		 * @params:@param end
		 */
	public void requestFriendSearch(TaskListenerWithState mHttpTaskListener,Context context,String keyword,int start,int end) {
		setMessageType(50009);
		JSONObject body = new JSONObject();
		try {
			body.put("keyword", keyword);
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
	 * @todo:获取群信息
	 * @date:2014-12-10 下午4:59:25
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param id
	 */
	public void requestGroupInfo(TaskListenerWithState mHttpTaskListener,Context context,String id,String hxgroupid,boolean showProgress) {
		setMessageType(50012);
		JSONObject body = new JSONObject();
		try {
			body.put("id", id);
			body.put("hxgroupid", hxgroupid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(showProgress,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	

	/**
	 * 
	 * @todo:获取群成员列表
	 * @date:2014-12-10 下午5:00:16
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param sendername
	 * @params:@param id
	 */
	public void requestGroupMembers(TaskListenerWithState mHttpTaskListener,Context context,String id,boolean showDialog) {
		setMessageType(50013);
		JSONObject body = new JSONObject();
		try {
			body.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(showDialog,ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	

	/**
	 * 
	 * @todo:创建固定群
	 * @date:2014-12-10 下午5:22:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param name
	 * @params:@param type	//0 固定群，1 临时群
	 * @params:@param info
	 */
	public void requestGroupCreate(TaskListenerWithState mHttpTaskListener,Context context,String name,int type,String info,String notice) {
		setMessageType(50014);
		JSONObject body = new JSONObject();
		try {
			body.put("name", name);
			body.put("type", type);
			body.put("intro", info);
			body.put("notice", notice);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}

	/**
	 * 
	 * @todo:查找群
	 * @date:2014-12-10 下午5:23:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param keyword
	 * @params:@param start
	 * @params:@param end
	 */
	public void requestGroupSearch(TaskListenerWithState mHttpTaskListener,Context context,String keyword,int start,int end) {
		setMessageType(50015);
		JSONObject body = new JSONObject();
		try {
			body.put("keyword", keyword);
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
	 * @todo:解散群
	 * @date:2014-12-10 下午5:24:17
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param groupid
	 */
	public void requestGroupDisMiss(TaskListenerWithState mHttpTaskListener,Context context,String groupid) {
		setMessageType(50016);
		JSONObject body = new JSONObject();
		try {
			body.put("groupid", groupid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}

	/**
	 * 
	 * @todo:TODO 转让群
	 * @date:2014-12-10 下午5:25:14
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 */
	public void requestGroupTransfer(TaskListenerWithState mHttpTaskListener,Context context,String groupid,String userid) {
		setMessageType(50017);
		JSONObject body = new JSONObject();
		try {
			body.put("groupid", groupid);
			body.put("userid", userid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}

	/**
	 * @todo:TODO 退出群
	 * @date:2014-12-10 下午5:26:15
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 */
	public void requestGroupQuit(TaskListenerWithState mHttpTaskListener,Context context,String userid,String groupid) {
		setMessageType(50018);
		JSONObject body = new JSONObject();
		try {
			body.put("groupid", groupid);
			body.put("userid", userid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
//	
//	
////	发布群公告
//	public void requestGroupNoticePublish(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
//		setMessageType(50007);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("sendername", sendername);
//			body.put("sender", sender);
//			body.put("userid", userid);
//			body.put("vertify", vertify);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
////	修改群公告
//	public void requestGroupNoticeModify(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
//		setMessageType(50007);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("sendername", sendername);
//			body.put("sender", sender);
//			body.put("userid", userid);
//			body.put("vertify", vertify);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
////	修改群资料
//	public void requestGroupInfoModify(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
//		setMessageType(50007);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("sendername", sendername);
//			body.put("sender", sender);
//			body.put("userid", userid);
//			body.put("vertify", vertify);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
////	任命管理员
//	public void requestGroupSetAdmin(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
//		setMessageType(50007);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("sendername", sendername);
//			body.put("sender", sender);
//			body.put("userid", userid);
//			body.put("vertify", vertify);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
	
	/**
	 * 
	 * @todo:移除群成员
	 * @date:2015-1-2 下午4:34:01
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param userid
	 * @params:@param groupid
	 */
	public void requestGroupMemberRemove(TaskListenerWithState mHttpTaskListener,Context context,String userid,String groupid) {
		setMessageType(50024);
		JSONObject body = new JSONObject();
		try {
			body.put("userid", userid);
			body.put("groupid", groupid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	
	/**
	 * 
	 * @todo:添加群成员
	 * @date:2015-1-2 下午4:34:01
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param userid
	 * @params:@param groupid
	 */
	public void requestGroupMemberJoinOrInvite(TaskListenerWithState mHttpTaskListener,Context context,String[] userid,String groupid) {
		setMessageType(50025);
		JSONObject body = new JSONObject();
		try {
			
			JSONArray array = new JSONArray();
			for(String uid:userid){
				array.put(uid);
			}
			body.put("userid", array);
			body.put("groupid", groupid);
			body.put("type", 0);
			body.put("agr", "true");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	
	/**
	 * 
	 * @todo:添加交流厅成员
	 * @date:2015-1-2 下午4:34:01
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param userid
	 * @params:@param groupid
	 */
	public void requestCommunicationMemberJoinOrInvite(TaskListenerWithState mHttpTaskListener,Context context,String[] userid,String groupid) {
		setMessageType(50030);
		JSONObject body = new JSONObject();
		try {
			
			JSONArray array = new JSONArray();
			for(String uid:userid){
				array.put(uid);
			}
			body.put("userid", array);
			body.put("groupid", groupid);
			body.put("agr", "true");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	}
	
	
	
	
////	移除群成员
//	public void requestGroupMemberRemove(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
//		setMessageType(50007);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("sendername", sendername);
//			body.put("sender", sender);
//			body.put("userid", userid);
//			body.put("vertify", vertify);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
////	获取聊天窗口广告
//	public void requestGroupAD(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
//		setMessageType(50007);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("sendername", sendername);
//			body.put("sender", sender);
//			body.put("userid", userid);
//			body.put("vertify", vertify);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	}
	
}
