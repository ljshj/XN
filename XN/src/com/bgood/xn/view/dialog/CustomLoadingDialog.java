package com.bgood.xn.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * @todo:自定义进度条
 * @date:2014-11-10 下午2:12:11
 * @author:hg_liuzl@163.com
 */
public class CustomLoadingDialog extends Dialog {
	private TextView tvMessage;

	public void setMessage(String message){
		tvMessage.setText(message);
	}

	public CustomLoadingDialog(Context context) {
		super(context,R.style.loadingDialogStyle);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_progress_layout);
		tvMessage = (TextView)findViewById(R.id.tv_content);
		tvMessage.setText("加载中.....");
	    LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);  
        linearLayout.getBackground().setAlpha(210);  
	}
}
