package com.bgood.xn.ui.xuanneng.joke;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.JokeBean.JokeActionType;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.ui.xuanneng.XuannengActivity;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;


/**
 * 
 * @todo:炫能排行页
 * @date:2014-11-22 下午2:38:22
 * @author:hg_liuzl@163.com
 */
public class JokeRankActivity extends BaseShowDataActivity implements OnClickListener, OnItemClickListener,TaskListenerWithState,IXListViewListener
{
	
	private ViewPager mTabPager;//页卡内容
	
	public static final int CHOOSE_DAY = 1;	
	public static final int CHOOSE_WEEK = CHOOSE_DAY + 1; 
	public static final int CHOOSE_MONTH = CHOOSE_WEEK + 1; 
	public static final int CHOOSE_YEAR = CHOOSE_MONTH + 1; 
	private int REQUEST_FLAG = CHOOSE_DAY;

    private RadioGroup radio_group;
	
	private JokeAdapter adapterDay,adapterWeek,adapterMonth,adapterYear;
	
	
	private XListView mXLDay,mXLWeek,mXLMonth,mXLYear;   // 会员
	
	private ArrayList<JokeBean> listDay = new ArrayList<JokeBean>();
	private ArrayList<JokeBean> listWeek = new ArrayList<JokeBean>();
	private ArrayList<JokeBean> listMonth = new ArrayList<JokeBean>();
	private ArrayList<JokeBean> listYear = new ArrayList<JokeBean>();

	
	private int m_start_day = 0;
	private int m_start_week = 0;
	private int m_start_month = 0;
	private int m_start_year = 0;
	
	private ShareUtils share = null;
	
    private JokeBean mActionJoke = null;
    private JokeActionType type;
    
