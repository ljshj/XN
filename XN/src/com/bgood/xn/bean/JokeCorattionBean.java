package com.bgood.xn.bean;

import java.util.List;

import com.bgood.xn.system.SystemConfig;


/**
 * 幽默秀与我相关的实体类
 * @author Administrator
 *
 */
public class JokeCorattionBean {
		public String jokeid;
		public String userid;
		public String name;
		public String photo;
		public String distance;
		public String date_time;
		public String content;
		public List<ImageBean> imgs;
		public String comment_count;
		public String share_count;
		public String forward_count;
		public String like_count;
		public String fromuserid;
		public List<CommentBean> commentlist;
		public List<SimpleUserBean> likeuserlist;
		public String type;
		public String fromname;
		public String opertime;
		public String comments;
		public int yuanchuang;
		
		public String getPhoto() {
			return SystemConfig.FILE_SERVER + photo;
		}
}
