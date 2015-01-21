package com.bgood.xn.bean;

import com.bgood.xn.system.SystemConfig;

/**
 * 临时群实体类
 */
public class HallBean
{
	public String hallid;    // 临时群Id
	public String title;  // 临时表群名字
	public String img;  // 临时群图标
	public String count; // 用户人总数
	
	public String getImg() {
		return SystemConfig.FILE_SERVER + img;
	}
}
