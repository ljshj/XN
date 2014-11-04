package com.bgood.xn.view.dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

import com.bgood.xn.R;

/**
 * 
 * @todo:从底部弹出的dialog
 * @date:2014-11-4 上午10:09:37
 * @author:hg_liuzl@163.com
 */
public class BottomDialog extends AlertDialog {
	private View vChild;
	protected Context mContext;
	protected Window window;

	public BottomDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	public BottomDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		
	}
	
	public void setvChild(View vChild) {
		this.vChild = vChild;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
   	    p.width = LayoutParams.MATCH_PARENT;
        p.height = LayoutParams.WRAP_CONTENT;
		window.setAttributes(p);
		window.setWindowAnimations(R.style.dialog_bottom);
		setContentView(vChild);
	}
}





