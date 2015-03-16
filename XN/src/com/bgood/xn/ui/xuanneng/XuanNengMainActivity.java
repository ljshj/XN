package com.bgood.xn.ui.xuanneng;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.location.MyLocationActivity;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.xuanneng.joke.JokeMainActivity;
import com.bgood.xn.ui.xuanneng.joke.JokePersonActivity;
import com.bgood.xn.ui.xuanneng.joke.JokeVerifyActivity;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author king 
 * 炫能主页列表，展示各种技能入口
 */

public class XuanNengMainActivity extends BaseActivity implements OnClickListener
{
	public static final int XUANNENG_JOKE = 0;
	private String mUserId = "";
	private boolean isSelf = true;
	private TitleBar mTitleBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_xuanneng);
        mUserId = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
        mTitleBar = new TitleBar(mActivity);
        if(TextUtils.isEmpty(mUserId)){	//这里为空，说明是tab栏目的炫能
        	mTitleBar.initTitleBar("炫能");
    		mTitleBar.setBackBtnVisible(View.GONE);
        }else{	//从别
        	isSelf = BGApp.mUserId.equals(mUserId)?true:false;
        	if(isSelf){
        		mTitleBar.initTitleBar("我的炫能");
        		mTitleBar.setBackBtnVisible(View.GONE);
        	}else{
        		mTitleBar.initTitleBar("TA的炫能");
        		mTitleBar.setBackBtnVisible(View.VISIBLE);
        	}
        }
        
        findViewById(R.id.ll_show_humor).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_show_humor:
			goToJoke();	
//			if(pUitl.hasInitJokeProtocol()){
//			}else{
//				pUitl.setInitJokeProtocol(true);
//				createProtocolDialog();
//			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @todo:进入幽默秀
	 * @date:2015-2-9 上午9:58:20
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void goToJoke() {
		Intent intent = null;
		if(isSelf && TextUtils.isEmpty(mUserId)){
			intent = new Intent(mActivity, JokeMainActivity.class);
			startActivity(intent);
		}else if(!isSelf){
			intent = new Intent(mActivity,JokePersonActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, mUserId);
			startActivity(intent);
		}
	}
	
	
	/**
	 * 
	 * @todo:使用协议
	 * @date:2015-1-24 上午10:47:52
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void createProtocolDialog() {
		@SuppressWarnings("deprecation")
		final BottomDialog dialog = new BottomDialog(mActivity,R.style.dialog_no_thing);
		View vProtocol = inflater.inflate(R.layout.dialog_protocol_layout, null);
		
		TextView tvRuleTitle = (TextView) vProtocol.findViewById(R.id.tv_protocol_title);
		tvRuleTitle.setText(getString(R.string.joke_rule_title));
		
		TextView tvRule = (TextView) vProtocol.findViewById(R.id.tv_protocol_content);
		tvRule.setText(getString(R.string.joke_rule));
		
		Button btnKnow = (Button) vProtocol.findViewById(R.id.btn_protocol);
		btnKnow.setText("我知道了");
		btnKnow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				goToJoke();
			}
		});
		
		Button btn =  (Button) vProtocol.findViewById(R.id.btn_dis_argee);
		btn.setVisibility(View.GONE);
		
		dialog.setvChild(vProtocol);
		dialog.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	
}
