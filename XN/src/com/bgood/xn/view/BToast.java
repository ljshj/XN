package com.bgood.xn.view;
import android.content.Context;
import android.widget.Toast;

/**
 * @todo 自定义Toast类
 */
public class BToast{
	public static void show(Context context,String text){
		 Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void show(Context context,int text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void show(Context context,String text,int duration){
		Toast.makeText(context, text, duration).show();
	}
	
	public static void show(Context context,int text,int duration){
		Toast.makeText(context, text, duration).show();
	}
	
	/**
	 * 定义Toast位置
	 */
	public static void showLongLocation(Context context,String text,int gravity){
		Toast tr = Toast.makeText(context, text, Toast.LENGTH_LONG);
		tr.setGravity(gravity, 0, 0);
		tr.show();
	}
	
	
	
	public static void showLongLocation(Context context,int text,int gravity){
		Toast tr = Toast.makeText(context, text, Toast.LENGTH_LONG);
		tr.setGravity(gravity, 0, 0);
		tr.show();		
		
	}
	
	public static void showShortLocation(Context context,String text,int gravity){
		Toast tr = Toast.makeText(context, text, Toast.LENGTH_LONG);
		tr.setGravity(gravity, 0, 0);
		tr.show();
	}
	
	public static void showShortLocation(Context context,int text,int gravity){
		Toast tr = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		tr.setGravity(gravity, 0, 0);
		tr.show();
	}

}
