package com.bgood.xn.network.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestAsyncTask;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig.ServerType;

/**
 * @todo:产品请求
 * @date:2014-11-13 下午4:11:00
 * @author:hg_liuzl@163.com
 */
public class ProductRequest extends BaseNetWork {
	
	private static ProductRequest instance = null;
	public  synchronized static ProductRequest getInstance(){
		if(null == instance)
			instance = new ProductRequest();
		
		return instance;
	}
	
	/**
	 * 
	 * @todo:获取橱窗信息
	 * @date:2014-11-13 下午4:14:05
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param type
	 * @params:@param start
	 * @params:@param end
	 */
	 public void requestShowCase(TaskListenerWithState mHttpTaskListener,Context context,String userid){
	 	setMessageType(30001);
		JSONObject body = new JSONObject();
		try {
			body.put("userid", userid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	
	/**添加商品*/
	 public void requestProductAdd(TaskListenerWithState mHttpTaskListener,Context context,String userid){
		 	setMessageType(30003);
			JSONObject body = new JSONObject();
			try {
				body.put("userid", userid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			setBody(body);
			 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
		 }
	 
	/**修改商品*/
	 public void requestProductModify(TaskListenerWithState mHttpTaskListener,Context context,ProductBean bean){
	 	setMessageType(30004);
		JSONObject body = new JSONObject();
		try {
			body.put("proid", bean.product_id);
			body.put("pname", bean.product_name);
			body.put("price", bean.price);
			body.put("intro", bean.intro);
			body.put("brecom", bean.recommed);
			body.put("img", bean.img);
			body.put("img_thum", bean.img_thum);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	/**删除商品*/
	 public void requestProductDelete(TaskListenerWithState mHttpTaskListener,Context context,String proid){
	 	setMessageType(30005);
		JSONObject body = new JSONObject();
		try {
			body.put("proid", proid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
//	
//	 /**查找商品*/
//	 public void requestProductSearch(TaskListenerWithState mHttpTaskListener,Context context,String userid,String keyword){
//	 	setMessageType(30006);
//		JSONObject body = new JSONObject();
//		try {
//			body.put("userid", userid);
//			body.put("keyword", keyword);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		setBody(body);
//		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
//	 }
	
	/**查看商品详情*/
	 public void requestProductDetail(TaskListenerWithState mHttpTaskListener,Context context,String product_id){
	 	setMessageType(30007);
		JSONObject body = new JSONObject();
		try {
			body.put("product_id", product_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	/**获取商品列表*/
	 public void requestProductList(TaskListenerWithState mHttpTaskListener,Context context,String userid,String keyword,String start,String end){
	 	setMessageType(30008);
		JSONObject body = new JSONObject();
		try {
			body.put("userid", userid);
			body.put("start", start);
			body.put("keyword", keyword);
			body.put("end", end);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	
	/**设置模板*/
	 public void requestProductSetTemplant(TaskListenerWithState mHttpTaskListener,Context context,String tempid){
	 	setMessageType(30009);
		JSONObject body = new JSONObject();
		try {
			body.put("tempid", tempid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	 
	 /**评论商品*/
	 public void requestProductComment(TaskListenerWithState mHttpTaskListener,Context context,String product_id,String comment){
	 	setMessageType(30010);
		JSONObject body = new JSONObject();
		try {
			body.put("product_id", product_id);
			body.put("comments", comment);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	
	/**商品评论列表*/
	 public void requestProductCommentList(TaskListenerWithState mHttpTaskListener,Context context,String product_id,String comment_start,String comment_end){
	 	setMessageType(300011);
		JSONObject body = new JSONObject();
		try {
			body.put("product_id", product_id);
			body.put("comment_start", comment_start);
			body.put("comment_end", comment_end);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setBody(body);
		 new HttpRequestAsyncTask(ServerType.BusinessServer,this, mHttpTaskListener, context).execute();
	 }
	
	
	
	
	
	
}
