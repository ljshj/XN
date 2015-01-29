package com.bgood.xn.ui.xuanneng.joke;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.adapter.JokeUnVerifyAdapter;
import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:我的幽默秀
 * @date:2014-12-1 下午5:15:28
 * @author:hg_liuzl@163.com
 */
public class JokeMeActivity extends BaseActivity implements OnClickListener, OnItemClickListener,TaskListenerWithState,IXListViewListener{

	
	private ViewPager mTabPager;//页卡内容
	
	private int REQUEST_FLAG = JokeBean.JOKE_VERIFY;

    private RadioGroup radio_group;
	
	private JokeAdapter m_VerifyAdapter = null;
	private JokeUnVerifyAdapter m_unVerifyAdapter = null;
	
	private XListView m_VerifyXLv = null;   // 会员
	private XListView m_UnVerifyXLv = null;    // 微墙
	
	private ArrayList<JokeBean> m_VerifyList = new ArrayList<JokeBean>();
	private ArrayList<JokeBean> m_UnVerifyList = new ArrayList<JokeBean>();

	private boolean isReFresh = true;
	private int m_VerifyStart = 0;
	private int m_UnVerifyStart = 0;
	
	private String userid = "";	
	
	private String mRefreshTime = ""; //刷新时间
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_joke_me);
		userid = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
		(new TitleBar(mActivity)).initTitleBar("我的炫能");
		initView();
		doRequest();
	}
	
	private void initView() {
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_group.setOnCheckedChangeListener(mOnCheckedChangeListener);
		
		mTabPager = (ViewPager) findViewById(R.id.vp_joke_type_select);
		//设置ViewPager的页面翻滚监听
		mTabPager.setOnPageChangeListener(new myOnPageChangeListener());
		
		View view1 = inflater.inflate(R.layout.listview_space_bar, null);
		m_VerifyXLv = (XListView) view1.findViewById(R.id.xlv_sapce);
		m_VerifyXLv.setPullLoadEnable(true);
		m_VerifyXLv.setPullRefreshEnable(true);
		m_VerifyXLv.setXListViewListener(this);
		m_VerifyAdapter = new JokeAdapter(m_VerifyList, mActivity,this);
		m_VerifyXLv.setAdapter(m_VerifyAdapter);
		
		View view2 = inflater.inflate(R.layout.listview_space_bar, null);
		m_UnVerifyXLv = (XListView) view2.findViewById(R.id.xlv_sapce);
		m_UnVerifyXLv.setPullLoadEnable(true);
		m_UnVerifyXLv.setPullRefreshEnable(true);
		m_UnVerifyXLv.setXListViewListener(this);
		m_UnVerifyXLv.setDividerHeight(1);
		m_unVerifyAdapter = new JokeUnVerifyAdapter(m_UnVerifyList, mActivity);
		m_UnVerifyXLv.setAdapter(m_unVerifyAdapter);
		
		//将布局放入集合
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		
		//放入adapter
		PagerAdapter adapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(adapter);

	}

	/**
	 * 
	 */
	OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int radioButtonId = group.getCheckedRadioButtonId();
			switch (radioButtonId) {
			case R.id.radio_01:
				REQUEST_FLAG = JokeBean.JOKE_VERIFY;
				mTabPager.setCurrentItem(0);
				break;
			case R.id.radio_02:
				REQUEST_FLAG = JokeBean.JOKE_UN_VERIFY;
				mTabPager.setCurrentItem(1);
				break;
			default:
				break;
			}
			doRequest();
		}
	};
	
	public class myOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int i) {
			RadioButton child = (RadioButton) radio_group.getChildAt(i);
			child.setChecked(true);
		}
	}

	@Override
	public void onRefresh() {
		isReFresh = true;
		if(REQUEST_FLAG == JokeBean.JOKE_VERIFY){
			m_VerifyStart = 0;
		}else{
			m_UnVerifyStart = 0;
		}
		doRequest();
	}

	@Override
	public void onLoadMore() {
		doRequest();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
		final JokeBean joke = (JokeBean) adapter.getAdapter().getItem(position);
        Intent intent = new Intent(mActivity, REQUEST_FLAG == JokeBean.JOKE_VERIFY ?JokeDetailActivity.class:JokePublishActivity.class);
        intent.putExtra(JokeDetailActivity.BEAN_JOKE_KEY, joke);
        startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		
	}

	private void doRequest() {
		if(REQUEST_FLAG == JokeBean.JOKE_VERIFY){
			if(m_VerifyList.size() == 0){
				XuannengRequest.getInstance().requestXuanPublishList(this, mActivity, userid, JokeBean.JOKE_VERIFY, m_VerifyStart, m_VerifyStart+PAGE_SIZE_ADD);
			}
		}else{
			if(m_UnVerifyList.size() == 0){
			XuannengRequest.getInstance().requestXuanPublishList(this, mActivity, userid, JokeBean.JOKE_UN_VERIFY, m_UnVerifyStart, m_UnVerifyStart+PAGE_SIZE_ADD);
			}
		}
	}
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		stopLoad(m_UnVerifyXLv);
		stopLoad(m_VerifyXLv);
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch(bNetWork.getMessageType()){
				case 870002:	//获取微墙详细内容
					break;
				case 870003:
					break;
				case 870004:
					break;
				case 870005:	//
					break;
				case 870006:
					break;
				case 870007:
					break;
				case 870008:	//
					JokeResponse response  = JSON.parseObject(strJson, JokeResponse.class);
					if(REQUEST_FLAG == JokeBean.JOKE_VERIFY){
						setDataAdapter(m_VerifyXLv, m_VerifyAdapter, m_VerifyList, response.jokes);
					}else{
						setDataAdapter(m_UnVerifyXLv, m_unVerifyAdapter, m_UnVerifyList, response.jokes);
					}
					break;
				}}}
	}
	
	   /**
     * 设置会员数据
     * @param list
     */
	@SuppressWarnings("unchecked")
	private void setDataAdapter(XListView xListView,KBaseAdapter adapter,List<?> showList,@SuppressWarnings("rawtypes") List resultlist)
    {
    	mRefreshTime = ToolUtils.getNowTime();
    	xListView.setRefreshTime(mRefreshTime);
    	if(null == resultlist || resultlist.size() ==0)
    	{
    		xListView.setPullLoadEnable(false);
            BToast.show(mActivity, "数据加载完毕");
    		return;
    	}
    	
    	 if (resultlist.size() < PAGE_SIZE_ADD)
         {
    		 xListView.setPullLoadEnable(false);
             BToast.show(mActivity, "数据加载完毕");
         }else
         {
        	 xListView.setPullLoadEnable(true);
         }
    	 
    	 if(isReFresh){
    		 showList.clear();
    		 isReFresh = false;
    	 }
    	 
    	 showList.addAll(resultlist);
         adapter.notifyDataSetChanged();
    }
    
    /**
     * 加载完成之后进行时间保存等方法
     */
    @SuppressLint("SimpleDateFormat")
	private void stopLoad(XListView xListView) {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }
}
