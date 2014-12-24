package com.bgood.xn.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class LoadingProgress {

//	private static LoadingProgress intance = null;
//
//	private CustomLoadingDialog mDialog;
//
//	private int mShownCount;
//
//	public static synchronized LoadingProgress getInstance() {
//		if(null == intance)
//		{
//			intance = new LoadingProgress();
//		}
//		return intance;
//	}
//
//	private LoadingProgress() {
//	}
//
//	public void show(Context context) {
//		show(context, "加载中...");
//	}
//
//	public void show(Context context, String message) {
//		if (haveShown()) {
//			mShownCount++;
//			return;
//		}
//
//		createLoadingDialog(context, message);
//
//		if (!mDialog.isShowing()) {
//			mShownCount++;
//			mDialog.show();
//		}
//	}
//
//	private boolean haveShown() {
//		return mShownCount != 0 ? true : false;
//	}
//
//	private void createLoadingDialog(Context context, String message) {
//		mDialog = new CustomLoadingDialog(context);
//		mDialog.setCancelable(true);
//		mDialog.setCanceledOnTouchOutside(false);
//		mDialog.setMessage(message);
//	}
//
//	public void dismiss() {
//		if (mDialog != null && mDialog.isShowing()) {
//			mShownCount = 0;
//			mDialog.cancel();
//			mDialog = null;
//		}
//	}
	
	
	private ProgressDialog mDialog;

	private static LoadingProgress intance = new LoadingProgress();

	private int mShownCount;

	public static LoadingProgress getInstance() {
		return intance;
	}

	private LoadingProgress() {
	}

	public void show(Context context) {
		show(context, "正在加载中...");
	}

	public void show(Context context, String message) {
		if (haveShown()) {
			mShownCount++;
			return;
		}

		createLoadingDialog(context, message);

		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	private boolean haveShown() {
		return mShownCount != 0 ? true : false;
	}

	private void createLoadingDialog(Context context, String message) {
		mDialog = new ProgressDialog(context);
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setIndeterminate(false);
		mDialog.setInverseBackgroundForced(false);
		mDialog.setMessage(message);
	}

	public void dismiss() {
		if (mDialog != null && mDialog.isShowing()) {
			System.out.println("关闭ProgressDialog");
			mShownCount = 0;
			mDialog.cancel();
		}
	}

}
