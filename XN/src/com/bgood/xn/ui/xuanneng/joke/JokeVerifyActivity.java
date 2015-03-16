package com.bgood.xn.ui.xuanneng.joke;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.ui.xuanneng.XuanNengMainActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.NoScrollViewPager;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:幽默秀审核
 * @date:2015-3-10 上午9:51:17
 * @author:hg_liuzl@163.com
 */
public class JokeVerifyActivity extends BaseActivity implements OnClickListener,TaskListenerWithState{
	
	private TitleBar mTitleBar;
	private NoScrollViewPager paper;
	private ContentAdapter adapter;
	private List<View> lists = new ArrayList<View>();
	private List<JokeBean> jokeBeans = new ArrayList<JokeBean>();
	private JokeBean mCurrentJoke;
	/**起始页**/
	private int mStartSize= 0;
	/**最大 的炫能ID*/
	private int maxxnid = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_joke_verify);
		maxxnid = pUitl.getUnCheckJokeMaxID();
		initView();
		doRequestData();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		pUitl.setUnCheckJokeMaxID(maxxnid);
	}
	
	/**
	 * 
	 * @todo:处理获取到的数据
	 * @date:2015-3-11 上午10:15:50
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void dealData(List<JokeBean> jokes) {
		if(null!=jokes && jokes.size()>0){
			lists.clear();
			jokeBeans.clear();
			for (JokeBean joke : jokes) {
				View v = addView();
				TextView tv = (TextView) v.findViewById(R.id.tv_content);
				tv.setText(joke.content);
				lists.add(v);
			}
			jokeBeans.addAll(jokes);
			mCurrentJoke = jokeBeans.get(0);
			adapter.notifyDataSetChanged();
		}else{
			BToast.show(mActivity, "暂无可审核内容");
		}
	}
	
	
	/**
	 * 
	 * @todo:初始化控件
	 * @date:2015-3-11 上午10:16:04
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void initView() {
		mTitleBar = new TitleBar(mActivity);
		mTitleBar.initAllBar("我来审核", "举报");
		mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BToast.show(mActivity, "举报");
			}
		});
		findViewById(R.id.av_disagree).setOnClickListener(this);
		findViewById(R.id.av_agree).setOnClickListener(this);
		findViewById(R.id.av_create).setOnClickListener(this);
		initPaper();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.av_create:
			doCheckState(JokeBean.JOKE_ORIGINAL);
			break;
		case R.id.av_disagree:
			doCheckState(JokeBean.JOKE_DISAGREE);
			break;
		case R.id.av_agree:
			doCheckState(JokeBean.JOKE_AGREE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @todo:审核操作
	 * @date:2015-3-11 上午10:07:15
	 * @author:hg_liuzl@163.com
	 * @params:@param state
	 */
	private void doCheckState(int state) {
		XuannengRequest.getInstance().requestXuanCheck(this, this, mCurrentJoke.jokeid, state,false);
	}
	
	/**
	 * 
	 * @todo:请求数据
	 * @date:2015-3-11 上午10:18:09
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void doRequestData() {
		XuannengRequest.getInstance().requestXuanUnCheckList(this, this, XuanNengMainActivity.XUANNENG_JOKE,JokeBean.JOKE_UN_VERIFY, mStartSize, mStartSize+PAGE_SIZE_ADD,maxxnid);

	}
	
	private void initPaper() {
		paper = (NoScrollViewPager) findViewById(R.id.paper_content);
		paper.setNoScroll(false);
		adapter = new ContentAdapter(lists);
		paper.setAdapter(adapter);
		paper.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (position > lists.size()-1) {//到了最后一个的时候，再重新加载数据
					doRequestData();
				}else{
					mCurrentJoke = jokeBeans.get(position);
					if(Integer.valueOf(mCurrentJoke.jokeid) >=maxxnid){
						maxxnid = Integer.valueOf(mCurrentJoke.jokeid);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
	}
	
	private View addView() {
		return inflater.inflate(R.layout.item_joke_verify, null);
	}
	
	class ContentAdapter extends PagerAdapter{
		public List<View> mListViews;

		public ContentAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}


		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}
		
		@Override
		public void setPrimaryItem(ViewGroup container, int position,Object object) {
			super.setPrimaryItem(container, position, object);
		}
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if (info.getState() == HttpTaskState.STATE_OK) {
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			switch (bNetWork.getMessageType()) {
			case 870022:
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
					dealData(response.jokes);
				}
				break;
			case 870021:	//审核
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					BToast.show(mActivity, "审核成功");
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 
	 * @todo:审核幽默秀
	 * @date:2015-3-16 上午10:19:10
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public static void doVerifyJoke(Activity activity){
		 Intent intent = new Intent(activity, JokeVerifyActivity.class);
         activity.startActivity(intent);
	}
}
