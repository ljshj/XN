package com.bgood.xn.bean;

import java.io.Serializable;
/**
 * @todo:群组实体类
 * @date:2014-12-18 下午6:40:09
 * @author:hg_liuzl@163.com
 */
public class GroupBean implements Serializable
{
	/**
	 * @todo:TODO
	 * @date:2014-12-18 下午6:40:03
	 * @author:hg_liuzl@163.com
	 */
	private static final long serialVersionUID = 1L;
	public String roomid;
	public String name;
	public String intro;
	public String photo;
	public int grouptype;//群类型 0，固定群；1，临时群
	
}
