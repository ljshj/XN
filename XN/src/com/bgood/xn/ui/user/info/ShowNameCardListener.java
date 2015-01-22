package com.bgood.xn.ui.user.info;

import com.bgood.xn.R;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.WeiQiangBean;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @todo:名片展示事件
 * @date:2015-1-22 上午11:38:30
 * @author:hg_liuzl@163.com
 */
public class ShowNameCardListener implements OnClickListener {
	private Object object;
	private Activity mActivity;
	public ShowNameCardListener(Object object,Activity activity){
		this.object = object;
		this.mActivity = activity;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_img:
		case R.id.tv_nick:
			if(object instanceof JokeBean){
				JokeBean mJokeBean = (JokeBean) object;
				NameCardActivity.lookNameCard(mActivity, mJokeBean.userid);
			}else if(object instanceof WeiQiangBean){
				WeiQiangBean mWeiqiang = (WeiQiangBean) object;
				NameCardActivity.lookNameCard(mActivity, String.valueOf(mWeiqiang.userid));
			}
			break;
		case R.id.tv_old_user:	//进入转发人的名片
			if(object instanceof JokeBean){
				JokeBean mJokeBean = (JokeBean) object;
				NameCardActivity.lookNameCard(mActivity, mJokeBean.fromuserid);
			}else if(object instanceof WeiQiangBean){
				WeiQiangBean mWeiqiang = (WeiQiangBean) object;
				NameCardActivity.lookNameCard(mActivity, String.valueOf(mWeiqiang.fromuserid));
			}
			break;
		default:
			break;
		}
	}
	
}
