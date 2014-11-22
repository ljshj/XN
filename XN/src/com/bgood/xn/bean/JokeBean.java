package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @todo:笑话类
 * @date:2014-11-21 下午6:28:55
 * @author:hg_liuzl@163.com
 */
public class JokeBean implements Serializable {
	
	/****0:随机;1:顺序*/
	public static final int JOKE_RADOM = 0;
	public static final int JOKE_ORDER = 1;
	
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
	
	/**操作
	 * 转发，还是评论
	 * */
	public enum JokeActionType{		
		RESPONSE,TRANSPOND
	}
}
