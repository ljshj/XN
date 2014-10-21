package com.bgood.xn.bean;

import java.util.ArrayList;

/**
 * 搜索结果实体类
 */
public class SearchResultBean
{
	public HallBean hallDTO;   // 临时群
	public ArrayList<UserBean> memberList;  // 会员列表
	public ArrayList<WeiQiangBean> weiqiangList;  // 微墙列表
	public ArrayList<CabinetBean> cabinetList;  // 橱窗列表
	public ArrayList<AppBean> appList;   // 应用列表
}
