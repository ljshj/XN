package com.bgood.xn.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @todo:所有Fragment基类
 * @date:2014-10-22 下午1:39:05
 * @author:hg_liuzl@163.com
 */
public class BaseFragment extends Fragment {
	
	public static final int PAGE_SIZE_ADD = 10;
	
	public Activity mActivity = null;
	public LayoutInflater inflater = null;
	public View layout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		inflater = LayoutInflater.from(mActivity);
	}
	
	public void finish() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		manager.popBackStack();
	}
}
