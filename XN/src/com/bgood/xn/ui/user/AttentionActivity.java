package com.bgood.xn.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.AttentionAdapter;
import com.bgood.xn.bean.AttentionBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.response.AttentionResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:我关注的，与关注我的
 * @date:2014-12-4 上午11:32:31
 * @author:hg_liuzl@163.com
 */
public class AttentionActivity extends BaseShowDataActivity implements TaskListenerWithState,OnClickListener{
	/**我关注的，与关注我的key*/
	public static final String KEY_ATTENTION = "key_attention";
	/**粉丝或者关注人的数量*/
    private XListView m_listLv = null;  // 列表数据
    private AttentionAdapter attentionAdapter = null;
    private List<AttentionBean> m_list = new ArrayList<AttentionBean>();
    private int m_start = 0;
    private int mType = 0;
    private int count;
    private TitleBar mTilteBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_attention);
        
        mType = getIntent().getIntExtra(KEY_ATTENTION, 0);
        
        count = TextUtils.isEmpty(BGApp.mUserBean.guanzhu)?0:Integer.valueOf(BGApp.mUserBean.guanzhu);
        
        mTilteBar = new TitleBar(mActivity);
        mTilteBar.initTitleBar(mType == AttentionBean.ATTENTION ?"我的关注":"我的粉丝");
        mTilteBar.backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					doBackLast();
			}
		});
        
        m_listLv = (XListView) findViewById(R.id.follow_xlv_list);
        m_listLv.setPullRefreshEnable(false);
        m_listLv.setPullLoadEnable(false);
        m_listLv.setFooterDividersEnabled(false);
        attentionAdapter = new AttentionAdapter(m_list, mActivity, this);
        m_listLv.setAdapter(attentionAdapter);
        UserCenterRequest.getInstance().requestAttentionOfMe(this, mActivity, String.valueOf(mType),String.valueOf(m_start),String.valueOf(m_start+PAGE_SIZE_ADD));
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
					switch (bNetWork.getMessageType()) {
					case 820005:
						if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
							AttentionResponse response = JSON.parseObject(strJson, AttentionResponse.class);
							setDataAdapter(m_listLv, attentionAdapter, m_list, response.items, isRefreshAction);
						}
						break;

					default:
						break;
					}
			}
	}


	@Override
	public void onClick(View v) {
		AttentionBean bean = (AttentionBean) v.getTag();
		switch (v.getId()) {
		case R.id.tv_attention_type:
			if(bean.searchtype == 0){
				UserCenterRequest.getInstance().requestAttention(AttentionActivity.this, mActivity,String.valueOf(bean.userid),BGApp.mUserId,String.valueOf(1));
				m_list.remove(bean);
				count--;
			}else{
				UserCenterRequest.getInstance().requestAttention(AttentionActivity.this, mActivity,String.valueOf(bean.userid),BGApp.mUserId,String.valueOf(bean.guanzhutype==1?1:0));
				bean.guanzhutype = bean.guanzhutype ==1?0:1;//0，普通，1可以相互关注
				count = bean.guanzhutype == 0 ?count++:count--;	//0表示添加了取消关注，非0表示关注
			}
			attentionAdapter.notifyDataSetChanged();
			break;
		case R.id.iv_user:
		case R.id.tv_username:
            NameCardActivity.lookNameCard(mActivity, String.valueOf(bean.userid));
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			doBackLast();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void doBackLast() {
		UserInfoBean userBean = BGApp.mUserBean;
		userBean.guanzhu = String.valueOf(count);
		BGApp.mUserBean = userBean;
		finish();
	}
}
