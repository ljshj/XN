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

/**
 * 
 * IChangeStateListener
 * 
 * tanxiaoxing 2013-8-16 下午8:15:32
 * 
 * @version 1.0.0
 * 
 */

public interface IChangeStateListener {

	/**
	 * 向右滑动选中状态监听
	 * 
	 * @param postion
	 * @param isSelect
	 *            是否选择
	 * @return 返回选中颜色值ID
	 */
	int onChangeState(int postion, boolean isSelect);

}
