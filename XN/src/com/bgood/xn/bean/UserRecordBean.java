package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.List;

public class UserRecordBean implements Serializable
{
	public String recordID;
	public String senderName;
	public String senderID;
	public String senderIcon;
	public String sendTime;
	public String content;
	public String[] images;
	public List<CommentBean> comments;

}
