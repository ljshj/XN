package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.CommentBean;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.bean.JokeCorattionBean;
import com.bgood.xn.bean.SimpleUserBean;
import com.bgood.xn.bean.WeiqiangCorattionBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.ActionView;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 
 * @todo:幽默秀中与我相关的适配类
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class JokeCorrelationAdapter extends KBaseAdapter 
{
	public JokeCorrelationAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
		super(mList, mActivity, listener);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final Holder holder;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_weiqiang_correlation, parent, false);
			holder.ivAuthorImg = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvComments = (TextView) convertView.findViewById(R.id.tv_comments);
			holder.gridView = (GridView) convertView.findViewById(R.id.gv_show_img);
			
			
			holder.llTransArea = (LinearLayout) convertView.findViewById(R.id.ll_old_area);
			holder.tvOldAuthorName = (TextView) convertView.findViewById(R.id.tv_old_user);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.oldgridView = (GridView) convertView.findViewById(R.id.gv_old_show_img);
			
			holder.avZan = (ActionView) convertView.findViewById(R.id.av_zan);
			holder.avReply = (ActionView) convertView.findViewById(R.id.av_reply);
			holder.avTranspnt = (ActionView) convertView.findViewById(R.id.av_transpont);
			holder.avShare = (ActionView) convertView.findViewById(R.id.av_share);
			
			holder.tvLikeUser = (TextView) convertView.findViewById(R.id.tv_like);
			
			holder.lvShowComment = (ListView) convertView.findViewById(R.id.ll_weiqiang_comment);
			
			
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final JokeCorattionBean wcb = (JokeCorattionBean) mList.get(position);
		
		BGApp.getInstance().setImage(wcb.photo,holder.ivAuthorImg);
				
		holder.ivAuthorImg.setOnClickListener(mListener);
		holder.ivAuthorImg.setTag(wcb);
		
		holder.tvAuthorName.setText(wcb.name);
		holder.tvAuthorName.setOnClickListener(mListener);
		holder.tvAuthorName.setTag(wcb);
		
		
		holder.tvTime.setText(ToolUtils.getFormatDate(wcb.date_time));
		
		if(!TextUtils.isEmpty(wcb.fromname)){	//如果转发人存在
			holder.llTransArea.setVisibility(View.VISIBLE);
			holder.tvOldAuthorName.setText(wcb.fromname);
			holder.tvOldAuthorName.setOnClickListener(mListener);
			holder.tvOldAuthorName.setTag(wcb);
			holder.tvContent.setText(wcb.content);
			holder.tvComments.setText(wcb.comments);
			ImgUtils.showImgs(wcb.imgs,holder.oldgridView,mActivity);
			holder.gridView.setVisibility(View.GONE);
			holder.tvContent.setVisibility(TextUtils.isEmpty(wcb.content)?View.GONE:View.VISIBLE);
			holder.tvComments.setVisibility(TextUtils.isEmpty(wcb.comments)?View.GONE:View.VISIBLE);
		}else{
			holder.llTransArea.setVisibility(View.GONE);
			holder.tvComments.setText(wcb.content);
			holder.tvComments.setVisibility(TextUtils.isEmpty(wcb.content)?View.GONE:View.VISIBLE);
			holder.gridView.setVisibility(View.GONE);
			ImgUtils.showImgs(wcb.imgs,holder.gridView,mActivity);
		}
		
		setCountAndListener(holder.avZan, wcb.like_count, mListener, wcb);
		setCountAndListener(holder.avReply, wcb.comment_count, mListener, wcb);
		setCountAndListener(holder.avTranspnt, wcb.forward_count, mListener, wcb);
		setCountAndListener(holder.avShare, wcb.share_count, mListener, wcb);
		
		setLikeUserNick(wcb.likeuserlist, holder.tvLikeUser);
		
		setCommentList(wcb.commentlist,holder.lvShowComment);
		
		return convertView;
	}
	
	/**设置下面的数据*/
	
	private void setCountAndListener(ActionView av,String count,OnClickListener listener,JokeCorattionBean bean){
		av.setCount(count);
		av.setOnClickListener(mListener);
		av.setTag(bean);
	}
	
	
	/**设置点赞的人*/
	private void setLikeUserNick(List<SimpleUserBean> beans,TextView tvLike){
		if(beans == null ||beans.size() ==0){
			tvLike.setVisibility(View.GONE);
		}else{
			tvLike.setVisibility(View.VISIBLE);
			final StringBuilder sb = new StringBuilder();
			for(int i = 0;i<beans.size();i++){
				final SimpleUserBean sub = beans.get(i);
				if(i <= 1){
						sb.append(sub.nicename);
						if(i-1 != 0){
							sb.append(",");
						}
				}else{
					sb.append("等"+beans.size()+"人");
					break;
				}
			}
			sb.append("觉得很赞");
			tvLike.setText(sb.toString());
		}
	}
	
	private void setCommentList(List<CommentBean> comments,ListView lv){
		CorrelationCommentAdapter adapter = new CorrelationCommentAdapter(comments, mActivity);
		lv.setAdapter(adapter);
	}
	
	
	
	

	final class Holder
	{
		 ImageView ivAuthorImg;
		 TextView tvAuthorName;
		 TextView tvTime;
		 TextView tvComments;
		
		 LinearLayout llTransArea;
		 TextView tvOldAuthorName;
		 TextView tvContent;
		 GridView gridView,oldgridView;
		
		 ActionView avZan,avReply,avTranspnt,avShare;
		
		 TextView tvLikeUser;
		 ListView lvShowComment;
		
	}
}
