package com.bgood.xn.bean;

import java.util.List;

import com.bgood.xn.system.SystemConfig;

/**
 * @todo:微墙，与我相关的实体类
 * @date:2014-12-4 下午6:59:51
 * @author:hg_liuzl@163.com
 */
public class WeiqiangCorattionBean {
	  public String comment_count;
	  public String share_count;
	  public String forward_count;
	  public String like_count;
	  public String fromuserid;
	  public List<ImageBean> imgs;
	  public List<CommentBean> commentlist;
	  public String weiboid;
	  public String userid;
	  public List<SimpleUserBean> likeuserlist;
	  public String type;
	  public String photo;
	  public String content;
	  public String distance;
	  public String name;
	  public String fromname;
	  public String opertime;
	  public String date_time;
	  public String comments;
	  
	public String getPhoto() {
		return SystemConfig.FILE_SERVER + photo;
	}
  
	  
}
//"comment_count": 1,
//"fromuserid": 0,
//"imgs": [
//    
//],
//"commentlist": [
//    
//],
//"weiboid": 456,
//"share_count": 0,
//"userid": 11,
//"likeuserlist": [
//    
//],
//"type": "",
//"photo": "http:\/\/114.215.189.189:80\/Files\/11\/userInfo\/small2fdbe1e6-8ae4-4977-8825-cf6fb88bf022.png",
//"content": "测试提到我的",
//"distance": "",
//"forward_count": 0,
//"like_count": 1,
//"name": "邦固产品",
//"fromname": "",
//"opertime": "2014-12-05 09:32:02",
//"date_time": "2014-12-05 09:28:22",
//"comments": ""