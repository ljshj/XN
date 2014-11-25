package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @todo:微墙类
 * @date:2014-10-24 下午2:10:40
 * @author:hg_liuzl@163.com
 */
public class WeiQiangBean implements Serializable {

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
	public String Comments;
	
	/**操作微墙
	 * 转发，还是评论
	 * */
	public enum WeiqiangActionType{		
		RESPONSE,TRANSPOND
	}
	
}



