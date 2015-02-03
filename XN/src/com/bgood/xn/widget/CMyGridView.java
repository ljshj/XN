package com.bgood.xn.widget;

import android.widget.GridView;

/**
 * CMyGridView继承GridView显示页面
 */
public class CMyGridView extends GridView 
{
	public CMyGridView(android.content.Context context,
			android.util.AttributeSet attrs) 
	{
		super(context, attrs);
	}

	
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}