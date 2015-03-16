package com.bgood.xn.ui.home;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import android.content.Intent;import android.graphics.Color;import android.os.Bundle;import android.os.Handler;import android.support.v4.view.PagerAdapter;import android.support.v4.view.ViewPager;import android.support.v4.view.ViewPager.OnPageChangeListener;import android.text.TextUtils;import android.view.View;import android.view.View.OnClickListener;import android.view.ViewGroup;import android.view.animation.Animation;import android.view.animation.Animation.AnimationListener;import android.view.animation.ScaleAnimation;import android.widget.PopupWindow;import android.widget.RelativeLayout;import android.widget.TextView;import com.bgood.xn.R;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.http.HttpRequestInfo;import com.bgood.xn.network.http.HttpResponseInfo;import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.request.HomeRequest;import com.bgood.xn.system.SystemConfig;import com.bgood.xn.ui.base.BaseActivity;import com.umeng.analytics.MobclickAgent;/** * @todo:首页界面 * @author:hg_liuzl@163.com */public class HomeActivity extends BaseActivity implements OnPageChangeListener,OnClickListener,TaskListenerWithState {		public static final String ACTION_TYPE = "action_type";	public int search_type = 0;	//0是我想，1，是我能		/**	 * 定时获取数据的时间	 */	public static final int POSTDELAYED_TIME = 6 * 1000;		/**存放解析热词的字符串*/	private String strJson;	private String[] ican =	{ "写代码", "做饭", "赚钱", "开车" };	private String[] iCanColor =	{ "#9a77fe","#ff65d9","#ff7f57","#2e92ff"};	private String[] ithink =	{ "打篮球", "写代码", "炒股", "讲笑话" };	private String[] iThinkColor =	{ "#2e92ff", "#ff7f57", "#ff65d9", "#9a77fe" };	private ViewPager vp_home_type_select;	private ViewGroup layout_home_ican;	private TextView m_oneICanTv = null;    private TextView m_twoICanTv = null;    private TextView m_threeICanTv = null;    private TextView m_fourICanTv = null;            private ViewGroup layout_home_ithink;	private TextView m_oneIThinkTv = null;	private TextView m_twoIThinkTv = null;	private TextView m_threeIThinkTv = null;	private TextView m_fourIThinkTv = null;	private TextView tv_home_type_left_select;	private TextView tv_home_type_right_select;	private View v_home_type_select_left_underline;	private View v_home_type_select_right_underline;	private TextView m_contentTv = null;	protected ViewGroup al_home_ithink_navigate;	protected ViewGroup al_home_ican_navigate;	private RelativeLayout m_speechRl = null;	private ViewGroup ll_home_search_check_type;	private PopupWindow popupWindowCheckSearchType;	private TextView home_tv_check_search_indecator;			@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.layout_home);		initViews();		strJson = pUitl.getHotWords();		parseResult();	}				/**	 * 初始化view	 */	protected void initViews()	{		home_tv_check_search_indecator = (TextView) findViewById(R.id.home_tv_check_search_indecator);		ll_home_search_check_type = (ViewGroup) findViewById(R.id.ll_home_search_check_type);		ll_home_search_check_type.setOnClickListener(this);		m_contentTv = (TextView) findViewById(R.id.home_tv_content);		m_speechRl = (RelativeLayout) findViewById(R.id.home_rl_speech);				tv_home_type_left_select = (TextView) findViewById(R.id.tv_home_type_left_select);		tv_home_type_right_select = (TextView) findViewById(R.id.tv_home_type_right_select);		v_home_type_select_left_underline = findViewById(R.id.v_home_type_select_left_underline);		v_home_type_select_right_underline = findViewById(R.id.v_home_type_select_right_underline);		vp_home_type_select = (ViewPager) findViewById(R.id.vp_home_type_select);		vp_home_type_select.setAdapter(new HomeTypePageAdapter());		vp_home_type_select.setOnPageChangeListener(this);		initIThink();		initICan();				m_contentTv.setOnClickListener(this);		m_speechRl.setOnClickListener(this);		tv_home_type_left_select.setOnClickListener(this);		tv_home_type_right_select.setOnClickListener(this);		m_oneIThinkTv.setOnClickListener(this);		m_twoIThinkTv.setOnClickListener(this);		m_threeIThinkTv.setOnClickListener(this);		m_fourIThinkTv.setOnClickListener(this);		m_oneICanTv.setOnClickListener(this);        m_twoICanTv.setOnClickListener(this);        m_threeICanTv.setOnClickListener(this);        m_fourICanTv.setOnClickListener(this);	}		Handler handler = new Handler();		/**`	 * 初始化我能界面	 */	private void initICan()	{		layout_home_ican = (ViewGroup) inflater.inflate(R.layout.layout_home_ican, null);		ViewGroup rl_home_ican_navigate = (ViewGroup) inflater.inflate(R.layout.home_item_ican, layout_home_ican, false);		layout_home_ican.addView(rl_home_ican_navigate);		al_home_ican_navigate = (ViewGroup) rl_home_ican_navigate.findViewById(R.id.al_home_can_navigate);		m_oneICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_one);        m_twoICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_two);        m_threeICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_three);        m_fourICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_four);                setHotWord(ican, al_home_ican_navigate);	}	/**	 * 初始化我想界面	 */	private void initIThink()	{		layout_home_ithink = (ViewGroup) inflater.inflate(R.layout.layout_home_ithink, null);		ViewGroup rl_home_ithink_navigate = (ViewGroup) inflater.inflate(R.layout.home_item_ithink, layout_home_ithink, false);		layout_home_ithink.addView(rl_home_ithink_navigate);		al_home_ithink_navigate = (ViewGroup) rl_home_ithink_navigate.findViewById(R.id.al_home_ithink_navigate);		m_oneIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_one);	    m_twoIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_two);	    m_threeIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_three);	    m_fourIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_four);	    setHotWord(ithink, al_home_ithink_navigate);	}		/**	 * 选择我想,我能时 上面的导航条变化	 * @param index	 */	private void checkType(int index)	{		switch (index)		{		case 0:			search_type = 0;			home_tv_check_search_indecator.setText("我想");			tv_home_type_left_select.setTextColor(getResources().getColor(R.color.home_type_text_select));			tv_home_type_right_select.setTextColor(getResources().getColor(R.color.text_color_normal));			v_home_type_select_left_underline.setVisibility(View.VISIBLE);			v_home_type_select_right_underline.setVisibility(View.INVISIBLE);			break;		case 1:			search_type = 1;			home_tv_check_search_indecator.setText("我能");			tv_home_type_left_select.setTextColor(getResources().getColor(R.color.text_color_normal));			tv_home_type_right_select.setTextColor(getResources().getColor(R.color.home_type_text_select));			v_home_type_select_left_underline.setVisibility(View.INVISIBLE);			v_home_type_select_right_underline.setVisibility(View.VISIBLE);			break;		}	}	/**	 * 我想,我能 viewpager的adapter	 * @author ChenGuoqing	 * 	 */	class HomeTypePageAdapter extends PagerAdapter	{		@Override		public int getCount()		{			return 2;		}		@Override		public boolean isViewFromObject(View arg0, Object arg1)		{			return arg0 == arg1;		}		@Override		public Object instantiateItem(ViewGroup container, int position)		{			View view = null;			switch (position)			{			case 0:				view = layout_home_ithink;				break;			case 1:				view = layout_home_ican;				break;			}			container.addView(view);			return view;		}		@Override		public void destroyItem(ViewGroup container, int position, Object object)		{			container.removeView((View) object);		}	}	@Override	public void onPageScrollStateChanged(int arg0)	{	}	@Override	public void onPageScrolled(int arg0, float arg1, int arg2)	{	}	@Override	public void onPageSelected(int arg0)	{		checkType(arg0);	}	/**	 * 搜索type改变	 * @param view	 */	private void checkSearchType(View view)	{		View home_iv_ican_indicator = view.findViewById(R.id.home_iv_ican_indicator);		View home_iv_ithink_indicator = view.findViewById(R.id.home_iv_ithink_indicator);		TextView home_tv_ican_indicator = (TextView) view.findViewById(R.id.home_tv_ican_indicator);		TextView home_tv_ithink_indicator = (TextView) view.findViewById(R.id.home_tv_ithink_indicator);		if (search_type == 0)		{			home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);			home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);			home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));			home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));		} else if (search_type == 1)		{			home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);			home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);			home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));			home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));		}	};	@Override	public void onClick(View v)	{		switch (v.getId())		{		case R.id.ll_home_search_check_type:			View view = inflater.inflate(R.layout.popup_home_check_seach, null);			checkSearchType(view);			view.findViewById(R.id.home_ll_ican).setOnClickListener(this);			view.findViewById(R.id.home_ll_ithink).setOnClickListener(this);			popupWindowCheckSearchType = null;			popupWindowCheckSearchType = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);			popupWindowCheckSearchType.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_home_check_popup));			popupWindowCheckSearchType.setOutsideTouchable(true);			popupWindowCheckSearchType.showAsDropDown(ll_home_search_check_type, 0, 0);			break;		case R.id.home_ll_ican:			search_type = 1;			home_tv_check_search_indecator.setText("我能");			vp_home_type_select.setCurrentItem(1);			popupWindowCheckSearchType.dismiss();			break;		case R.id.home_ll_ithink:			search_type = 0;			vp_home_type_select.setCurrentItem(0);			home_tv_check_search_indecator.setText("我想");			popupWindowCheckSearchType.dismiss();			break;					case R.id.ithink_tv_one:			MobclickAgent.onEvent(HomeActivity.this,"home_ithink_hot_click");            doSearch(ithink[0]);            break;				case R.id.ithink_tv_two:			MobclickAgent.onEvent(HomeActivity.this,"home_ithink_hot_click");			 doSearch(ithink[1]);            break;        		case R.id.ithink_tv_three:			MobclickAgent.onEvent(HomeActivity.this,"home_ithink_hot_click");			 doSearch(ithink[2]);            break;        		case R.id.ithink_tv_four:			MobclickAgent.onEvent(HomeActivity.this,"home_ithink_hot_click");			 doSearch(ithink[3]);            break;                      /**我能的顺序好像弄反了**/  		case R.id.home_ican_tv_one:			MobclickAgent.onEvent(HomeActivity.this,"home_ican_click");			 doSearch(ican[3]);            break;        case R.id.home_ican_tv_two:        	MobclickAgent.onEvent(HomeActivity.this,"home_ican_click");        	 doSearch(ican[2]);            break;                case R.id.home_ican_tv_three:        	MobclickAgent.onEvent(HomeActivity.this,"home_ican_click");        	 doSearch(ican[1]);            break;        case R.id.home_ican_tv_four:        	MobclickAgent.onEvent(HomeActivity.this,"home_ican_click");        	 doSearch(ican[0]);            break;		case R.id.home_tv_content:			Intent intent = new Intent(mActivity, SearchActivity.class);            intent.putExtra(ACTION_TYPE, search_type);            startActivity(intent);			break;		case R.id.home_rl_speech:			Intent inten2 = new Intent(mActivity, SpeechSearchActivity.class);			startActivity(inten2);			break;		case R.id.tv_home_type_left_select:			vp_home_type_select.setCurrentItem(0);			break;		case R.id.tv_home_type_right_select:			vp_home_type_select.setCurrentItem(1);			break;		}	}	/**	 * 	 * @todo:搜索结果关键词传递	 * @date:2015-1-22 上午10:25:35	 * @author:hg_liuzl@163.com	 * @params:@param strMsg	 */	private void doSearch(String strMsg){		 Intent intent = new Intent(mActivity, SearchResultActivity.class);		 intent.putExtra("msg", strMsg);         intent.putExtra(HomeActivity.ACTION_TYPE, search_type);         startActivity(intent);	}			@Override	public void onDestroy() {		super.onDestroy();		if (popupWindowCheckSearchType != null)		{		    popupWindowCheckSearchType.dismiss();		    popupWindowCheckSearchType = null;		}		pUitl.setHotWords(strJson);	}		 	 @Override	public void onResume() {		super.onResume();		MobclickAgent.onResume(this);		if(null != SystemConfig.BS_SERVER){			handler.postDelayed(run, POSTDELAYED_TIME);		}	}	 	 @Override	protected void onPause() {		super.onPause();		MobclickAgent.onPause(this);		handler.removeCallbacks(run);	}	 		private Runnable run = new Runnable(){		@Override		public void run() {			handler.postDelayed(this, POSTDELAYED_TIME);			HomeRequest.getInstance().requestHotWord(HomeActivity.this, mActivity);		//获取热词	保证业务服务器地址存在		}	};				/**		 * 		 * @todo:设置热词		 * @date:2014-10-22 下午4:39:05		 * @author:hg_liuzl@163.com		 * @params:@param strs		 * @params:@param vg		 */		private void setHotWord(String[] strs,ViewGroup vg) {			for (int i = 0; i < strs.length; i++)			{				String tag = strs[i];				TextView textView = (TextView) vg.getChildAt(i);				//textView.setText(name);				if(vg == al_home_ithink_navigate){					String color = iThinkColor[i];					textView.setTextColor(Color.parseColor(color));				}else{					String color = iCanColor[i];					textView.setTextColor(Color.parseColor(color));				}								textView.setTextSize(12);				zoomControl(textView,tag);			}		}				/**		 * 		 * @todo:控件缩放,先缩小，后放大 		 * @date:2014-10-22 下午4:23:05		 * @author:hg_liuzl@163.com		 * @params:		 */		private void zoomControl(final TextView tv,final String tag) {		    ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.1f, 1, 0.1f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f); 		    scaleAnimation.setDuration(2000); 		    scaleAnimation.setFillAfter(true);		    tv.startAnimation(scaleAnimation); 		    scaleAnimation.setAnimationListener(new AnimationListener() {								@Override				public void onAnimationStart(Animation a) {									}								@Override				public void onAnimationRepeat(Animation a) {									}								@Override				public void onAnimationEnd(Animation a) {					    ScaleAnimation scaleAnimation=new ScaleAnimation(0.1f, 1, 0.1f, 1,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f); 					    scaleAnimation.setDuration(2000); 					    scaleAnimation.setFillAfter(true);					    tv.startAnimation(scaleAnimation); 					    tv.setText(tag);				}			});		    		}		@Override		public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {			if(info.getState() == HttpTaskState.STATE_OK){				BaseNetWork bNetWork = info.getmBaseNetWork();				strJson = bNetWork.getStrJson();				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){				switch (bNetWork.getMessageType()) {				case 840007:					parseResult();					break;									default:					break;				}}			}		}				private void parseResult() {			try {				if(!TextUtils.isEmpty(strJson)){					JSONObject body = new JSONObject(strJson);					JSONArray iCans = body.getJSONArray("ican");					JSONArray iNeed = body.getJSONArray("ineed");										for(int i = 0;i<iCans.length();i++){						ican[i] = (String) iCans.get(i);						ithink[i] = (String) iNeed.get(i);					}				}				setHotWord(ithink,al_home_ithink_navigate);				setHotWord(ican,al_home_ican_navigate);			} catch (JSONException e) {				e.printStackTrace();			}		}		}