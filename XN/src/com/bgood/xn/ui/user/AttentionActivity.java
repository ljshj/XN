package com.bgood.xn.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.FansAdapter;
import com.bgood.xn.adapter.FollowAdapter;
import com.bgood.xn.bean.AttentionBean;
import com.bgood.xn.bean.response.AttentionResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;


/**
 * 我的关注页面,与关注我的
 */
public class AttentionActivity extends BaseActivity implements IXListViewListener,TaskListenerWithState
{
	/**我关注的，与关注我的key*/
	public static final String KEY_ATTENTION = "key_attention";
    private XListView m_listLv = null;  // 列表数据
    private FollowAdapter followAdapter = null;
    private FansAdapter fansAdapter = null;
    private List<AttentionBean> m_list = new ArrayList<AttentionBean>();
    private int m_start = 0;
    private int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_attention);
        
        mType = getIntent().getIntExtra(KEY_ATTENTION, 0);
        
        (new TitleBar(mActivity)).initTitleBar(mType == 0 ?"我的关注":"我的粉丝");
        
        m_listLv = (XListView) findViewById(R.id.follow_xlv_list);
        m_listLv.setPullRefreshEnable(false);
        m_listLv.setPullLoadEnable(false);
        UserCenterRequest.getInstance().requestAttention(this, mActivity, String.valueOf(mType));
    }
    

    /**
     * 设置适配器数据方法
     */
    private void setAdapter()
    {
    	if(0 == mType){
            followAdapter = new FollowAdapter(m_list,mActivity);
            m_listLv.setAdapter(followAdapter);
    	}else{
            fansAdapter = new FansAdapter(m_list,mActivity);
            m_listLv.setAdapter(fansAdapter);
    	}

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
				AttentionResponse response = JSON.parseObject(strJson, AttentionResponse.class);
				if (response.items.size() < PAGE_SIZE_ADD) {
					m_listLv.setPullLoadEnable(false);
					Toast.makeText(mActivity, "加载完毕！",Toast.LENGTH_LONG).show();
				} else {
					m_start = m_start + PAGE_SIZE_ADD;
				}
				m_list.addAll(response.items);
				setAdapter();
				}
			}
	}
}
