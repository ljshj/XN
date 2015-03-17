package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.List;

import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.SystemConfig;

/**
 * @todo:微墙类
 * @date:2014-10-24 下午2:10:40
 * @author:hg_liuzl@163.com
 */
public class WeiQiangBean implements Serializable {

	public static final int WEIQIANG_FIND = 0;
	public static final int WEIQIANG_ATTENTION = 1;
	public static final int WEIQIANG_ALL = 2;
	
	/**
	 * @todo:TODO
	 * @date:2014-11-25 上午11:37:37
	 * @author:hg_liuzl@163.com
	 */
	private static final long serialVersionUID = 1L;

	public static final String KEY_WEIQIANG_BEAN = "key_weiqiang_bean";
	
	public String userid;
	public String weiboid;
	public String name;
	public String photo;
	public String distance;
	public String date_time;
	public String fromuserid;
	public String fromname;
	public String content;
	public String like_count;
	public String comment_count;
	public String forward_count;
	public String share_count;
	public String type;
	public List<ImageBean> imgs;
	public String comments;
	
	/**操作微墙
	 * 转发，还是评论
	 * */
	public enum WeiqiangActionType{		
		RESPONSE,TRANSPOND
	}
	
	public static WeiQiangBean copy(WeiqiangCorattionBean bean){
		WeiQiangBean wbean = new WeiQiangBean();
		
		wbean.userid = bean.userid;
		wbean.weiboid = bean.weiboid;
		wbean.name = bean.name;
		wbean.photo = bean.photo;
		wbean.distance = bean.distance;
		wbean.date_time = bean.date_time;
		wbean.fromuserid = bean.fromuserid;
		wbean.fromname = bean.fromname;
		wbean.content =bean.content;
		wbean.like_count = bean.like_count;
		wbean.comment_count = bean.comment_count;
		wbean.forward_count = bean.forward_count;
		wbean.share_count =bean.share_count;
		wbean.type = bean.type;
		wbean.imgs = bean.imgs;
		//wbean.Comments;
		return wbean;
	}

	public String getPhoto() {
		return SystemConfig.FILE_SERVER + photo;
	}


	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	
	public String getDistance() {
		return distance;
	}
	
}



