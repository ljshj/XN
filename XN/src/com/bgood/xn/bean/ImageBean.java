package com.bgood.xn.bean;

import java.io.Serializable;

import android.text.TextUtils;

import com.bgood.xn.system.SystemConfig;

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

	
	public String getImg() {
    	if(!TextUtils.isEmpty(img) && !img.contains("http")){
    		img = SystemConfig.FILE_SERVER+img;
    	}
		return img;
	}

	public String getImg_thum() {
    	if(!TextUtils.isEmpty(img_thum) && !img_thum.contains("http")){
    		img_thum = SystemConfig.FILE_SERVER+img_thum;
    	}
		return img_thum;
	}
	
}
