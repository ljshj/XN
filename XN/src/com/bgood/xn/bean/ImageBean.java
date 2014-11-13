package com.bgood.xn.bean;

import java.io.Serializable;

/**
 * 图片类
 */
public class ImageBean implements Serializable
{
	/**
	 * @todo:TODO
	 * @date:2014-11-13 上午11:54:26
	 * @author:hg_liuzl@163.com
	 */
	private static final long serialVersionUID = 1L;
	public String img;
	public String img_thum;
	
	public ImageBean() {
		super();
	}

	public ImageBean(String img) {
		super();
		this.img = img;
	}

	public ImageBean(String img, String img_thum) {
		super();
		this.img = img;
		this.img_thum = img_thum;
	}
	
}
