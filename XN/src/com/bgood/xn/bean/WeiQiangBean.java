package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @todo:微墙类
 * @date:2014-10-24 下午2:10:40
 * @author:hg_liuzl@163.com
 */
public class WeiQiangBean implements Serializable {
//	 <userid/>用户id
//	    <weiboid/>//帖子id
//	    <name/>
//	<photo/>//用户头像
//	<distance/>
//	<date_time/>
//	<fromuserid/>//来源用户ID，如果是转才有此字段
//	<fromusername/>来源用户名
//	<content/>
//	<like_count/>
//	<comment_count/>
//	<forward_count/>
//	<share_count/>

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



