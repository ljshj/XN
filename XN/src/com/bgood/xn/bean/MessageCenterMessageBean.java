package com.bgood.xn.bean;

import java.io.Serializable;

import com.bgood.xn.system.SystemEnum;

/**
 * 消息中心
 */
public class MessageCenterMessageBean implements Serializable
{
	/* message class , 这个功能上保留，但底层程序已经对该属性进行了处理 */
	public enum FDSMessageCenterMessage_Class
	{
		FDSMessageCenterMessage_Class_NONE, FDSMessageCenterMessage_Class_USER, // 个人消息
		FDSMessageCenterMessage_Class_SYSTEM, // 系统消息
		FDSMessageCenterMessage_Class_MAX
	};

	/* message type */
	public enum FDSMessageCenterMessage_Type
	{
		FDSMessageCenterMessage_Type_NONE, FDSMessageCenterMessage_Type_CHAT_PERSON, // liao
																						// tian
		FDSMessageCenterMessage_Type_CHAT_GROUP, FDSMessageCenterMessage_Type_ADD_FRIEND_REQUEST, // 添加好友申请
		FDSMessageCenterMessage_Type_ADD_FRIEND_RESULT, // 添加好友结果
		FDSMessageCenterMessage_Type_MAX
	};


	/* group type */
	public String m_id;
	public FDSMessageCenterMessage_Class m_messageClass;
	public FDSMessageCenterMessage_Type m_messageType;
	public String m_icon;
	public int m_iconRes;
	public String m_senderID;
	public String m_senderName;
	public String m_content;
	public String m_sendTime;// 发送时间
	public SystemEnum.FDS_Message_State m_state;
	public String m_param1;/* 保留参数1：在消息列表中这个用来表示群聊天的时候，表示是企业群，还是球队群 */
	public String m_param2;/* 保留参数2：在消息列表中这个用来表示群聊天的时候，这个来表示企业ID */
	public int m_newMessageCount;// 如果为0则没有新消息
}
