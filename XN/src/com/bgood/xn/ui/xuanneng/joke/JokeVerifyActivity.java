package com.bgood.xn.ui.xuanneng.joke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.FriendBean;
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
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.NoScrollViewPager;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * @todo:幽默秀审核
 * @date:2015-3-10 上午9:51:17
 * @author:hg_liuzl@163.com
 */
public class JokeVerifyActivity extends BaseActivity implements OnClickListener,OnGestureListener,TaskListenerWithState{
	
	private GestureDetector detector;
	private ViewFlipper flipper;
	private TitleBar mTitleBar;
	private List<JokeBean> jokeBeans = new ArrayList<JokeBean>();
	private JokeBean mCurrentJoke;
	/**起始页**/
	private int mStartSize= 0;
	/**最大 的炫能ID*/
	private int maxxnid = 0;
	
	private ImageView ivNodata;
	
	/**对页数进行计数,也从0开始计数,对应集合索引**/
	private int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_joke_verify);
		maxxnid = pUitl.getUnCheckJokeMaxID();
		initView();
		doRequestData();
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
	
	@Override
	protected void onStop() {
		super.onStop();
		pUitl.setUnCheckJokeMaxID(maxxnid);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event); 
	}
	
	private View addView() {
		return inflater.inflate(R.layout.item_joke_verify, null);
	}
	
	
	/**
	 * 
	 * @todo:根据jokeId排序一下
	 * @date:2015-3-24 上午10:33:16
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void sortByID() {
		Collections.sort(jokeBeans, new Comparator<JokeBean>() {
			@Override
			public int compare(final JokeBean j1, final JokeBean j2) {
				if (Integer.valueOf(j1.jokeid) == Integer.valueOf(j2.jokeid)) {
					return 0;
				} else if (Integer.valueOf(j1.jokeid) < Integer.valueOf(j2.jokeid)) {
					return -1;
				} else {
					return 1;
				}
			}
		});
	}
	
	/**
	 * 
	 * @todo:处理获取到的数据
	 * @date:2015-3-11 上午10:15:50
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void dealData() {
		if(null!=jokeBeans && jokeBeans.size()>0){
			sortByID();
			for (JokeBean joke : jokeBeans) {
				View v = addView();
				TextView tv = (TextView) v.findViewById(R.id.tv_content);
				tv.setText(joke.content);
				GridView gv = (GridView) v.findViewById(R.id.gv_show_img);
				ImgUtils.showImgs(joke.imgs,gv,mActivity);
				flipper.addView(v, new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			}
			mCurrentJoke = jokeBeans.get(0);
			maxxnid = Integer.valueOf(mCurrentJoke.jokeid);
			ivNodata.setVisibility(View.GONE);
			mTitleBar.rightBtn.setVisibility(View.VISIBLE);
		}else{
			mTitleBar.rightBtn.setVisibility(View.GONE);
			ivNodata.setVisibility(View.VISIBLE);
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
				doCheckState(JokeBean.JOKE_REPORT);
				showNextPage();
			}
		});
		
		flipper = (ViewFlipper) findViewById(R.id.paper_content);
		detector = new GestureDetector(this);
		
		ivNodata = (ImageView) findViewById(R.id.iv_no_data);
		findViewById(R.id.av_disagree).setOnClickListener(this);
		findViewById(R.id.av_agree).setOnClickListener(this);
		findViewById(R.id.av_create).setOnClickListener(this);
		
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
	 * @todo:是否需要加载数据
	 * @date:2015-3-18 下午5:07:05
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void showNextPage() {
		if(count >= jokeBeans.size()-1){
			count = 0;
			doRequestData();
		}else{
			count++;
			mCurrentJoke = jokeBeans.get(count);
			maxxnid = Integer.valueOf(mCurrentJoke.jokeid);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.animation_left_out));
			this.flipper.showNext();
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
		XuannengRequest.getInstance().requestXuanCheck(this, this, mCurrentJoke.jokeid, String.valueOf(state),false);
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

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if (info.getState() == HttpTaskState.STATE_OK) {
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			switch (bNetWork.getMessageType()) {
			case 870022:
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
					jokeBeans.clear();
					jokeBeans.addAll(response.jokes);
					dealData();
				}
				break;
			case 870021:	//不审核，审核，原创，举报
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					//BToast.show(mActivity, "操作成功");
					showNextPage();
				}else{
					BToast.show(mActivity, "操作失败");
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


	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float arg3) {
		
		if ((e1.getX() - e2.getX() > 30)) {	//向左滑动距离大于50就进入下一页
				showNextPage();
			return true;
		} 
		return false;
	}


	@Override
	public void onLongPress(MotionEvent arg0) {
	}


	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,float arg3) {
		return false;
	}


	@Override
	public void onShowPress(MotionEvent arg0) {
	}


	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
}
