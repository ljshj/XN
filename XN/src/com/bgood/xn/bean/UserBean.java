package com.bgood.xn.bean;


/**
 * @todo:TODO
 * @date:2014-10-20 下午4:06:36
 * @author:hg_liuzl@163.com
 */
public class UserBean {
	public String roleID; // 用户权限ID
	/* fds搬运 */
	public String m_job;
	public String m_company;
	public String m_remarkName;
	public String m_brief;
	public UserRelation m_relation;
	public String m_localDtaID;// 本地数据库ID

	public String userAccount;  // 用户账号
    public String userName;     // 用户姓名
	public String userPhone;    // 用户手机号码
	public String userPassword; // 密码
	public String userId;       // 用户Id号
	public String userIcon;     // 用户头像
	public String ican;         // 我能
	public String ineed;        // 我想
	public String nickn;        // 昵称
	public String signature;    // 个性签名
	public int sex;          // 性别
	public String birthday;     // 生日
	public String conste;       // 星座
	public String hometown;     // 故乡
	public String loplace;      // 现居住地
	public String email;        // 邮箱
	public String btype;        // 血型
	public String nicktitle;    // 头衔
    public String level;        // 用户等级
    public String favor;        // 好评
    public String credit;       // 好评
    public String exp;          // 经验值
    public String guanzhu;      // 我的关注数
    public String fansnumber;   // 我的粉丝数
    public String weiqiang;     // 我的微墙数
    public String age;          // 年龄
    public int follow;          // 关注
    public String distance;     // 距离
    public int searchType;      // 关注
    public int guanzhuType;     // 关注 0 单方关注 1 相互关注


	/**
	 * 登录用户类型
	 */
	public enum UserState
	{
		UserState_NONE, UserState_NO_ACCOUNT, UserState_HAS_ACCOUNT_NO_LOGIN_SETTING, UserState_HAS_ACCOUNT_NO_LOGIN_NET, // because
																															// net
		UserState_LOGINED, UserState_MAX
	};

	public enum UserRelation
	{
		UserRelation_NONE, UserRelation_NO, UserRelation_FRIEND, UserRelation_MAX
	};

}
