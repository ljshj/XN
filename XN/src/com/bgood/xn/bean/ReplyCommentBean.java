package com.bgood.xn.bean;

import java.io.Serializable;

/**
 * 评论的实体类
 */
public class ReplyCommentBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String revertID;
	public String senderID;
	public String senderName;
	public String senderIcon;
	public String sendTime;
	public String content;
	public String commentID;

}
