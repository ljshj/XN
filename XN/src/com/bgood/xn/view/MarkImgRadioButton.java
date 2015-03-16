package com.bgood.xn.view;

import com.bgood.xn.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @todo:自定义RadioButton,带有提醒功能的radioButton,有图片，有文字，有提醒的RadioButton
 * @date:2015-2-28 上午10:50:08
 * @author:hg_liuzl@163.com
 */
public class MarkImgRadioButton extends RelativeLayout {

	private ImageView ivIcon;
	private TextView tvName;
	private TextView tvTip;
	public MarkImgRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,attrs);
	}
	
	private void initView(Context context,AttributeSet attrs) {
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.markimgbar); 
		
		int icon  = a.getResourceId(R.styleable.markimgbar_mibicon, -1);
		int text  = a.getResourceId(R.styleable.markimgbar_mibkname, -1);
		int count = a.getResourceId(R.styleable.markimgbar_mibkcount, -1);
		
		
		LayoutInflater.from(context).inflate(R.layout.mark_img_radiobutton, this);
		
		ivIcon = (ImageView) findViewById(R.id.iv_img);
		ivIcon.setBackgroundResource(icon);
		
		tvName = (TextView) findViewById(R.id.tv_name);
		tvName.setText(text);
		
		tvTip = (TextView) findViewById(R.id.tv_tip);
		tvTip.setText(count);
		
		a.recycle();
		
	}
	
	/**
	 * 
	 * @todo:设置按钮名字
	 * @date:2015-2-28 上午11:09:56
	 * @author:hg_liuzl@163.com
	 * @params:@param strName
	 */
	public void setRBName(String strName){
		tvName.setText(strName);
	}
	
	/**
	 * 
	 * @todo:设置按钮提示文字
	 * @date:2015-2-28 上午11:10:12
	 * @author:hg_liuzl@163.com
	 * @params:@param strValue
	 */
	public void setRBTipValue(String count){
		if (TextUtils.isEmpty(count) || "0".equals(count)) {
			tvTip.setVisibility(View.GONE);
		} else {
			tvTip.setText(String.valueOf(count));
			if (Integer.valueOf(String.valueOf(count)) > 99) {
				tvTip.setText("99+");
			}
			tvTip.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 
	 * @todo:设置按钮提示文字
	 * @date:2015-2-28 上午11:10:12
	 * @author:hg_liuzl@163.com
	 * @params:@param strValue
	 */
	public void setRBTipValue(int count){
		if (0 == count) {
			tvTip.setVisibility(View.GONE);
		} else {
			tvTip.setText(String.valueOf(count));
			if (Integer.valueOf(String.valueOf(count)) > 99) {
				tvTip.setText("99+");
			}
			tvTip.setVisibility(View.VISIBLE);
		}
	}
}
