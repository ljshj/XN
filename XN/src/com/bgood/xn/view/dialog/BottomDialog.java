package com.bgood.xn.view.dialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

import com.bgood.xn.R;

/**
 * 
 * @todo:从底部弹出的Dialog
 * @date:2014-11-3 下午5:23:02
 * @author:hg_liuzl@163.com
 */
public class BottomDialog extends AlertDialog {
	
	public BottomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public BottomDialog(Context context) {
		super(context);
	}

	private boolean isAnimation = true;
    private int animationStyle = R.style.bottom_dialog_animation;
    

    public BottomDialog setAnimation(boolean isAnimation){
    	this.isAnimation = isAnimation;
    	return this;
    }
    
    public void setAnimationStyle(int animationStyle){
    	this.animationStyle = animationStyle;
    }
    
    public void createDialog(View view){
        setContentView(view);
        Window window = getWindow(); //得到对话框
        if(isAnimation) window.setWindowAnimations(animationStyle); 
        
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width =  LayoutParams.MATCH_PARENT; 
        params.height = LayoutParams.WRAP_CONTENT;
        params.dimAmount=0.5f;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
    }
    
    public void createDialog(int layoutResId){
        setContentView(layoutResId);
        Window window = getWindow(); //得到对话框
        if(isAnimation) window.setWindowAnimations(animationStyle); 
        
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width =  LayoutParams.MATCH_PARENT; 
        params.height = LayoutParams.WRAP_CONTENT;
        params.dimAmount=0.5f;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
    }
    
    public void showDialog(View view){
        setContentView(view);
        Window window = getWindow(); //得到对话框
        if(isAnimation) window.setWindowAnimations(animationStyle); 
        
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width =  LayoutParams.MATCH_PARENT; 
            params.height = LayoutParams.WRAP_CONTENT;
            params.dimAmount=0.5f;
            window.setAttributes(params);
            window.setGravity(Gravity.BOTTOM);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
            show();
    }
}





