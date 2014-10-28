//package com.bgood.xn.bean;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
///**
// * @todo:评论实体类
// * @date:2014-10-21 下午6:27:34
// * @author:hg_liuzl@163.com
// */
//public class CommentBean implements Serializable {
//	public String commentId;// 评论的唯一ID
//	public String content;   // 内容
//	public String senderID;  // 发送用户Id
//	public String senderIcon;  // 发送用户头像
//	public String senderName;  // 发送用户姓名
//	public String sendTime;  // 发送时间
//	public CommentObjectType commentObjectType;
//	public String commentObjectID;
//	public ArrayList<ImageBean> imageList;
//	public int revertCount;  // 评论数
//	public ArrayList<WeiqiangCommentBean> reverts = new ArrayList<WeiqiangCommentBean>();
//	public String reveredName;
//	public String distance;
//	public String from_userID;
//	public String from_name;
//	public int like_count;   // 赞数
//	public int forward_count;  // 转发数
//	public int share_count;  // 分享数
//
//	public enum CommentObjectType implements Serializable
//	{
//		weiqiang, xuanneng;
//	}
//}
