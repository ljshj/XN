package com.bgood.xn.ui.xuanneng.joke;


import java.util.ArrayList;
import java.util.List;
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
import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.JokeBean.JokeActionType;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.weiqiang.WeiqiangDetailActivity;
import com.bgood.xn.ui.xuanneng.XuannengFragment;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;


/**
 * 
 * @todo:炫能排行页
 * @date:2014-11-22 下午2:38:22
 * @author:hg_liuzl@163.com
 */
public class JokeRankActivity extends BaseActivity implements OnClickListener, OnItemClickListener,TaskListenerWithState,IXListViewListener
{
	
	private ViewPager mTabPager;//页卡内容
	
	public static final int CHOOSE_DAY = 1;	
	public static final int CHOOSE_WEEK = CHOOSE_DAY + 1; 
	public static final int CHOOSE_MONTH = CHOOSE_WEEK + 1; 
	public static final int CHOOSE_YEAR = CHOOSE_MONTH + 1; 
	private int REQUEST_FLAG = CHOOSE_DAY;

    private RadioGroup radio_group;
	private RadioButton radio_01,radio_02,radio_03,radio_04;
	
	private JokeAdapter adapterDay,adapterWeek,adapterMonth,adapterYear;
	
	
	private XListView mXLDay,mXLWeek,mXLMonth,mXLYear;   // 会员
	
	ArrayList<JokeBean> listDay = new ArrayList<JokeBean>();
	ArrayList<JokeBean> listWeek = new ArrayList<JokeBean>();
	ArrayList<JokeBean> listMonth = new ArrayList<JokeBean>();
	ArrayList<JokeBean> listYear = new ArrayList<JokeBean>();

	
	private int m_start_day = 0;
	private int m_start_week = 0;
	private int m_start_month = 0;
	private int m_start_year = 0;
	
	private ShareUtils share = null;
	
    private JokeBean mActionJoke = null;
    private JokeActionType type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
		setContentView(R.layout.layout_xuanneng_rank);
		findView();
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
		radio_01 = (RadioButton) findViewById(R.id.radio_01);
		radio_02 = (RadioButton) findViewById(R.id.radio_02);
		radio_03 = (RadioButton) findViewById(R.id.radio_03);
		radio_04 = (RadioButton) findViewById(R.id.radio_04);
		
		radio_01.setOnClickListener(radio_click);
		radio_02.setOnClickListener(radio_click);
		radio_03.setOnClickListener(radio_click);
		radio_04.setOnClickListener(radio_click);
		
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

	OnClickListener radio_click = new OnClickListener() {

		@Override
		public void onClick(View view) {
				switch (view.getId()) {
				case R.id.radio_01:
					getData(CHOOSE_DAY);
					break;
				case R.id.radio_02:
					getData(CHOOSE_WEEK);
					break;
				case R.id.radio_03:
					getData(CHOOSE_MONTH);
					break;
				case R.id.radio_04:
					getData(CHOOSE_YEAR);
					break;
				default:
					break;
				
			}
		}
		
	};
	
	/**
	 * 设置选中
	 * @param flag
	 */
	private void getData(int flag){
		switch (flag) {
		case CHOOSE_DAY:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengFragment.XUANNENG_JOKE, CHOOSE_DAY, m_start_day, m_start_day+PAGE_SIZE_ADD);
			break;
		case CHOOSE_WEEK:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengFragment.XUANNENG_JOKE, CHOOSE_WEEK, m_start_week, m_start_week+PAGE_SIZE_ADD);
			break;
		case CHOOSE_MONTH:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengFragment.XUANNENG_JOKE, CHOOSE_MONTH, m_start_month, m_start_month+PAGE_SIZE_ADD);
			break;
		case CHOOSE_YEAR:
			XuannengRequest.getInstance().requestXuanRank(this, mActivity, XuannengFragment.XUANNENG_JOKE, CHOOSE_YEAR, m_start_year, m_start_year+PAGE_SIZE_ADD);
			break;
		default:
			break;
		}
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
				REQUEST_FLAG = CHOOSE_DAY;
				mTabPager.setCurrentItem(0);
				break;
			case R.id.radio_02:
				REQUEST_FLAG = CHOOSE_WEEK;
				mTabPager.setCurrentItem(1);
				break;
			case R.id.radio_03:
				REQUEST_FLAG = CHOOSE_MONTH;
				mTabPager.setCurrentItem(2);
				break;
			case R.id.radio_04:
				REQUEST_FLAG = CHOOSE_MONTH;
				mTabPager.setCurrentItem(3);
				break;
		
			default:
				break;
			}
			getData(REQUEST_FLAG);
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

    /**
     * 设置会员数据
     * @param list
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void setDataAdapter(XListView xListView,KBaseAdapter adapter,List<?> showList,List resultlist,int start_page)
    {
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
        	 start_page +=PAGE_SIZE_ADD;
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
			stopLoad(mXLDay);
			stopLoad(mXLWeek);
			stopLoad(mXLMonth);
			stopLoad(mXLYear);
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
				switch (REQUEST_FLAG) {
				case CHOOSE_DAY:
					setDataAdapter(mXLDay, adapterDay, listDay, response.jokes, m_start_day);
					break;
				case CHOOSE_WEEK:
					setDataAdapter(mXLWeek, adapterWeek, listWeek, response.jokes, m_start_week);
					break;
				case CHOOSE_MONTH:
					setDataAdapter(mXLMonth, adapterMonth, listMonth, response.jokes, m_start_month);
					break;
				case CHOOSE_YEAR:
					setDataAdapter(mXLYear, adapterYear, listYear, response.jokes, m_start_year);
					break;
				default:
					break;
			
				}
			}
		}
	}


	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		getData(REQUEST_FLAG);
	}
	@Override
	public void onClick(View v)
	{
		
		judgeLogin();
		JokeBean jBean = null;
		switch (v.getId())
		{
		case R.id.tv_zan_count:	//赞
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, jBean.jokeid);
			break;
		case R.id.tv_comment_count:	//评论
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.RESPONSE;
			createSendDialog();
			break;
		case R.id.tv_transpont_count:	//转发
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.TRANSPOND;
			createSendDialog();
			break;
		case R.id.tv_share_count:	//分享
			jBean = (JokeBean) v.getTag();
			share.doShare();
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
//					if(type == JokeActionType.TRANSPOND){
//						if(m_type == WEIQIANG_ALL){
//							mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
//							m_allFriendsAdapter.notifyDataSetChanged();
//						}else{
//							mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
//							m_followFriendsAdapter.notifyDataSetChanged();
//						}
//						WeiqiangRequest.getInstance().requestWeiqiangTranspond(WeiqiangFragment.this, mActivity, mActionWeiqiang.weiboid,content);
//					}else{
//						if(m_type == WEIQIANG_ALL){
//							mActionWeiqiang.comment_count = String.valueOf(Integer.valueOf(mActionWeiqiang.comment_count)+1);
//							m_allFriendsAdapter.notifyDataSetChanged();
//						}else{
//							mActionWeiqiang.comment_count = String.valueOf(Integer.valueOf(mActionWeiqiang.comment_count)+1);
//							m_followFriendsAdapter.notifyDataSetChanged();
//						}
//						WeiqiangRequest.getInstance().requestWeiqiangReply(WeiqiangFragment.this, mActivity, mActionWeiqiang.weiboid,content);
//					}
					
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
