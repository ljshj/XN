package com.bgood.xn.bean;

import com.bgood.xn.system.SystemConfig;

/**
 * @todo:橱窗实体类
 * @date:2014-10-21 下午3:29:47
 * @author:hg_liuzl@163.com
 */
public class CabinetResultBean {
	public String productid;      // 橱窗Id
	public String title;    // 橱窗名
	public String img;   // 橱窗图片
	public String price;     // 橱窗价格
	
	public String getPrice(){
		return "¥"+price;
	}
	
	public String getImg() {
		return SystemConfig.FILE_SERVER + img;
	}
}
