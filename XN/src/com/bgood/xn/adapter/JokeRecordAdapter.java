package com.bgood.xn.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.help.DeleteListener;
import com.bgood.xn.ui.help.IDeleteCallback;
import com.bgood.xn.ui.user.info.ShowNameCardListener;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.ActionView;
/**
 * 
 * @todo:炫能榜单
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class JokeRecordAdapter extends KBaseAdapter 
{

	private int paihangindex = 0;
	private String indextime = "";
	
	public enum JokeRank{
		RANK,RECORD
	}
	
	public enum JokeTimeType{
		DAY,WEEK,MONTH
	}
	
	private JokeRank mType;
	
	private JokeTimeType mTimeType;
	
	public JokeRecordAdapter(List<?> mList, Activity mActivity,OnClickListener listener,JokeRank type) {
		super(mList, mActivity, listener);
		this.mType = type;
	}
	
	public JokeRecordAdapter(List<JokeBean> mList, Activity mActivity,OnClickListener listener,JokeRank type,JokeTimeType timeType) {
		super(mList, mActivity, listener);
		this.mType = type;
		this.mTimeType = timeType;
	}
	
	public JokeRecordAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
		super(mList, mActivity, listener);
	}

	public JokeRecordAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final Holder holder;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.weiqiang_item_layout, parent, false);
			holder.ivAuthorImg = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvRecordTime = (TextView) convertView.findViewById(R.id.tv_record_time);
			holder.tvComments = (TextView) convertView.findViewById(R.id.tv_comments);
			holder.gridView = (GridView) convertView.findViewById(R.id.gv_show_img);
			holder.ivRank = (ImageView) convertView.findViewById(R.id.iv_rank);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
			holder.ivOriginal = (ImageView) convertView.findViewById(R.id.iv_original);
			
			holder.llTransArea = (LinearLayout) convertView.findViewById(R.id.ll_old_area);
			holder.tvOldAuthorName = (TextView) convertView.findViewById(R.id.tv_old_user);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.oldgridView = (GridView) convertView.findViewById(R.id.gv_old_show_img);
			
			holder.avZan = (ActionView) convertView.findViewById(R.id.av_zan);
			holder.avReply = (ActionView) convertView.findViewById(R.id.av_reply);
			holder.avTranspnt = (ActionView) convertView.findViewById(R.id.av_transpont);
			holder.avShare = (ActionView) convertView.findViewById(R.id.av_share);
			
			holder.llShare = (LinearLayout) convertView.findViewById(R.id.ll_share);
			
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final JokeBean jokeBean = (JokeBean) mList.get(position);
		
		BGApp.getInstance().setImage(jokeBean.getPhoto(),holder.ivAuthorImg);
		
		holder.ivAuthorImg.setOnClickListener(new ShowNameCardListener(jokeBean,mActivity));
		
		holder.tvAuthorName.setText(jokeBean.username);
		holder.tvAuthorName.setOnClickListener(new ShowNameCardListener(jokeBean,mActivity));
		
		holder.tvDistance.setVisibility(View.VISIBLE);
		holder.tvDistance.setText(ToolUtils.formatDistance(jokeBean.distance));
		
		holder.tvTime.setText(ToolUtils.getFormatDate(jokeBean.date_time));
		
		if(mType == JokeRank.RECORD){		//如果是榜单，则需要全部展示
			String time = "";
			if(mTimeType == JokeTimeType.DAY){	//日排行
				time = ToolUtils.getFormatTime(jokeBean.opartime,"MM月dd日");
			}else if(mTimeType == JokeTimeType.WEEK){	//周排行
				time = ToolUtils.getFormatTime(jokeBean.opartime,"MM月dd日",1000 * 3600 * 24 * 7L)+"-"+ToolUtils.getFormatTime(jokeBean.opartime,"MM月dd日");
			}else{ //月排行
				time = ToolUtils.getFormatTime(jokeBean.opartime,"yyyy年MM月");
			}
			setBackgroudByRecord(jokeBean.paihang,holder.ivRank);
			holder.tvRecordTime.setText(time);
			holder.tvRecordTime.setVisibility(View.VISIBLE);
		}else{ //if(mType == JokeRank.RANK)
			holder.tvRecordTime.setVisibility(View.GONE);
			//如果只是排行榜，则只展示前3名
			if(position < 3){
				setBackgroudByRank(position,holder.ivRank);
			}else{
				holder.ivRank.setVisibility(View.INVISIBLE);
			}
		}
		
		
		
//		if(!TextUtils.isEmpty(BGApp.mUserId)&& BGApp.mUserId.equals(String.valueOf(jokeBean.userid))){
//			holder.ivDelete.setVisibility(View.VISIBLE);
//			holder.ivDelete.setOnClickListener(new DeleteListener(jokeBean, mActivity,callback));
//		}else{
//			holder.ivDelete.setVisibility(View.GONE);
//		}
		
		//显示是否为原创的icon
		
		//holder.ivOriginal.setVisibility(jokeBean.original == 0?View.GONE:View.VISIBLE);
		holder.ivOriginal.setVisibility(position % 2 ==0?View.GONE:View.VISIBLE);
		
		
		if(!TextUtils.isEmpty(jokeBean.fromname)){	//如果转发人存在
			holder.llTransArea.setVisibility(View.VISIBLE);
			holder.tvOldAuthorName.setText(jokeBean.fromname);
			holder.tvOldAuthorName.setOnClickListener(new ShowNameCardListener(jokeBean,mActivity));
			holder.tvContent.setText(jokeBean.content);
			holder.tvComments.setText(jokeBean.Comments);
			ImgUtils.showImgs(jokeBean.imgs,holder.oldgridView,mActivity);
			holder.gridView.setVisibility(View.GONE);
			holder.tvContent.setVisibility(TextUtils.isEmpty(jokeBean.content)?View.GONE:View.VISIBLE);
			holder.tvComments.setVisibility(TextUtils.isEmpty(jokeBean.Comments)?View.GONE:View.VISIBLE);
		}else{
			holder.llTransArea.setVisibility(View.GONE);
			holder.tvComments.setText(jokeBean.content);
			holder.tvComments.setVisibility(TextUtils.isEmpty(jokeBean.content)?View.GONE:View.VISIBLE);
			holder.gridView.setVisibility(View.GONE);
			ImgUtils.showImgs(jokeBean.imgs,holder.gridView,mActivity);
		}
		
		holder.avZan.setCount(jokeBean.like_count);
		holder.avZan.setOnClickListener(mListener);
		holder.avZan.setTag(jokeBean);
		
		holder.avReply.setCount(jokeBean.comment_count);
		holder.avReply.setOnClickListener(mListener);
		holder.avReply.setTag(jokeBean);
		
		holder.avTranspnt.setCount(jokeBean.forward_count);
		holder.avTranspnt.setOnClickListener(mListener);
		holder.avTranspnt.setTag(jokeBean);
		
		holder.avShare.setCount(jokeBean.share_count);
		holder.avShare.setOnClickListener(mListener);
		holder.avShare.setTag(jokeBean);
		
		holder.llShare.setOnClickListener(mListener);
		holder.llShare.setTag(jokeBean);
		
		return convertView;
	}
	
	private void setBackgroudByRecord(int paihang,ImageView iv){
		int mResourceID = -1;
		switch (paihang) {
		case 1:
			mResourceID = R.drawable.icon_joke_one;
			break;
		case 2:
			mResourceID = R.drawable.icon_joke_two;
			break;
		default:
			mResourceID = R.drawable.icon_joke_three;
			break;
		}
		iv.setImageResource(mResourceID);
		iv.setVisibility(View.VISIBLE);
	}
	
	private void setBackgroudByRank(int position,ImageView iv){
		int mResourceID = -1;
		switch (position % 3) {
		case 0:
			mResourceID = R.drawable.icon_joke_one;
			break;
		case 1:
			mResourceID = R.drawable.icon_joke_two;
			break;
		default:
			mResourceID = R.drawable.icon_joke_three;
			break;
		}
		iv.setImageResource(mResourceID);
		iv.setVisibility(View.VISIBLE);
	}

	/**删除微墙的回调*/
	private IDeleteCallback callback = new IDeleteCallback() {
		@Override
		public void deleteAction(final Object object) {
			if(object instanceof WeiQiangBean){
				mList.remove(object);
				notifyDataSetChanged();
			}
		}
	};
	
	final class Holder
	{
		 ImageView ivDelete,ivAuthorImg,ivOriginal;
		 ImageView ivRank;
		 TextView tvAuthorName;
		 TextView tvDistance;
		 TextView tvTime,tvRecordTime;
		 TextView tvComments;
		 LinearLayout llTransArea,llShare;
		 TextView tvOldAuthorName;
		 TextView tvContent;
		 GridView gridView,oldgridView;
		
		 ActionView avZan,avReply,avTranspnt,avShare;
	}
}
