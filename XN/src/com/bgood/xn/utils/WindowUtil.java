package com.bgood.xn.utils;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.ui.user.LoginActivity;
import com.bgood.xn.view.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class WindowUtil
{
	private Dialog dialog;
	private AlertDialog alertDialog;
	private ProgressDialog progressDialog;
	private static WindowUtil popoupWindowBottomUtil;

	public static WindowUtil getInstance()
	{
		if (popoupWindowBottomUtil == null)
			popoupWindowBottomUtil = new WindowUtil();
		return popoupWindowBottomUtil;
	}

	public static boolean isLongPressed(float lastX, float lastY,

	float thisX, float thisY, long lastDownTime, long thisEventTime, long longPressTime)
	{

		float offsetX = Math.abs(thisX - lastX);

		float offsetY = Math.abs(thisY - lastY);

		long intervalTime = thisEventTime - lastDownTime;

		if (offsetX <= 100 && offsetY <= 100 && intervalTime >= longPressTime)
		{

			return true;

		}

		return false;

	}

	public boolean isShowPopup()
	{
		if (dialog != null && dialog.isShowing())
			return true;
		return false;
	}

	public void popupIn(View layout, int ducation, final View parent, final int gravity)
	{
		dialog = null;
		dialog = new Dialog(layout.getContext(), R.style.dialog_no_thing);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		dialog.setContentView(layout);
		dialog.getWindow().setWindowAnimations(R.style.anim_flick_bottom);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.BOTTOM;
		dialog.show();
	}

	public boolean popupOut()
	{
		if (dialog != null && dialog.isShowing())
		{
			dialog.dismiss();
			return true;
		}

		return false;
	}

	public void dialogImageShow(Context context, String path)
	{
		final Dialog dialog = new Dialog(context, R.style.dialog_no_thing);
		final PhotoView imageView = new PhotoView(context);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		
		imageView.setBackgroundColor(Color.BLACK);
		imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		
	
		ImageLoader.getInstance().displayImage(path, imageView, options);
		
		
		imageView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		dialog.setContentView(imageView);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		dialog.show();
	}

	public void dialogViewPagerShow(Context context, List<ImageBean> list, int position)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.layout_viewpager_show, null);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		TextView textView = (TextView) view.findViewById(R.id.viewpager_index);
		textView.setText(position + 1 + "/" + list.size());
		final Dialog dialog = new Dialog(context, R.style.dialog_no_thing);
		FDSPagerAdapter pagerAdapter = new FDSPagerAdapter(context, list, textView);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setPageMargin(10);
		viewPager.setOnPageChangeListener(pagerAdapter);
		viewPager.setCurrentItem(position);
		viewPager.setBackgroundColor(Color.BLACK);
		viewPager.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		dialog.show();
	}

	public void alertDialogSure(Context context, String title, String message)
	{
		DismissAllDialog();
		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
			alertDialog = null;
		}
		alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setIcon(android.R.drawable.ic_dialog_alert).setNegativeButton("确定", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		}).create();
		alertDialog.show();
	}

	public void DismissAllDialog()
	{
		if (progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
			progressDialog = null;
		}
		if (alertDialog != null && alertDialog.isShowing())
		{
			alertDialog.dismiss();
			alertDialog = null;
		}
	}

	public void progressDialogShow(Context context, String message)
	{
		DismissAllDialog();
		if (progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage(message);
		progressDialog.show();
	}
}
