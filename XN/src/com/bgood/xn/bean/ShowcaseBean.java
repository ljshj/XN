package com.bgood.xn.bean;

import java.util.ArrayList;

/**
 * 橱窗实体类
 */
public class ShowcaseBean
{
	public String userId;                      // 用户Id号
	public String shopName;                    // 商店名
	public String shopLogo;                    // 商店Logo
	public String comments;                    // 好评数
	public String credit;                      // 信用值
	public String goodCount;                // 商品总数
	public String productCount;                // 商品总数
	public ArrayList<ProductBean> productList;  // 商品列表
}
