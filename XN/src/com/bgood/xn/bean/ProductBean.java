package com.bgood.xn.bean;

import java.io.Serializable;

/**
 * 商品实体类
 */
public class ProductBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    public String productId;         // 商品Id号
	public String productName;       // 商品名称
	public String productBigIcon;    // 商品大图
	public String productSmallIcon;  // 商品小图
	public String productPrice;      // 商品价格
	public String productInfo;       // 商品介绍信息
	public String productAddTime;    // 商品上架时间
}
