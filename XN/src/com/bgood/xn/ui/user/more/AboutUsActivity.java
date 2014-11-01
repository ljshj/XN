package com.bgood.xn.ui.user.more;
import android.os.Bundle;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:关于我们
 * @date:2014-11-1 下午2:24:21
 * @author:hg_liuzl@163.com
 */
public class AboutUsActivity extends BaseActivity  {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_layout_about);
		initView();
	}
	public void initView() {
		(new TitleBar(mActivity)).initTitleBar("关于我们");
		TextView tvVersion = (TextView)findViewById(R.id.tv_version);
		tvVersion.setText(getString(R.string.app_version, ConfigUtil.getVersionName(mActivity)));
	}
	
}
