package com.bgood.xn.bean;

import com.bgood.xn.system.SystemConfig;

/**
 * @todo:关注类，关注我的与我关注的
 * @date:2014-10-29 下午6:52:28
 * @author:hg_liuzl@163.com
 */
public class AttentionBean {
	
	/**我的粉丝**/
	public static final int ATTENTION = 0;

	/**我的关注**/
	public static final int FANS = 1;
	
	public int distance;
	public int sex;
	public int searchtype;
	public String name;
	public String img;
	public int userid;
	public int level;
	public String signatrue;
	public int guanzhutype;
	public String ican;
	public String ineed;
	
	public String getImg() {
		return SystemConfig.FILE_SERVER + img;
	}
}

