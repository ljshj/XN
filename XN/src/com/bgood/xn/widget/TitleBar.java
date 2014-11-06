package com.bgood.xn.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	
	private View layout;
	
	private Button backBtn;
	
	public Button rightBtn;
	
	private TextView titleTV;
	
	public TitleBar(Activity context,View v){
		this.mContext = context;
		this.layout = v;
	}

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
	
	public void initAllBar(String title) {
		initBackBtn();
		initRightBtn(null);
		initTitle(title);
	}
	
	public void initAllBar(String title,String rightBtnTitle) {
		initBackBtn();
		initRightBtn(rightBtnTitle);
		initTitle(title);
	}

	public void initBackTitleMenuBar(View container, String title) {
		backBtn = (Button) container.findViewById(R.id.btn_back);
		TextView titleTV = (TextView) container.findViewById(R.id.tv_title);
		titleTV.setText(title);
		View menuBtn = container.findViewById(R.id.btn_right);
		ClickListener listener = new ClickListener();
		backBtn.setOnClickListener(listener);
		menuBtn.setOnClickListener(listener);
	}

	private void initBackBtn() {
		if(layout!= null){
			backBtn = (Button) layout.findViewById(R.id.btn_back);
		}else{
			backBtn = (Button) mContext.findViewById(R.id.btn_back);
		}
		backBtn.setOnClickListener(new ClickListener());
	}
	
	private void initRightBtn(String rightBtnTitle) {
		if(layout!= null){
			rightBtn = (Button) layout.findViewById(R.id.btn_right);
		}else{
			rightBtn = (Button) mContext.findViewById(R.id.btn_right);
		}
		rightBtn.setText(TextUtils.isEmpty(rightBtnTitle)?"确定":rightBtnTitle);
	}
	
	public void setBackBtnVisible(int visibility){
		backBtn.setVisibility(visibility);
	}

	private void initTitle(String title) {
		
		if(layout!= null){
			titleTV = (TextView) layout.findViewById(R.id.tv_title);
		}else{
			titleTV = (TextView) mContext.findViewById(R.id.tv_title);
		}
		
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
			
			default:
				break;
			}
		}
	}
	
	public void doRightAction(){
		
	}
}
