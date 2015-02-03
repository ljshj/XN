package com.bgood.xn.bean;

import com.bgood.xn.system.SystemConfig;

/**
 * @todo:应用类
 * @date:2014-10-21 下午3:29:03
 * @author:hg_liuzl@163.com
 */
public class AppBean {
	public String id;       // 应用Id
	public String title;     // 应用名
	public String size;     // 应用大小
	public String intro;     // 应用详情
	public String img;      // 应用图片
	public String img_thum;  // 应用缩略图
	
	public String getImg() {
		return SystemConfig.FILE_SERVER + img;
	}
	
	public String getImgThum() {
		return SystemConfig.FILE_SERVER + img_thum;
	}
}
