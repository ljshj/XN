package com.bgood.xn.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 用于解决ViewPager与ScrollView滑动冲突问题
 */
public class VerticalScrollView extends ScrollView 
{

	/*private GestureDetector mGestureDetector;

	public VerticalScrollView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) 
	{
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener 
	{

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) 
		{
			*//**
			 * 如果我们滚动更接近水平方向,返回false,让子视图来处理它
			 *//*
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}*/
    
    private GestureDetector mGestureDetector;  
    
    public VerticalScrollView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    public VerticalScrollView(Context context) {  
        super(context);  
        init();  
    }  
  
    public VerticalScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }  
  
    private void init() {  
        mGestureDetector = new GestureDetector(getContext(),  
                new YScrollDetector());  
        setFadingEdgeLength(0);  
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        return super.onInterceptTouchEvent(ev)  
                && mGestureDetector.onTouchEvent(ev);  
    }  
  
    private class YScrollDetector extends SimpleOnGestureListener {  
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                float distanceX, float distanceY) {  
              
            if (Math.abs(distanceY) >= Math.abs(distanceX)) {  
                return true;  
            }  
            return false;  
        }  
    }  
	
	// 滑动距离及坐标
    /*private float xDistance, yDistance, xLast, yLast;

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                
                if(xDistance > yDistance){
                    return false;
                }  
        }

        return super.onInterceptTouchEvent(ev);
    }*/
}

