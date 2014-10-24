package com.bgood.xn.bean;
/**
 * @todo:橱窗实体类
 * @date:2014-10-21 下午3:29:47
 * @author:hg_liuzl@163.com
 */
public class CabinetResultBean {
	public String productid;      // 橱窗Id
	public String title;    // 橱窗名
	public String img;   // 橱窗图片
	public String img_thum;   // 橱窗图片
	public String price;     // 橱窗价格
	
	
	
	public CabinetResultBean() {
		super();
	}



	public CabinetResultBean(String productid, String title, String img,
			String img_thum, String price) {
		super();
		this.productid = productid;
		this.title = title;
		this.img = img;
		this.img_thum = img_thum;
		this.price = price;
	}
	
	
}
