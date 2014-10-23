package com.bgood.xn.widget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.ui.BaseFragment;

/**
 * 
 * 初始化 带有一个返回按钮与一个标题的标题栏.
 * 
 */
public class TitleBar {
	private Activity mContext;

	private BaseFragment mFragment;

	public TitleBar(Activity context) {
		this.mContext = context;
	}

	public TitleBar(BaseFragment fragment) {
		mFragment = fragment;
	}

	public void initTitleBar(int titleId) {
		String title = mContext.getString(titleId);
		initTitleBar(title);
	}

	public void initTitleBar(String title) {
		initBackBtn();
		initTitle(title);
	}

	public void initBackTitleMenuBar(View container, String title) {
		View backBtn = container.findViewById(R.id.btn_back);
		TextView titleTV = (TextView) container.findViewById(R.id.tv_title);
		titleTV.setText(title);
		View menuBtn = container.findViewById(R.id.btn_right);
		ClickListener listener = new ClickListener();
		backBtn.setOnClickListener(listener);
		menuBtn.setOnClickListener(listener);
	}

	private void initBackBtn() {
		View backBtn = mContext.findViewById(R.id.btn_back);
		backBtn.setOnClickListener(new ClickListener());
	}

	private void initTitle(String title) {
		TextView titleTV = (TextView) mContext.findViewById(R.id.tv_title);
		titleTV.setText(title);
	}

	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				if (mFragment != null) {
					mFragment.finish();
				} else {
					mContext.finish();
				}
				break;
			case R.id.btn_right:
				break;
			default:
				break;
			}
		}
	}
}
