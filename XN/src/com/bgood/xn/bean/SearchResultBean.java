package com.bgood.xn.bean;

import java.util.List;

/**
 * 搜索结果实体类
 */
public class SearchResultBean
{
	public HallBean hall;   // 临时群
	public List<MemberBean> members;  // 会员列表
	public List<WeiQiangBean> weiqiang;  // 微墙列表
	public List<CabinetBean> cabinet;  // 橱窗列表
	public List<AppBean> apps;   // 应用列表
	
	
	public List<MemberBean> getMembers(){
		
		for(int i = 0;i<3;i++){
			MemberBean mb = new MemberBean("userid"+i, "", "user"+i, "男"+i, ""+i, "1111"+i, "哈"+i, "我能吃饭"+i, "我要大吃"+i);
			members.add(mb);
		}
		return members;
	}
	
	public List<WeiQiangBean> getWeiqiang(){
		for(int i = 0;i<3;i++){
			WeiQiangBean mb = new WeiQiangBean("userid"+i, "张三"+i, "我是一个吃货"+i, "","");
			weiqiang.add(mb);
		}
		return weiqiang;
	}
	
	public List<CabinetBean> getCabinets(){
		for(int i = 0;i<3;i++){
			CabinetBean cb = new CabinetBean("product"+i, "张三小店", "", "", "99"+i);
			cabinet.add(cb);
		}
		return cabinet;
	}
}


