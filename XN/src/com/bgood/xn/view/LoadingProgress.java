package com.bgood.xn.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class LoadingProgress {

	private static LoadingProgress intance = null;

	private ProgressDialog mDialog;

	private int mShownCount;

	public static synchronized LoadingProgress getInstance() {
		if(null == intance)
		{
			intance = new LoadingProgress();
		}
		return intance;
	}

	private LoadingProgress() {
	}

	public void show(Context context) {
		show(context, "加载中...");
	}

	public void show(Context context, String message) {
		if (haveShown()) {
			mShownCount++;
			return;
		}

		createLoadingDialog(context, message);

		if (!mDialog.isShowing()) {
			mShownCount++;
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
			mShownCount = 0;
			mDialog.cancel();
			mDialog = null;
		}
	}

}
