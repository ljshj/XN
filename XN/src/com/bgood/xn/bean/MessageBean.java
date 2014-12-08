package com.bgood.xn.bean;
/**
 * @todo:消息实体类
 * @date:2014-12-8 下午4:55:04
 * @author:hg_liuzl@163.com
 */
public class MessageBean {
	
	public String sendername;
	public String sender;
	public String recver;
	
	/**标识消息类型
	 * 0,好友普通消息;
     * 1,群普通消息**/
	public int type;	
	public String datetime;
	public String info;
	
//	<sendername/> //发送人
//	<sender/> //id
//	<recver/>//如果是群聊这里填群号码
//	<type/>  //标识消息类型
//	//0,好友普通消息;
//	//1,群普通消息
//	<datetime/>//时间
//	<info/>//内容
	
}
