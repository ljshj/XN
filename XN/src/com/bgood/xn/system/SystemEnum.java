package com.bgood.xn.system;

public class SystemEnum
{
	/* 定义角色类型： */
	public enum FDS_Role_Type
	{
		FDS_Role_Type_NONE, FDS_Role_Type_User, // 个人
		FDS_Role_Type_Group, // 公司
		FDS_Role_Type_MAX
	};

	/* 定义系统消息类型 */
	public enum FDS_Message_Type
	{
		FDS_Message_Type_NONE, FDS_Message_Type_AddFriend_Request, FDS_Message_Type_AddFriend_Reply, FDS_Message_Type_MAX
	};

	/* 定义消息状态 */
	public enum FDS_Message_State
	{
		FDS_Message_State_NONE, FDS_Message_State_unDone, // 未处理
		FDS_Message_State_Done, // 处理状态
		FDS_Message_State_MAX
	};

	public enum FDS_ACTIVITY_UPDATE_TYPE
	{
		FDS_Message_State_NONE, FDS_ACTIVITY_UPDATE_TYPE_ADD, FDS_ACTIVITY_UPDATE_TYPE_UPDATE, FDS_ACTIVITY_UPDATE_TYPE_DELETE, FDS_Message_State_MAX
	};

	/* 定义结果 */
	public enum Result
	{
		FDS_Result_NONE, FDS_Result_OK, FDS_Result_FAIL, FDS_Result_PARAME_ERROR, FDS_Result_MEMORY_LOW, FDS_Result_NO_NET, FDS_Result_MAX
	};
	/* define process result */

}
