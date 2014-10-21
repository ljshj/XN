package com.bgood.xn.bean;

import java.io.Serializable;
/**
 * 群组实体类
 */
public class GroupBean implements Serializable
{
	public String groupID;
	public String groupName;
	public String groupIcon;
	public RELATION relation = RELATION.NO;
	
	public enum RELATION
	{
		NO, MEMBER, SALESMAN, SUPERADMIN, ADMIN, NOUSER;
	}
}
