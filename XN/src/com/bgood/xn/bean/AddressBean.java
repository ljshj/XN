
package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.Comparator;

import com.bgood.xn.utils.PingYinUtil;

/** 
 * 城市信息实体类
 */
public class AddressBean implements Serializable,Comparator
{
	private static final long serialVersionUID = 1L;
	
	public String regionId;      // 自增id,用于数据库第一列
	public String parentId;      // 上级id
	public String regionName;    // 地区名
	public String regionType;    // 层级
	public String agencyId;      // "0"
    


	@Override
	public int compare(Object lhs, Object rhs) 
	{
		AddressBean o1 = (AddressBean) lhs;
		AddressBean o2 = (AddressBean) rhs;
		String str1 = PingYinUtil.getPingYin(o1.regionName);
		String str2 = PingYinUtil.getPingYin(o2.regionName);
		return str1.compareTo(str2);
	}
}
