package com.bgood.xn.bean;

import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.utils.ImgUtils;

/** 
 * 最近浏览实体类
 */
public class SearchBrowseBean
{
    public String imagePath;     // 图片路径
    public String price;         // 商品价格
    public String productId;     // 商品Id
    public String productName;   // 商品名
    public String orgPrice;      // 市场价
    public String colorCount;    // 颜色总数
  
	public String getImagePath() {
		return SystemConfig.FILE_SERVER + imagePath;
	}
}
