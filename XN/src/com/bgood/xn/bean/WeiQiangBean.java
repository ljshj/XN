package com.bgood.xn.bean;

import java.io.Serializable;

/**
 * @todo:微墙类
 * @date:2014-10-21 下午3:29:24
 * @author:hg_liuzl@163.com
 */
public class WeiQiangBean implements Serializable {
	/**
	 * @todo:TODO
	 * @date:2014-10-23 下午5:11:25
	 * @author:hg_liuzl@163.com
	 */
	private static final long serialVersionUID = 1L;
	public String id;      // 微墙Id
	public String name;    // 微墙名
	public String content; // 微墙内容
	public String img;     // 微墙图片
	public String img_thum; // 微墙缩略图
	public WeiQiangBean() {
		super();
	}
	public WeiQiangBean(String id, String name, String content, String img,
			String img_thum) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
		this.img = img;
		this.img_thum = img_thum;
	}
	
	
}

