package com.bgood.xn.widget;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * 字母表
 */
public class ZZCityQuickAlphabeticBar extends ImageButton
{
	private TextView mDialogText; // 中间显示字母的文本框
	private Handler mHandler; // 处理UI的句柄
	private ListView mList; // 列表
	private float mHight; // 高度
	// 字母列表索引
	private String[] letters = new String[] { "#", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };
	// 字母索引哈希表
	private HashMap<String, Integer> alphaIndexer;
	Paint paint = new Paint();
	boolean showBkg = false;
	int choose = -1;

	public ZZCityQuickAlphabeticBar(Context context) {
		super(context);
	}

	public ZZCityQuickAlphabeticBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ZZCityQuickAlphabeticBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 初始化
	public void init(Activity ctx) {
		mDialogText = (TextView) ctx.findViewById(R.id.city_tv_fast_position);
		mDialogText.setVisibility(View.INVISIBLE);
		mHandler = new Handler();
	}

	// 设置需要索引的列表
	public void setListView(ListView mList) {
		this.mList = mList;
	}

	// 设置字母索引哈希表
	public void setAlphaIndexer(HashMap<String, Integer> alphaIndexer) {
		this.alphaIndexer = alphaIndexer;
	}

	// 设置字母索引条的高度
	public void setHight(float mHight) {
		this.mHight = mHight;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int act = event.getAction();
		float y = event.getY();
		mHight = this.getHeight();
		// 计算手指位置，找到对应的段，让mList移动段开头的位置上
		int selectIndex = (int) (y / (mHight / 27));
		if (0 < selectIndex && selectIndex < 27)
		{// 防止越界
			String key = letters[selectIndex];

			if (key != null && alphaIndexer != null && alphaIndexer.containsKey(key))
			{
				int pos = alphaIndexer.get(key);
				if (mList.getHeaderViewsCount() > 0)
				{// 防止ListView有标题栏，本例中没有。
					this.mList.setSelectionFromTop(pos + mList.getHeaderViewsCount(), 0);
				} else
				{
					this.mList.setSelectionFromTop(pos, 0);
				}
			}
			mDialogText.setText(letters[selectIndex]);
		}
		if (act == MotionEvent.ACTION_DOWN)
		{
			if (mHandler != null)
			{
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						if (mDialogText != null && mDialogText.getVisibility() == View.INVISIBLE)
						{
							mDialogText.setVisibility(VISIBLE);
						}
					}
				});
			}
		} else if (act == MotionEvent.ACTION_UP)
		{
			if (mHandler != null)
			{
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						if (mDialogText != null && mDialogText.getVisibility() == View.VISIBLE)
						{
							mDialogText.setVisibility(INVISIBLE);
						}
					}
				});
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int sigleHeight = height / letters.length; // 单个字母占的高度
		for (int i = 0; i < letters.length; i++) {
			paint.setColor(Color.BLACK);
			paint.setTextSize(20);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			if (i == choose) { 
				paint.setColor(Color.parseColor("#00BFFF")); // 滑动时按下字母颜色
				paint.setFakeBoldText(true);
			}
			// 绘画的位置
			float xPos = width / 2 - paint.measureText(letters[i]) / 2;
			float yPos = sigleHeight * i + sigleHeight;
			canvas.drawText(letters[i], xPos, yPos, paint);
			paint.reset();
		}
	}
}
