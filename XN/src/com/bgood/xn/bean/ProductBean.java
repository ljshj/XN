package com.bgood.xn.bean;

import java.io.Serializable;

/**
 * 商品实体类
 */
public class ProductBean implements Serializable
{
	
	public static final String BEAN_PRODUCT = "bean_product";
	
    private static final long serialVersionUID = 1L;
    
    public String product_id;         // 商品Id号
	public String product_name;       // 商品名称
	public String img;    // 商品大图
	public String img_thum;  // 商品小图
	public String price;      // 商品价格
	public String intro;       // 商品介绍信息
	public String date_time;    // 商品上架时间
	public int isRecommend;		//是否推荐
}
