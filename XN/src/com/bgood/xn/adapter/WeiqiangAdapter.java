package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.help.DeleteListener;
import com.bgood.xn.ui.help.IDeleteCallback;
import com.bgood.xn.ui.user.info.ShowNameCardListener;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.ActionView;
import com.bgood.xn.view.NoScrollGridView;
/**
 * 
 * @todo:微墙适配器
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class WeiqiangAdapter extends KBaseAdapter 
{
	public WeiqiangAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
		super(mList, mActivity, listener);
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
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvComments = (TextView) convertView.findViewById(R.id.tv_comments);
			holder.gridView = (NoScrollGridView) convertView.findViewById(R.id.gv_show_img);
			
			
			holder.llTransArea = (LinearLayout) convertView.findViewById(R.id.ll_old_area);
			holder.tvOldAuthorName = (TextView) convertView.findViewById(R.id.tv_old_user);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.oldgridView = (NoScrollGridView) convertView.findViewById(R.id.gv_old_show_img);
			
			holder.avZan = (ActionView) convertView.findViewById(R.id.av_zan);
			holder.avReply = (ActionView) convertView.findViewById(R.id.av_reply);
			holder.avTranspnt = (ActionView) convertView.findViewById(R.id.av_transpont);
			holder.avShare = (ActionView) convertView.findViewById(R.id.av_share);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final WeiQiangBean weiqiangBean = (WeiQiangBean) mList.get(position);
		
		BGApp.getInstance().setImage(weiqiangBean.photo, holder.ivAuthorImg);
		
		holder.ivAuthorImg.setOnClickListener(new ShowNameCardListener(weiqiangBean, mActivity));
		
		holder.tvAuthorName.setText(weiqiangBean.name);
		holder.tvAuthorName.setOnClickListener(new ShowNameCardListener(weiqiangBean, mActivity));
		
		if(!TextUtils.isEmpty(BGApp.mUserId)&& BGApp.mUserId.equals(String.valueOf(weiqiangBean.userid))){
			holder.ivDelete.setVisibility(View.VISIBLE);
			holder.ivDelete.setOnClickListener(new DeleteListener(weiqiangBean, mActivity,callback));
		}else{
			holder.ivDelete.setVisibility(View.GONE);
		}
		
		holder.tvTime.setText(ToolUtils.getFormatDate(weiqiangBean.date_time));
		
		if(!TextUtils.isEmpty(weiqiangBean.fromname)){	//如果转发人存在
			holder.llTransArea.setVisibility(View.VISIBLE);
			holder.tvOldAuthorName.setText(weiqiangBean.fromname);
			holder.tvOldAuthorName.setOnClickListener(new ShowNameCardListener(weiqiangBean, mActivity));
			holder.tvContent.setText(weiqiangBean.content);
			holder.tvComments.setText(weiqiangBean.comments);
			ImgUtils.showImgs(weiqiangBean.imgs,holder.oldgridView,mActivity);
			holder.gridView.setVisibility(View.GONE);
			holder.tvContent.setVisibility(TextUtils.isEmpty(weiqiangBean.content)?View.GONE:View.VISIBLE);
			holder.tvComments.setVisibility(TextUtils.isEmpty(weiqiangBean.comments)?View.GONE:View.VISIBLE);
		}else{
			holder.llTransArea.setVisibility(View.GONE);
			holder.tvComments.setText(weiqiangBean.content);
			holder.tvComments.setVisibility(TextUtils.isEmpty(weiqiangBean.content)?View.GONE:View.VISIBLE);
			holder.gridView.setVisibility(View.GONE);
			ImgUtils.showImgs(weiqiangBean.imgs,holder.gridView,mActivity);
		}
		
		holder.avZan.setCount(weiqiangBean.like_count);
		holder.avZan.setOnClickListener(mListener);
		holder.avZan.setTag(weiqiangBean);
		
		holder.avReply.setCount(weiqiangBean.comment_count);
		holder.avReply.setOnClickListener(mListener);
		holder.avReply.setTag(weiqiangBean);
		
		holder.avTranspnt.setCount(weiqiangBean.forward_count);
		holder.avTranspnt.setOnClickListener(mListener);
		holder.avTranspnt.setTag(weiqiangBean);
		
		holder.avShare.setCount(weiqiangBean.share_count);
		holder.avShare.setOnClickListener(mListener);
		holder.avShare.setTag(weiqiangBean);
		
		
		return convertView;
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
	      ImageView ivDelete;
		 ImageView ivAuthorImg;
		 TextView tvAuthorName;
		 TextView tvTime;
		 TextView tvComments;
		
		 LinearLayout llTransArea;
		 TextView tvOldAuthorName;
		 TextView tvContent;
		 NoScrollGridView gridView,oldgridView;
		
		 ActionView avZan,avReply,avTranspnt,avShare;
	}

}
