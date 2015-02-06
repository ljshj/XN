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
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.help.DeleteListener;
import com.bgood.xn.ui.help.IDeleteCallback;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.ui.user.info.ShowNameCardListener;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.ActionView;
/**
 * 
 * @todo:炫能幽默秀适配器
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class JokeAdapter extends KBaseAdapter 
{

	/**是否是排行榜*/
	private boolean isRank = false;	
	
	public JokeAdapter(List<?> mList, Activity mActivity,OnClickListener listener,boolean isRank) {
		super(mList, mActivity, listener);
		this.isRank = isRank;
	}
	
	public JokeAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
		super(mList, mActivity, listener);
	}

	public JokeAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}
	
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
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvComments = (TextView) convertView.findViewById(R.id.tv_comments);
			holder.gridView = (GridView) convertView.findViewById(R.id.gv_show_img);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
			
			holder.llTransArea = (LinearLayout) convertView.findViewById(R.id.ll_old_area);
			holder.tvOldAuthorName = (TextView) convertView.findViewById(R.id.tv_old_user);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.oldgridView = (GridView) convertView.findViewById(R.id.gv_old_show_img);
			
			holder.avZan = (ActionView) convertView.findViewById(R.id.av_zan);
			holder.avReply = (ActionView) convertView.findViewById(R.id.av_reply);
			holder.avTranspnt = (ActionView) convertView.findViewById(R.id.av_transpont);
			holder.avShare = (ActionView) convertView.findViewById(R.id.av_share);
			
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
		
		holder.tvTime.setText(ToolUtils.getFormatDate(jokeBean.date_time));
		
//		if(position < 3 && isRank){
//			holder.tvRank.setText(String.valueOf(position+1));
//			holder.tvRank.setVisibility(View.VISIBLE);
//		}else{
//			holder.tvRank.setVisibility(View.INVISIBLE);
//		}
		
		if(!TextUtils.isEmpty(BGApp.mUserId)&& BGApp.mUserId.equals(String.valueOf(jokeBean.userid))){
			holder.ivDelete.setVisibility(View.VISIBLE);
			holder.ivDelete.setOnClickListener(new DeleteListener(jokeBean, mActivity,callback));
		}else{
			holder.ivDelete.setVisibility(View.GONE);
		}
		
		
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
		
		
		return convertView;
	}

	/**删除微墙的回调*/
	private IDeleteCallback callback = new IDeleteCallback() {
		@Override
		public void deleteAction(final Object object) {
			if(object instanceof JokeBean){
				mList.remove(object);
				notifyDataSetChanged();
			}
		}
	};
	
	final class Holder
	{
		 ImageView ivDelete;
		 ImageView ivAuthorImg;
		 TextView tvAuthorName;
		 TextView tvTime;
		 TextView tvComments;
		 LinearLayout llTransArea;
		 TextView tvOldAuthorName;
		 TextView tvContent;
		 GridView gridView,oldgridView;
		
		 ActionView avZan,avReply,avTranspnt,avShare;
	}
}
