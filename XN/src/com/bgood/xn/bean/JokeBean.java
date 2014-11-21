package com.bgood.xn.bean;

import java.util.List;

/**
 * @todo:笑话类
 * @date:2014-11-21 下午6:28:55
 * @author:hg_liuzl@163.com
 */
public class JokeBean {
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
	
	/**操作微墙
	 * 转发，还是评论
	 * */
	public enum WeiqiangActionType{		
		RESPONSE,TRANSPOND
	}
	
	
//	 <jokeid/>
//     <userid/>
// <username/>
// <photo/>
// <distance/>
// <date_time/>
// <content/>
// <imgs>
//    <img>product_img.jpg</img>//大图<img_thum>product_img_thum.jpg</img_thum>//缩略图
// </imgs>
//<like_count/>
//<comment_count/>
//<forward_count/>
//<share_count/>
	
}
