package com.bgood.xn.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 *自定义控件，操作赞，评论，转发，分享
 */
public class ActionView extends LinearLayout {

	private ImageView iv;
	private TextView tvCount;
	
	public ActionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}
	
	private void initView(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.actionBar); 
		int text  = a.getResourceId(R.styleable.actionBar_text, -1);
		int img = a.getResourceId(R.styleable.actionBar_img, -1);
		
		LayoutInflater.from(context).inflate(R.layout.layout_action_view, this);
		iv = (ImageView) findViewById(R.id.iv_type);
		iv.setBackgroundResource(img);
		
		tvCount = (TextView) findViewById(R.id.tv_count);
		tvCount.setText(text);
		
		a.recycle();
	}
	
	public void setCount(int count){
		tvCount.setText(String.valueOf(count));
	}
	
	public void setCount(String count){
		tvCount.setText(count);
	}
	
	public void setText(String text){
		tvCount.setText(text);
	}
}