    private boolean isRefreshAction = true;	//是否是刷新操作
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
		setContentView(R.layout.layout_xuanneng_rank);
		findView();
		getData(CHOOSE_DAY, true);
	}
	/**
	 * 控件初始化方法
	 */
	@SuppressLint("InflateParams")
	private void findView()
	{
		
		mTabPager = (ViewPager) findViewById(R.id.pager_xuanneng_rank);
		//设置ViewPager的页面翻滚监听
		mTabPager.setOnPageChangeListener(new myOnPageChangeListener());
		
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_group.setOnCheckedChangeListener(mOnCheckedChangeListener);

		View view1 = inflater.inflate(R.layout.home_layout_result_listview, null);
		mXLDay = (XListView) view1.findViewById(R.id.xListView);
		mXLDay.setPullLoadEnable(true);
		mXLDay.setPullRefreshEnable(true);
		mXLDay.setXListViewListener(this);
		mXLDay.setOnItemClickListener(this);
		adapterDay = new JokeAdapter(listDay, mActivity,this);
		mXLDay.setAdapter(adapterDay);
		
		View view2 = inflater.inflate(R.layout.home_layout_result_listview, null);
		mXLWeek = (XListView) view2.findViewById(R.id.xListView);
		mXLWeek.setPullLoadEnable(true);
		mXLWeek.setPullRefreshEnable(true);
		mXLWeek.setXListViewListener(this);
		mXLWeek.setOnItemClickListener(this);
		adapterWeek = new JokeAdapter(listWeek, mActivity,this);
		mXLWeek.setAdapter(adapterWeek);
		
		View view3 = inflater.inflate(R.layout.home_layout_result_listview, null);
		mXLMonth = (XListView) view3.findViewById(R.id.xListView);
		mXLMonth.setPullLoadEnable(true);
		mXLMonth.setPullRefreshEnable(true);
		mXLMonth.setXListViewListener(this);
		mXLMonth.setOnItemClickListener(this);
		adapterMonth = new JokeAdapter(listMonth, mActivity,this);
		mXLMonth.setAdapter(adapterMonth);
		
		View view4 = inflater.inflate(R.layout.home_layout_result_listview, null);
		mXLYear = (XListView) view4.findViewById(R.id.xListView);
		mXLYear.setPullLoadEnable(true);
		mXLYear.setPullRefreshEnable(true);
		mXLYear.setXListViewListener(this);
		mXLYear.setOnItemClickListener(this);
		adapterYear = new JokeAdapter(listYear, mActivity,this);
		mXLYear.setAdapter(adapterYear);
		
		//将布局放入集合
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		
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
	 * @todo:TODO
	 * @date:2014-11-24 上午10:32:09
	 * @author:hg_liuzl@163.com
	 * @params:@param flag
	 * @params:@param loadData  是否需要加载数据
	 */
	private void getData(int flag,boolean isNeedLoadData){
		if(!isNeedLoadData){
			return;
		}
		switch (flag) {
		case CHOOSE_DAY:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengActivity.XUANNENG_JOKE, CHOOSE_DAY, m_start_day, m_start_day+PAGE_SIZE_ADD);
			break;
		case CHOOSE_WEEK:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengActivity.XUANNENG_JOKE, CHOOSE_WEEK, m_start_week, m_start_week+PAGE_SIZE_ADD);
			break;
		case CHOOSE_MONTH:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengActivity.XUANNENG_JOKE, CHOOSE_MONTH, m_start_month, m_start_month+PAGE_SIZE_ADD);
			break;
		case CHOOSE_YEAR:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengActivity.XUANNENG_JOKE, CHOOSE_YEAR, m_start_year, m_start_year+PAGE_SIZE_ADD);
			break;
		default:
			break;
		}
	}
    
	/**
	 * 
	 */
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int radioButtonId = group.getCheckedRadioButtonId();
			boolean isNeedLoadData = true;	//如果没有数据，就需要加载数据，如果有数据就不需要加载数据
			switch (radioButtonId) {
			case R.id.radio_01:
				REQUEST_FLAG = CHOOSE_DAY;
				mTabPager.setCurrentItem(0);
				isNeedLoadData = listDay.size()>0? false : true;
				break;
			case R.id.radio_02:
				REQUEST_FLAG = CHOOSE_WEEK;
				mTabPager.setCurrentItem(1);
				isNeedLoadData = listWeek.size()>0? false : true;
				break;
			case R.id.radio_03:
				REQUEST_FLAG = CHOOSE_MONTH;
				mTabPager.setCurrentItem(2);
				isNeedLoadData = listMonth.size()>0? false : true;
				break;
			case R.id.radio_04:
				REQUEST_FLAG = CHOOSE_YEAR;
				mTabPager.setCurrentItem(3);
				isNeedLoadData = listYear.size()>0? false : true;
				break;
		
			default:
				break;
			}
			getData(REQUEST_FLAG,isNeedLoadData);
		}
	};
	
	public class myOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int i) {
			// TODO Auto-generated method stub
			RadioButton child = (RadioButton) radio_group.getChildAt(i);
			child.setChecked(true);
		}
	}


    


    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
    {
	 	final JokeBean joke = (JokeBean) adapter.getAdapter().getItem(position);
        Intent intent = new Intent(mActivity, JokeDetailActivity.class);
        intent.putExtra(JokeDetailActivity.BEAN_JOKE_KEY, joke);
        startActivity(intent);
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				if(bNetWork.getMessageType()==870002){}
				JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
				switch (REQUEST_FLAG) {
				case CHOOSE_DAY:
					m_start_day+=PAGE_SIZE_ADD;
					setDataAdapter(mXLDay, adapterDay, listDay, response.jokes,isRefreshAction);
					break;
				case CHOOSE_WEEK:
					m_start_week+=PAGE_SIZE_ADD;
					setDataAdapter(mXLWeek, adapterWeek, listWeek, response.jokes,isRefreshAction);
					break;
				case CHOOSE_MONTH:
					m_start_month+=PAGE_SIZE_ADD;
					setDataAdapter(mXLMonth, adapterMonth, listMonth, response.jokes,isRefreshAction);
					break;
				case CHOOSE_YEAR:
					m_start_year+=PAGE_SIZE_ADD;
					setDataAdapter(mXLYear, adapterYear, listYear, response.jokes,isRefreshAction);
					break;
				default:
					break;
			
				}
			}
		}
	}


	@Override
	public void onRefresh() {
		isRefreshAction = true;
		doRefreshAction(REQUEST_FLAG);
	}
	
	private void doRefreshAction(int checkType) {
		
		switch (checkType) {
		case CHOOSE_DAY:
			m_start_day = 0;
			break;
			
		case CHOOSE_WEEK:
			m_start_week = 0;
			break;
			
		case CHOOSE_MONTH:
			m_start_month = 0;
			break;
			
		case CHOOSE_YEAR:
			m_start_year = 0;
			break;

		default:
			break;
		}
		
		getData(checkType,true);
	}
	

	@Override
	public void onLoadMore() {
		isRefreshAction = false;
		getData(REQUEST_FLAG,true);
	}
	@Override
	public void onClick(View v)
	{
		
		judgeLogin();
		JokeBean jBean = null;
		switch (v.getId())
		{
		case R.id.av_zan:	//赞
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, jBean.jokeid);
			break;
		case R.id.av_reply:	//评论
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.RESPONSE;
			createSendDialog();
			break;
		case R.id.av_transpont:	//转发
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.TRANSPOND;
			createSendDialog();
			break;
		case R.id.av_share:	//分享
			jBean = (JokeBean) v.getTag();
			share.setShareContent(jBean.content, jBean.imgs.size() > 0 ? jBean.imgs.get(0).img:null);
			XuannengRequest.getInstance().requestXuanShare(this, this, jBean.jokeid);
			break;
		}
	}
	
	
	/**
	 * 
	 * @todo 时间选择器
	 * @author lzlong@zwmob.com
	 * @time 2014-3-26 下午2:09:30
	 */
	private void createSendDialog() {

		int screenWidth = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth()); // 屏幕宽
		int screenHeight = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight()*0.3); // 屏幕高

		final BGDialog dialog = new BGDialog(mActivity,R.style.dialog_no_thing,screenWidth, screenHeight);

		View vSend = inflater.inflate(R.layout.dialog_send, null);

		vSend.requestFocus();

		final EditText etcontent = (EditText) vSend.findViewById(R.id.et_content);
		vSend.findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String content = etcontent.getText().toString();
				if(TextUtils.isEmpty(content))
				{
					return;
				}else{
					etcontent.setText("");
					if(type == JokeActionType.TRANSPOND){
						mActionJoke.forward_count = String.valueOf(Integer.valueOf(mActionJoke.forward_count)+1);
						adapterDay.notifyDataSetChanged();
						WeiqiangRequest.getInstance().requestWeiqiangTranspond(JokeRankActivity.this, mActivity, mActionJoke.jokeid,content);
					}else{
						mActionJoke.comment_count = String.valueOf(Integer.valueOf(mActionJoke.comment_count)+1);
						adapterDay.notifyDataSetChanged();
					    WeiqiangRequest.getInstance().requestWeiqiangReply(JokeRankActivity.this, mActivity, mActionJoke.jokeid,content);
					}
				}
			}
		});
		
		//弹出键盘
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
			}
		},100);
		
		dialog.setCancelable(true);
		dialog.showDialog(vSend);
	}
}
