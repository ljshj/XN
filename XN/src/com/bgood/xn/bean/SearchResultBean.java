package com.bgood.xn.bean;

import java.util.List;

/**
 * 搜索结果实体类
 */
public class SearchResultBean
{
	public HallBean hall;   // 临时群
	public List<MemberResultBean> members;  // 会员列表
	public List<WeiQiangResultBean> weiqiang;  // 微墙列表
	public List<CabinetResultBean> cabinet;  // 橱窗列表
	public List<AppBean> apps;   // 应用列表
	
	
	public List<MemberResultBean> getMembers(){
		
		for(int i = 0;i<3;i++){
			MemberResultBean mb = new MemberResultBean("userid"+i, "", "user"+i, "男"+i, ""+i, "1111"+i, "哈"+i, "我能吃饭"+i, "我要大吃"+i);
			members.add(mb);
		}
		return members;
	}
	
	public List<WeiQiangResultBean> getWeiqiang(){
		for(int i = 0;i<3;i++){
			WeiQiangResultBean mb = new WeiQiangResultBean("userid"+i, "张三"+i, "我是一个吃货"+i, "","");
			weiqiang.add(mb);
		}
		return weiqiang;
	}
	
	public List<CabinetResultBean> getCabinets(){
		for(int i = 0;i<3;i++){
			CabinetResultBean cb = new CabinetResultBean("product"+i, "张三小店", "", "", "99"+i);
			cabinet.add(cb);
		}
		return cabinet;
	}
}


