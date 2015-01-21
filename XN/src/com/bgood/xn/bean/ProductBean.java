package com.bgood.xn.bean;

import java.io.Serializable;

import com.bgood.xn.system.SystemConfig;

/**
 * 
 * @todo:商品实体类
 * @date:2014-11-25 上午11:33:27
 * @author:hg_liuzl@163.com
 */
public class ProductBean implements Serializable
{
	
	public static final String KEY_PRODUCT_ID = "product_id";
	
	public static final String KEY_PRODUCT_BEAN = "product_bean";
	
    private static final long serialVersionUID = 1L;
    
    public String product_id;         // 商品Id号
	public String product_name;       // 商品名称
	public String img;    // 商品大图
	public String img_thum;  // 商品小图
	public String price;      // 商品价格
	public String intro;       // 商品介绍信息
	public String date_time;    // 商品上架时间
	public int brecom;		//是否推荐
	
	public String getPrice(){
		return "¥"+price;
	}
	
	public String getImg() {
		return SystemConfig.FILE_SERVER + img;
	}
	
	public String getImgThum() {
		return SystemConfig.FILE_SERVER + img_thum;
	}
}

