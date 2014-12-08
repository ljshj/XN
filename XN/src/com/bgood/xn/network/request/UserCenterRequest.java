package com.bgood.xn.network.request;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestAsyncTask;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig.ServerType;
import com.bgood.xn.utils.MD5;

/**
 * @author lzlong@zwmob.com
 * @time 2014-2-11 上午11:48:02
 * @todo  管理请求接口
 */

public class UserCenterRequest extends BaseNetWork {
	private static UserCenterRequest instance = null;
	public  synchronized static UserCenterRequest getInstance(){
		if(null == instance)
			instance = new UserCenterRequest();
		
		return instance;
	}
	
	/**
	 *获取手机验证码
	 */
	 public void requestVerifyCode(TaskListenerWithState mHttpTaskListener,Context context,String phone){
	 	setMessageType(10001);
		JSONObject body = new JSONObject();
		try {
			body.put("pnum", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
	 }
	 
	 /**验证手机号并获取备选邦固号列表*/
	 public void requestVerifyPhoneAndGetBgID(TaskListenerWithState mHttpTaskListener,Context context,String phone,String verfy){
		 	setMessageType(10002);
			JSONObject body = new JSONObject();
			try {
				body.put("pnum", phone);
				body.put("verfy", verfy);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**更换邦固号列表*/
	 public void requestChangeBGID(TaskListenerWithState mHttpTaskListener,Context context,String phone){
		 	setMessageType(10003);
			JSONObject body = new JSONObject();
			try {
				body.put("pnum", phone);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**发送注册请求*/
	 public void requestRegister(TaskListenerWithState mHttpTaskListener,Context context,String phone,String userid,String pwd){
		 	setMessageType(10004);
			JSONObject body = new JSONObject();
			try {
				body.put("pnum", phone);
				body.put("userid", userid);
				body.put("pwd",MD5.getMD5(pwd));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	
	 /**会员登录*/
	 public void requestLogin(TaskListenerWithState mHttpTaskListener,Context context,String userid,String pwd){
		 	setMessageType(10005);
			JSONObject body = new JSONObject();
			try {
				body.put("userid", userid);
				body.put("pwd", MD5.getMD5(pwd));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**忘记密码时获取验证码*/
	 public void requestVerifyByForget(TaskListenerWithState mHttpTaskListener,Context context,String phone){
		 	setMessageType(10006);
			JSONObject body = new JSONObject();
			try {
				body.put("pnum", phone);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**忘记密码时重置密码*/
	 public void requestReSetPwd(TaskListenerWithState mHttpTaskListener,Context context,String userid,String pwd){
		 	setMessageType(10007);
			JSONObject body = new JSONObject();
			try {
				body.put("userid", userid);
				body.put("pwd",  MD5.getMD5(pwd));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**忘记密码时验证手机号*/
	 public void requestVerifyPhoneByForget(TaskListenerWithState mHttpTaskListener,Context context,String phone,String verfy){
		 	setMessageType(10008);
			JSONObject body = new JSONObject();
			try {
				body.put("pnum", phone);
				body.put("verfy", verfy);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**未登录时获取业务服务器的地址*/
	 public void requestUnLoginBSServer(TaskListenerWithState mHttpTaskListener,Context context){
		 	 setMessageType(10009);
			 new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**获取个人资料*/
	 public void requestPersonInfo(TaskListenerWithState mHttpTaskListener,Context context,String userid){
		 	setMessageType(20001);
			JSONObject body = new JSONObject();
			try {
				body.put("userid", userid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	 
	 /**修改个人资料
	 修改“我能”
	 修改“我需”
	 修改头像
	 修改昵称
	 修改签名
	 修改性别
	 修改生日
	 修改星座
	 修改家乡
	 修改所在地
	 修改邮箱
	 修改血型
	 年龄
	  * */
	 
	 public void requestUpdatePerson(TaskListenerWithState mHttpTaskListener,Context context,String type,String value){
		 	setMessageType(20002);
			JSONObject body = new JSONObject();
			try {
				body.put("type", type);
				body.put("value", value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	
	 /**
	  * 
	  * @todo:更新位置信息
	  * @date:2014-10-20 下午5:29:12
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param type
	  * @params:@param value
	  */
	 public void requestUpdateLocation(TaskListenerWithState mHttpTaskListener,Context context,String longitude,String latitude){
		 	setMessageType(20003);
			JSONObject body = new JSONObject();
			try {
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
	  * @todo:关注与取消关注
	  * @date:2014-10-20 下午5:34:59
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param type 关注是0，取消关注是1
	  * @params:@param value
	  */
	 
	 public void requestAttention(TaskListenerWithState mHttpTaskListener,Context context,String userid,String byUserid,String type){
		 	setMessageType(20004);
			JSONObject body = new JSONObject();
			try {
				body.put("userid", userid);
				body.put("byuserid", byUserid);
				body.put("type", type);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	 /**
	  * @todo:我关注的对象与关注我的对象
	  * @date:2014-10-20 下午5:34:26
	  * @author:hg_liuzl@163.com
	  * @params:@param mHttpTaskListener
	  * @params:@param context
	  * @params:@param type 我关注的是 0，关注我的是1
	  * @params:@param value
	  */
	 public void requestAttentionOfMe(TaskListenerWithState mHttpTaskListener,Context context,String type,String start,String end){
		 	setMessageType(20005);
			JSONObject body = new JSONObject();
			try {
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
	 * @todo:修改密码
	 * @date:2014-10-30 下午3:46:38
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param type
	 * @params:@param start
	 * @params:@param end
	 */
	 public void requestModifyPWD(TaskListenerWithState mHttpTaskListener,Context context,String oldPwd,String newPwd){
		 	setMessageType(20006);
			JSONObject body = new JSONObject();
			try {
				body.put("oldpassword", MD5.getMD5(oldPwd));
				body.put("newpassword", MD5.getMD5(newPwd));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
	 
		/**
		 * 
		 * @todo:添加意见反馈
		 */
		 public void requestFeedbackInsert(TaskListenerWithState mHttpTaskListener,Context context,String message,String contactnum){
			 	setMessageType(20007);
				JSONObject body = new JSONObject();
				try {
					body.put("message", message);
					body.put("contactnum", contactnum);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				setBody(body);
				new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
			}
		 
		/**
		 * 
		 * @todo:获取意见反馈列表
		 */
		 public void requestFeedbackList(TaskListenerWithState mHttpTaskListener,Context context){
			setMessageType(20008);
			new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		}
		 
		/**
		 * 
		 * @todo:检查版本升级
		 */
		 public void requestCheckVesion(TaskListenerWithState mHttpTaskListener,Context context){
			    setMessageType(80001);
				JSONObject body = new JSONObject();
				try {
					body.put("os", "android");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				setBody(body);
			new HttpRequestAsyncTask(ServerType.LoginServer,this, mHttpTaskListener, context).execute();
		}
}
