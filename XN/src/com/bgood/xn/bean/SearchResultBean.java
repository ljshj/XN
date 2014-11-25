package com.bgood.xn.bean;

import java.util.List;

/**
 * 搜索结果实体类
 */
public class SearchResultBean
{
	public HallBean hall;   // 临时群
	public List<MemberResultBean> members;  // 会员列表
	public List<WeiQiangBean> weiqiang;  // 微墙列表
	public List<CabinetResultBean> cabinet;  // 橱窗列表
	public List<AppBean> apps;   // 应用列表
}


