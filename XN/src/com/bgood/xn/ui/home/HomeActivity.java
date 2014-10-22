//package com.bgood.xn.ui.home;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.AnimationUtils;
//import android.view.animation.LayoutAnimationController;
//import android.view.animation.ScaleAnimation;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.zhuozhong.bandgood.R;
//import com.zhuozhong.bandgood.activity.BaseActivity2;
//import com.zhuozhong.net2.BaseNetWork;
//import com.zhuozhong.net2.HttpRequestInfo;
//import com.zhuozhong.net2.HttpResponseInfo;
//import com.zhuozhong.net2.HttpRquestAsyncTask.TaskListenerWithState;
//import com.zhuozhong.net2.request.HomeRequest;
//import com.zhuozhong.net2.request.UserCenterRequest;
//import com.zhuozhong.util.LogUtils;
//import com.zhuozhong.zzframework.SystemConfig;
//
///**
// * 
// * @todo:首页
// * @date:2014-10-21 下午1:56:37
// * @author:hg_liuzl@163.com
// */
//public class HomeActivity extends BaseActivity2 implements OnPageChangeListener, OnClickListener,TaskListenerWithState
//{
//	/**
//	 * 定时获取数据的时间
//	 */
//	public static final int POSTDELAYED_TIME = 10 * 1000;		
//	
//	private ViewGroup rl_home_ican_navigate;
//	private String[] ican =
//	{ "我能能", "教小提琴", "教摄影", "教摄影" };
//	private String[] ithink =
//	{ "我想想", "教小提琴", "教摄影", "教摄影" };
//	private String[] ithinkColor =
//	{ "#2e92ff", "#ff7f57", "#ff65d9", "#9a77fe" };
//	private ViewPager vp_home_type_select;
//	private ViewGroup layout_home_ican;
//	private TextView m_oneICanTv = null;
//    private TextView m_twoICanTv = null;
//    private TextView m_threeICanTv = null;
//    private TextView m_fourICanTv = null;
//	private ViewGroup layout_home_ithink;
//	private TextView m_oneIThinkTv = null;
//	private TextView m_twoIThinkTv = null;
//	private TextView m_threeIThinkTv = null;
//	private TextView m_fourIThinkTv = null;
//	private TextView tv_home_type_left_select;
//	private TextView tv_home_type_right_select;
//	private View v_home_type_select_left_underline;
//	private View v_home_type_select_right_underline;
//	private int search_type = 0; // 0 为我想, 1为我能
//
//	private TextView m_contentTv = null;
//	protected ViewGroup al_home_ithink_navigate;
//	private RelativeLayout m_speechRl = null;
//	private ViewGroup ll_home_search_check_type;
//	private PopupWindow popupWindowCheckSearchType;
//	private TextView home_tv_check_search_indecator;
//	
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_home);
//		initViews();
//		UserCenterRequest.getInstance().requestUnLoginBSServer(this, this);
//	}
//
//	/**
//	 * 初始化view
//	 */
//	protected void initViews()
//	{
//		home_tv_check_search_indecator = (TextView) findViewById(R.id.home_tv_check_search_indecator);
//		ll_home_search_check_type = (ViewGroup) findViewById(R.id.ll_home_search_check_type);
//		ll_home_search_check_type.setOnClickListener(this);
//		m_contentTv = (TextView) findViewById(R.id.home_tv_content);
//		m_speechRl = (RelativeLayout) findViewById(R.id.home_rl_speech);
//		
//		tv_home_type_left_select = (TextView) findViewById(R.id.tv_home_type_left_select);
//		tv_home_type_right_select = (TextView) findViewById(R.id.tv_home_type_right_select);
//		v_home_type_select_left_underline = findViewById(R.id.v_home_type_select_left_underline);
//		v_home_type_select_right_underline = findViewById(R.id.v_home_type_select_right_underline);
//		vp_home_type_select = (ViewPager) findViewById(R.id.vp_home_type_select);
//		vp_home_type_select.setAdapter(new HomeTypePageAdapter());
//		vp_home_type_select.setOnPageChangeListener(this);
//		initIThink();
//		initICan();
//		
//		m_contentTv.setOnClickListener(this);
//		m_speechRl.setOnClickListener(this);
//		tv_home_type_left_select.setOnClickListener(this);
//		tv_home_type_right_select.setOnClickListener(this);
//		m_oneIThinkTv.setOnClickListener(this);
//		m_twoIThinkTv.setOnClickListener(this);
//		m_threeIThinkTv.setOnClickListener(this);
//		m_fourIThinkTv.setOnClickListener(this);
//		m_oneICanTv.setOnClickListener(this);
//        m_twoICanTv.setOnClickListener(this);
//        m_threeICanTv.setOnClickListener(this);
//        m_fourICanTv.setOnClickListener(this);
//        
//        handler.postDelayed(run, POSTDELAYED_TIME);
//	}
//	
//	Handler handler = new Handler();
//
//
//
//	/**`
//	 * 初始化我能界面
//	 */
//	private void initICan()
//	{
//		layout_home_ican = (ViewGroup) inflater.inflate(R.layout.layout_home_ican, null);
//		final ViewGroup rl_home_icon = (ViewGroup) layout_home_ican.findViewById(R.id.rl_home_ican);
//		rl_home_ican_navigate = (ViewGroup) inflater.inflate(R.layout.child_home_ican, rl_home_icon, false);
//		rl_home_icon.addView(rl_home_ican_navigate);
//		m_oneICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_one);
//        m_twoICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_two);
//        m_threeICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_three);
//        m_fourICanTv = (TextView) layout_home_ican.findViewById(R.id.home_ican_tv_four);
//		Animation animation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.alpha_display_enter);
//		LayoutAnimationController controller = new LayoutAnimationController(animation);
//		rl_home_ican_navigate.setLayoutAnimation(controller);
//	}
//
//	/**
//	 * 初始化我想界面
//	 */
//	private void initIThink()
//	{
//		layout_home_ithink = (ViewGroup) inflater.inflate(R.layout.layout_home_ithink, null);
//		ViewGroup rl_home_ithink_navigate = (ViewGroup) inflater.inflate(R.layout.child_home_ithink, layout_home_ithink, false);
//		layout_home_ithink.addView(rl_home_ithink_navigate);
//		al_home_ithink_navigate = (ViewGroup) rl_home_ithink_navigate.findViewById(R.id.al_home_ithink_navigate);
//		m_oneIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_one);
//	    m_twoIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_two);
//	    m_threeIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_three);
//	    m_fourIThinkTv = (TextView) layout_home_ithink.findViewById(R.id.ithink_tv_four);
//	}
//	
//	/**
//	 * 显示动画
//	 */
//	public void ShowScale(TextView textView) 
//	{ 
//	    AnimationSet animationSet=new AnimationSet(true); 
//	    ScaleAnimation scaleAnimation=new ScaleAnimation( 
//	    0.1f, 1, 0.1f, 1, 
//	    Animation.RELATIVE_TO_SELF, 0.5f, 
//	    Animation.RELATIVE_TO_SELF, 0.5f); 
//	    scaleAnimation.setDuration(2000); 
//	    animationSet.addAnimation(scaleAnimation); 
//	    textView.startAnimation(scaleAnimation); 
//	} 
//	
//	/**
//	 * 隐藏动画
//	 */
//	public void HiddenScale(TextView textView) 
//	{ 
//	    AnimationSet animationSet=new AnimationSet(true); 
//	    ScaleAnimation scaleAnimation=new ScaleAnimation( 
//	    1, 0.1f, 1, 0.1f, 
//	    Animation.RELATIVE_TO_SELF, 0.5f, 
//	    Animation.RELATIVE_TO_SELF, 0.5f); 
//	    scaleAnimation.setDuration(2000); 
//	    animationSet.addAnimation(scaleAnimation); 
//	    textView.startAnimation(scaleAnimation); 
//	}
//
//	
//	/**
//	 * 选择我想,我能时 上面的导航条变化
//	 * @param index
//	 * 
//	 */
//	private void checkType(int index)
//	{
//		switch (index)
//		{
//		case 0:
//			search_type = 0;
//			home_tv_check_search_indecator.setText("我想");
//			tv_home_type_left_select.setTextColor(getResources().getColor(R.color.home_type_text_select));
//			tv_home_type_right_select.setTextColor(getResources().getColor(R.color.text_color_normal));
//			v_home_type_select_left_underline.setVisibility(View.VISIBLE);
//			v_home_type_select_right_underline.setVisibility(View.INVISIBLE);
//			break;
//		case 1:
//			search_type = 1;
//			home_tv_check_search_indecator.setText("我能");
//			tv_home_type_left_select.setTextColor(getResources().getColor(R.color.text_color_normal));
//			tv_home_type_right_select.setTextColor(getResources().getColor(R.color.home_type_text_select));
//			v_home_type_select_left_underline.setVisibility(View.INVISIBLE);
//			v_home_type_select_right_underline.setVisibility(View.VISIBLE);
//			break;
//		}
//	}
//
//	/**
//	 * 我想,我能 viewpager的adapter
//	 * @author ChenGuoqing
//	 * 
//	 */
//	class HomeTypePageAdapter extends PagerAdapter
//	{
//
//		@Override
//		public int getCount()
//		{
//			return 2;
//		}
//
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1)
//		{
//			return arg0 == arg1;
//		}
//
//		@Override
//		public Object instantiateItem(ViewGroup container, int position)
//		{
//			View view = null;
//			switch (position)
//			{
//			case 0:
//				view = layout_home_ithink;
//				break;
//			case 1:
//				view = layout_home_ican;
//				break;
//			}
//			container.addView(view);
//			return view;
//		}
//
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object)
//		{
//			container.removeView((View) object);
//		}
//	}
//
//	@Override
//	public void onPageScrollStateChanged(int arg0)
//	{
//
//	}
//
//	@Override
//	public void onPageScrolled(int arg0, float arg1, int arg2)
//	{
//
//	}
//
//	@Override
//	public void onPageSelected(int arg0)
//	{
//		checkType(arg0);
//	}
//
//	@Override
//	protected void onDestroy()
//	{
//		super.onDestroy();
//		
//		if (popupWindowCheckSearchType != null)
//		{
//		    popupWindowCheckSearchType.dismiss();
//		    popupWindowCheckSearchType = null;
//		}
//		
//	}
//
//	/**
//	 * 搜索type改变
//	 * @param view
//	 */
//	private void checkSearchType(View view)
//	{
//		View home_iv_ican_indicator = view.findViewById(R.id.home_iv_ican_indicator);
//		View home_iv_ithink_indicator = view.findViewById(R.id.home_iv_ithink_indicator);
//		TextView home_tv_ican_indicator = (TextView) view.findViewById(R.id.home_tv_ican_indicator);
//		TextView home_tv_ithink_indicator = (TextView) view.findViewById(R.id.home_tv_ithink_indicator);
//		if (search_type == 0)
//		{
//			home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);
//			home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);
//			home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));
//			home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));
//		} else if (search_type == 1)
//		{
//			home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);
//			home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);
//			home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));
//			home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));
//		}
//	};
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.ll_home_search_check_type:
//			View view = inflater.inflate(R.layout.popup_home_check_seach, null);
//			checkSearchType(view);
//			view.findViewById(R.id.home_ll_ican).setOnClickListener(this);
//			view.findViewById(R.id.home_ll_ithink).setOnClickListener(this);
//			popupWindowCheckSearchType = null;
//			popupWindowCheckSearchType = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//			popupWindowCheckSearchType.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_home_check_popup));
//			popupWindowCheckSearchType.setOutsideTouchable(true);
//			popupWindowCheckSearchType.showAsDropDown(ll_home_search_check_type, 0, 10);
//			break;
//		case R.id.home_ll_ican:
//			search_type = 1;
//			home_tv_check_search_indecator.setText("我能");
//			vp_home_type_select.setCurrentItem(1);
//			popupWindowCheckSearchType.dismiss();
//			break;
//		case R.id.home_ll_ithink:
//			search_type = 0;
//			vp_home_type_select.setCurrentItem(0);
//			home_tv_check_search_indecator.setText("我想");
//			popupWindowCheckSearchType.dismiss();
//			break;
//			
//		case R.id.ithink_tv_one:
//		{
//		    Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ithink[0]);
//            startActivity(intent);
//            break;
//		}
//		
//		case R.id.ithink_tv_two:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ithink[1]);
//            startActivity(intent);
//            break;
//        }
//        
//		case R.id.ithink_tv_three:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ithink[2]);
//            startActivity(intent);
//            break;
//        }
//        
//		case R.id.ithink_tv_four:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ithink[3]);
//            startActivity(intent);
//            break;
//        }
//		case R.id.home_ican_tv_one:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ican[0]);
//            startActivity(intent);
//            break;
//        }
//        
//        case R.id.home_ican_tv_two:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ican[1]);
//            startActivity(intent);
//            break;
//        }
//        
//        case R.id.home_ican_tv_three:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ican[2]);
//            startActivity(intent);
//            break;
//        }
//        
//        case R.id.home_ican_tv_four:
//        {
//            Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
//            intent.putExtra("msg", ican[3]);
//            startActivity(intent);
//            break;
//        }
//		case R.id.home_tv_content:
//		{
//			Bundle bundle = new Bundle();
//			bundle.putInt("searchType", SearchActivity.SEARCH_TYPE_TEXT);
//			toActivity(SearchActivity.class, bundle);
//			break;
//		}
//		case R.id.home_rl_speech:
//		{
//			Intent intent = new Intent(HomeActivity.this, SpeechSearchActivity.class);
//			startActivity(intent);
//			break;
//		}
//		case R.id.tv_home_type_left_select:
//		{
//			vp_home_type_select.setCurrentItem(0);
//			break;
//		}
//		case R.id.tv_home_type_right_select:
//		{
//			vp_home_type_select.setCurrentItem(1);
//			break;
//		}
//		}
//	}
//	
//
//	private Runnable run = new Runnable(){
//
//		@Override
//		public void run() {
//			handler.postDelayed(this, POSTDELAYED_TIME);
//			changeICAN();
//			changeIThink();
//		}};
//		
//		private void changeIThink(){
//			for (int i = 0; i < ithink.length; i++)
//			{
//				String name = ithink[i];
//				TextView textView = (TextView) al_home_ithink_navigate.getChildAt(i);
//				String color = ithinkColor[i];
//				textView.setTextColor(Color.parseColor(color));
//				textView.setText(name);
//				ShowScale(textView);
//				HiddenScale(textView);
//			}
//		}
//		private void changeICAN(){
//			for (int i = 0; i < ican.length; i++)
//			{
//				String name = ican[i];
//				TextView textView = (TextView) rl_home_ican_navigate.getChildAt(i);
//				textView.setText("·" + name);
//				String color = ithinkColor[i];
//				textView.setTextColor(Color.parseColor(color));
//				textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
//				HiddenScale(textView);
//			}
//		}
//
//		@Override
//		public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
//			switch (info.getState()) {
//			case STATE_ERROR_SERVER:
//				Toast.makeText(mContext, "服务器地址错误", Toast.LENGTH_SHORT).show();
//				break;
//			case STATE_NO_NETWORK_CONNECT:
//				Toast.makeText(mContext, "没有网络，请检查您的网络连接", Toast.LENGTH_SHORT).show();
//				break;
//			case STATE_TIME_OUT:
//				Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
//				break;
//			case STATE_UNKNOWN:
//				Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
//				break;
//			case STATE_OK:
//				BaseNetWork bNetWork = info.getmBaseNetWork();
//				JSONObject body = bNetWork.getBody();
//				switch (bNetWork.getMessageType()) {
//				case 810009:
//					SystemConfig.BS_SERVER = body.optString("bserver");
//					LogUtils.i("------------------"+SystemConfig.BS_SERVER);
//					HomeRequest.getInstance().requestHotWord(this, this);		//获取热词	保证业务服务器地址存在
//					break;
//				case 840007:
//					try {
//						JSONArray iCan = body.getJSONArray("ican");
//						iCan.toString();
//						JSONArray iNeed = body.getJSONArray("ineed");
//						iNeed.toString();
//						
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					break;					
//				default:
//					break;
//				}
//				
//
//
//			default:
//				break;
//				}
//		}
//}
