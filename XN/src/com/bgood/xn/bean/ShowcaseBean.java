package com.bgood.xn.bean;

import java.util.List;

import com.bgood.xn.system.SystemConfig;

/**
 * 
 * @todo:橱窗实体类
 * @date:2014-11-17 上午10:26:38
 * @author:hg_liuzl@163.com
 */
public class ShowcaseBean
{
	public String userid;                     // 用户Id号
	public String shop_name;                  // 商店名
	public String logo;                       // 商店Logo
	public String good_comments;              // 好评数
	public String credit;                     // 信用值
	public String product_count;              // 商品总数
	public List<ProductBean> recommend_list;     // 商品列表
	
	public String getLogo() {
		return SystemConfig.FILE_SERVER + logo;
	}
}
