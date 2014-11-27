package com.bgood.xn.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.AttentionAdapter;
import com.bgood.xn.bean.AttentionBean;
import com.bgood.xn.bean.response.AttentionResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;


/**
 * 我的关注页面,与关注我的
 */
public class AttentionActivity extends BaseActivity implements IXListViewListener,TaskListenerWithState,OnClickListener
{
	/**我关注的，与关注我的key*/
	public static final String KEY_ATTENTION = "key_attention";
    private XListView m_listLv = null;  // 列表数据
    private AttentionAdapter attentionAdapter = null;
    private List<AttentionBean> m_list = new ArrayList<AttentionBean>();
    private int m_start = 0;
    private int mType = 0;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_attention);
        
        mType = getIntent().getIntExtra(KEY_ATTENTION, 0);
        
        (new TitleBar(mActivity)).initTitleBar(mType == AttentionBean.ATTENTION ?"我的关注":"我的粉丝");
        
        m_listLv = (XListView) findViewById(R.id.follow_xlv_list);
        m_listLv.setPullRefreshEnable(false);
        m_listLv.setPullLoadEnable(false);
        m_listLv.setFooterDividersEnabled(false);
        UserCenterRequest.getInstance().requestAttentionOfMe(this, mActivity, String.valueOf(mType),String.valueOf(m_start),String.valueOf(m_start+PAGE_SIZE_ADD));
    }
    

    /**
     * 设置适配器数据方法
     */
    private void setAdapter()
    {
		attentionAdapter = new AttentionAdapter(m_list,mActivity,this);
        m_listLv.setAdapter(attentionAdapter);
    }

    
    /**
     * 加载完成之后进行时间保存等方法
     */
    private void stopLoad() {
        m_listLv.stopRefresh();
        m_listLv.stopLoadMore();
        m_listLv.setRefreshTime(ToolUtils.getNowTime());
    }
    
    @Override
    public void onRefresh()
    {
    	isRefresh = true;
    	m_start = 0;
    	UserCenterRequest.getInstance().requestAttentionOfMe(this, mActivity, String.valueOf(mType),String.valueOf(m_start),String.valueOf(m_start+PAGE_SIZE_ADD));

    }

    @Override
    public void onLoadMore()
    {
        
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			stopLoad();
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
					switch (bNetWork.getMessageType()) {
					case 820005:
						AttentionResponse response = JSON.parseObject(strJson, AttentionResponse.class);
						if (response.items.size() < PAGE_SIZE_ADD) {
							m_listLv.setPullLoadEnable(false);
							BToast.show(mActivity, "数据加载完毕");
						} else {
							m_start = m_start + PAGE_SIZE_ADD;
							m_listLv.setPullLoadEnable(true);
						}
						if(isRefresh){
							isRefresh = false;
							m_list.clear();
						}
						m_list.addAll(response.items);
						setAdapter();
						break;

					default:
						break;
					}
				}
			}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_attention_type:
			AttentionBean bean = (AttentionBean) v.getTag();
			
			if(bean.searchtype == 0){
				UserCenterRequest.getInstance().requestAttention(AttentionActivity.this, mActivity,String.valueOf(bean.userid),BGApp.mUserId,String.valueOf(1));
				m_list.remove(bean);
			}else{
				UserCenterRequest.getInstance().requestAttention(AttentionActivity.this, mActivity,String.valueOf(bean.userid),BGApp.mUserId,String.valueOf(bean.guanzhutype==1?0:1));
				bean.guanzhutype = bean.guanzhutype ==1?0:1;
			}
			attentionAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
	
}
