/**
 * 系统项目名称
 * com.tt.uitest
 * IListViewItemListener.java
 * 
 * 2013-8-17-上午11:01:49
 *  2013XX公司-版权所有
 * 
 */

package com.bgood.xn.view.pulltorefresh.listener;

import android.view.View;

/**
 * 
 * IListViewItemListener listview初始化item监听
 * 
 * tanxiaoxing 2013-8-17 上午11:01:49
 * 
 * @version 1.0.0
 * 
 */

public interface IListViewItemListener {
	
	/**
	 * 获取item标题view
	 * @param position
	 * @param titleView  使用者传入的view
	
	*/
	void getItemTitleView(int position, View titleView); 
}
