package com.bgood.xn.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * @todo:带红色标记的RadioButton
 * @date:2015-1-29 下午6:20:28
 * @author:hg_liuzl@163.com
 */
public class MarkRadioButton extends RadioButton {

	private TextView tvTitle;
	private TextView tvCount;
	
	public MarkRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}
	
	private void initView(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.markRadionButton); 
	//	int text  = a.getResourceId(R.styleable.markRadionButton_txtTitle, -1);
		int textCount = a.getResourceId(R.styleable.markRadionButton_txtCount, -1);
		
		View v = LayoutInflater.from(context).inflate(R.layout.layout_markl_radiobutton, null);
		
//		tvTitle = (TextView) v.findViewById(R.id.tv_title);
//		tvTitle.setText(text);
		
		tvCount = (TextView) v.findViewById(R.id.tv_count);
		tvCount.setText(textCount);
		
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
