package com.bgood.xn.network.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bgood.xn.bean.MessageBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestAsyncTask;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.system.SystemConfig.ServerType;

/**
 * @todo:IM请求
 * @date:2014-12-8 上午10:45:17
 * @author:hg_liuzl@163.com
 * 
 *  需要保持长链接的加上Link标记
 */
public class IMRequest extends BaseNetWork {
	private static HomeRequest instance = null;
	public  synchronized static HomeRequest getInstance(){
		if(null == instance)
			instance = new HomeRequest();
		
		return instance;
	}
	/**
	 * 
	 * @todo:请求聊天服务器地址
	 * @date:2014-12-8 下午4:53:11
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 */
	public void requestChatServerAddress(TaskListenerWithState mHttpTaskListener,Context context) {
		setMessageType(50000);
		new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();

	}
	
	/**发送文字*/
	public void requestLinkSendWords(TaskListenerWithState mHttpTaskListener,Context context,MessageBean bean) {
		setMessageType(50001);
		JSONObject message = new JSONObject();
		try
		{
			message.put("sendername", bean.sendername);
			message.put("sender", bean.sender);
			message.put("recver", bean.recver);
			message.put("type", bean.type);
			message.put("datetime", bean.datetime);
			message.put("info", bean.info);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		setBody(message);
		new HttpRequestAsyncTask(ServerType.IMServer,this, mHttpTaskListener, context).execute();
	}
	
	/**登录聊天服务器*/
	public void requestLinkLoginChatServer(TaskListenerWithState mHttpTaskListener,Context context) {
		  setMessageType(50002);
			JSONObject body = new JSONObject();
			try {
				body.put("sessionid", SystemConfig.SessionID);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
		new HttpRequestAsyncTask(ServerType.IMServer,this, mHttpTaskListener, context).execute();
	}
	

	/**发送文件*/
	private void requestLinkSendFile() {
	
	}
	
	/**发送图片*/
	private void requestLinkSendPic() {
	

	}
	
	/**
	 * 
	 * @todo:TODO 请求添加好友 
	 * @date:2014-12-8 下午5:10:14
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param sendername  发送人
	 * @params:@param sender 发送人ID
	 * @params:@param userid 好友ID
	 * @params:@param vertify 验证信息
	 */
	public void requestLinkAddFriend(TaskListenerWithState mHttpTaskListener,Context context,String sendername,String sender,String userid,String vertify) {
		setMessageType(50007);
		JSONObject body = new JSONObject();
		try {
			body.put("sendername", sendername);
			body.put("sender", sender);
			body.put("userid", userid);
			body.put("vertify", vertify);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		new HttpRequestAsyncTask(ServerType.IMServer,this, mHttpTaskListener, context).execute();
	}
	
//	删除好友
//	搜索要添加的好友
//	创建分组
//	添加好友到分组
//
//
//
//	获取群信息
//	获取群成员列表
//	创建固定群
//	查找群
//	解散群
//	转让群
//	退出群
//	发布群公告
//	修改群公告
//	修改群资料
//	任命管理员
//	请求加入群或邀请加入群
//	移除群成员
//	是否同意申请加入群
//	获取离线消息数目
//	是否同意添加为好友
//	获取聊天窗口广告
	
}
