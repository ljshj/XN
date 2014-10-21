package com.bgood.xn.bean;

import com.alibaba.fastjson.JSON;
/**
 * 
 * @author 登录返回
 *
 */
public class MemberLoginBean {
	public int userid;
	public String success;
	public String token;
	public String bserver;
	public String fserver;
	
	public MemberLoginBean parseBean(String strJson){
		return JSON.parseObject(strJson, MemberLoginBean.class);
	}
	
}
