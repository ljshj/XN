/**
 * 系统项目名称
 * com.tt.uitest
 * PageListViewItemListener.java
 * 
 * 2013-8-16-下午8:15:32
 *  2013XX公司-版权所有
 * 
 */

package com.bgood.xn.view.pulltorefresh.listener;

import android.view.View;

/**
 * 
 * PageListViewItemListener
 * 
 * tanxiaoxing 2013-8-16 下午8:15:32
 * 
 * @version 1.0.0
 * 
 */

public interface IPageListViewItemListener {

	/**
	 * 可左右滑动listview 适配器第一页初始化getview方法回调，调用者实现
	 * 
	 * @param postion
	 *            postion
	 * @param firstItemView
	 *            第一页
	 * @param selectColorId
	 *            默认选中颜色id
	 * @return 是否是选中状态
	 */
	boolean getFirstView(int postion, View firstItemView);

	/**
	 * 可左右滑动listview 适配器 第二页初始化getview方法回调，调用者实现
	 * 
	 * @param postion
	 *            postion
	 * @param seconItemView
	 *            第二页
	 */
	void getSecondView(int postion, View seconItemView);

	/**
	 * 设置默认选中状态的颜色
	 * 
	 * @param postion
	 * @return selectColorId
	 */
	int getSelectColorId(int postion);

}
