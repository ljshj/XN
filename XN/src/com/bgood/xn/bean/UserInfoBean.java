package com.bgood.xn.bean;

import java.io.Serializable;


/**
 * @todo:用户资料实体类
 * @date:2014-10-20 下午4:06:36
 * @author:hg_liuzl@163.com
 */
public class UserInfoBean implements Serializable {
	/**用户间传bean的key*/
	public static final String KEY_USER_BEAN = "user_info_bean";
	/**用户的编号*/
	public static final String KEY_USER_ID = "user_id";
//	public String roleID; // 用户权限ID
//	/* fds搬运 */
//	public String m_job;
//	public String m_company;
//	public String m_remarkName;
//	public String m_brief;
//	public UserRelation m_relation;
//	public String m_localDtaID;// 本地数据库ID
//
//	public String userAccount;  // 用户账号
//    public String userName;     // 用户姓名
//	public String userPhone;    // 用户手机号码
//	public String userPassword; // 密码
//	public String userId;       // 用户Id号
//	public String userIcon;     // 用户头像
//
//	/**
//	 * 登录用户类型
//	 */
//	public enum UserState
//	{
//		UserState_NONE, UserState_NO_ACCOUNT, UserState_HAS_ACCOUNT_NO_LOGIN_SETTING, UserState_HAS_ACCOUNT_NO_LOGIN_NET, // because
//																															// net
//		UserState_LOGINED, UserState_MAX
//	};
//
//	public enum UserRelation
//	{
//		UserRelation_NONE, UserRelation_NO, UserRelation_FRIEND, UserRelation_MAX
//	};

//	
	
	
	
	
	//<username/>//能能号
//	<ican/>//我能
//	<ineed/>//我想
//	<photo/>//头像
//	<nickn/>//昵称
//	<signature/>//签名
//	<sex/>//性别
//	<birthday/>//生日
//	<conste/>//星座
//	<hometown/>//家乡
//	<loplace/>//当前住址
//	<email/>//邮箱
//	<btype/>//血型
//	<nicktitle/>//头衔
//	<level/>//用户等级
//	<favor/>//好评
//	<credit/>//好评
//	<exp/>//经验值
//	<age/>
//	<phonenumber/>
//	<guanzhu/>//我的关注数
//	<fansnumber/>//我的粉丝数
//	<weiqiang/>//我的微墙数
	
	public String userid;
	public String username;
	public String ican;
	public String ineed;
	public String photo;
	public String nickn;
	public String signature;
	public int sex;
	public String birthday;
	public String conste;
	public String hometown;
	public String loplace;
	public String email;
	public String btype;
	public String nicktitle;
	public int level;
	public String favor;
	public String credit;
	public String exp;
	public String age;
	public String phonenumber;
	public String guanzhu;
	public String fansnumber;
	public String weiqiang;
}

