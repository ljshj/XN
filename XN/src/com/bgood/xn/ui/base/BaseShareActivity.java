package com.bgood.xn.ui.base;

import android.content.Intent;
import android.os.Bundle;

import com.bgood.xn.utils.ShareUtils;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * @todo:TODO
 * @date:2015-3-23 上午10:42:57
 * @author:hg_liuzl@163.com
 */
public class BaseShareActivity extends BaseActivity {
	
	public ShareUtils share = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
	}
	
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = share.mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
}
