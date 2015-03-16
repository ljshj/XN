package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.List;

import com.bgood.xn.system.SystemConfig;

/**
 * @todo:笑话类
 * @date:2014-11-21 下午6:28:55
 * @author:hg_liuzl@163.com
 */
public class JokeBean implements Serializable {
	
	/**未通过审核**/
	public static final int JOKE_FAIL_VERIFY = -1;
	/**未审核**/
	public static final int JOKE_UN_VERIFY = 0;
	/**已经审核**/
	public static final int JOKE_VERIFY = 1;
	
	/****6:随机;5:顺序*/
	public static final int JOKE_RADOM = 6;
	public static final int JOKE_ORDER = 5;
	
	/**-1 原创,0 不审核，1 审核**/
	public static final int JOKE_ORIGINAL = -1;
	public static final int JOKE_DISAGREE = 0;
	public static final int JOKE_AGREE = 1;
	
	public static final String JOKE_BEAN = "joke_bean";
	
	/**
	 * @todo:TODO
	 * @date:2014-11-22 下午2:58:10
	 * @author:hg_liuzl@163.com
	 */
	private static final long serialVersionUID = 1L;
	public String rank_index;
	public String jokeid;
	public String userid;
	public String username;
	public String photo;
	public String distance;
	public String date_time;
	public String opartime;
	public String fromuserid;
	public String fromname;
	public String content;
	public String like_count;
	public String comment_count;
	public String forward_count;
	public String share_count;
	public String type;
	public List<ImageBean> imgs;
	public String Comments;
	public String remark;	/**审核备注*/
	public int status;		/**1，审核中 2.未通过审核***/
	public int paihang;	// 榜单排行
	public int original;	/***0非原创，1原创*****/
	
	/**操作
	 * 转发，还是评论
	 * */
	public enum JokeActionType{		
		RESPONSE,TRANSPOND
	}
	
	
	public static JokeBean copy(JokeCorattionBean bean){
		JokeBean wbean = new JokeBean();
		wbean.userid = bean.userid;
		wbean.jokeid = bean.jokeid;
		wbean.username = bean.name;
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
}
