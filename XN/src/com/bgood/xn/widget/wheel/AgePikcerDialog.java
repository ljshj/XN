package com.bgood.xn.widget.wheel;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.bgood.xn.R;

/**
 * 
 * @todo:年龄选择
 * @date:2014-11-4 下午6:09:45
 * @author:hg_liuzl@163.com
 */
public class AgePikcerDialog extends AlertDialog implements OnClickListener{
	private static int START_YEAR = 1,END_YEAR=100;
	private final OnDateAgeSetListener mCallBack;
	private WheelView wv_age;
	private int curr_age;
	
	public AgePikcerDialog(Context context, final String age,OnDateAgeSetListener callBack) {
		super(context);
		int currentAge = Integer.valueOf(age);
		mCallBack = callBack;
		setCancelable(false);
        setButton("确定",this);
        setButton2("取消", (OnClickListener) null);
		setTitle("选择您的年龄");
		// 找到dialog的布局文件
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.age_picker_layout, null);
		int textSize = 0;
		textSize = adjustFontSize(getWindow().getWindowManager()); 
		// 年
		wv_age = (WheelView) view.findViewById(R.id.age);
		wv_age.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_age.setCyclic(false);// 可循环滚动
		wv_age.setLabel("    岁");// 添加文字
		wv_age.setCurrentItem(currentAge - START_YEAR);// 初始化时显示的数据
		wv_age.TEXT_SIZE = textSize;
		setView(view);
	}
	
	public void onClick(DialogInterface dialog, int which) {

		curr_age = wv_age.getCurrentItem() + START_YEAR;
		if (mCallBack != null) {
			mCallBack.onDateAgeSet(curr_age);
		}
	}
	 public void show() {
	        super.show();
	 }
	public  interface OnDateAgeSetListener {
			void onDateAgeSet(int age);
		}
	public static int adjustFontSize(WindowManager windowmanager) {

		 int screenWidth = windowmanager.getDefaultDisplay().getWidth();
	     int screenHeight = windowmanager.getDefaultDisplay().getHeight();
	     /*  DisplayMetrics dm = new DisplayMetrics();
	      dm = windowmanager.getApplicationContext().getResources().getDisplayMetrics();
	     int widthPixels = dm.widthPixels;
	     int heightPixels = dm.heightPixels;
	     float density = dm.density;
	     fullScreenWidth = (int)(widthPixels * density);
	     fullScreenHeight = (int)(heightPixels * density);*/
		if (screenWidth <= 240) { // 240X320 屏幕
			return (int) (1.5*10);
		} else if (screenWidth <= 320) { // 320X480 屏幕
			return (int) (1.5*14);
		} else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
			return (int) (1.5*24);
		} else if (screenWidth <= 540) { // 540X960 屏幕
			return (int) (1.5*26);
		} else if (screenWidth <= 800) { // 800X1280 屏幕
			return (int) (1.5*30);
		} else { // 大于 800X1280
			return (int) (1.5*30);
		}
	}
}

