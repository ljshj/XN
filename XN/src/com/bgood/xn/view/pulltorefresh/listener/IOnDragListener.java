/**
 * 系统项目名称
 * com.tt.widget.listener
 * IOnDragListener.java
 * 
 * 2013-8-18-下午1:31:19
 *  2013XX公司-版权所有
 * 
 */

package com.bgood.xn.view.pulltorefresh.listener;

/**
 * 
 * IOnDragListener 拖曳监听
 * 
 * tanxiaoxing 2013-8-18 下午1:31:19
 * 
 * @version 1.0.0
 * 
 */

public interface IOnDragListener {

	/**
	 * 完成拖曳回调
	 * 
	 * @param startIndex
	 *            开始位置
	 * @param toIndex
	 *            结束位置
	 */
	void onDragFinish(int startIndex, int toIndex);

}
