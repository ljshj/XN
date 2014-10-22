package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.CommentBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.squareup.picasso.Picasso;
/**
 * 
 * @todo:微墙适配器
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class WeiqiangAdapter extends KBaseAdapter 
{
	public WeiqiangAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Holder holder;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_weiqiang_comment, parent, false);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_icon);
			holder.nameTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_name);
			holder.distanceTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_distance);
			holder.deleteImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_delete);
			holder.timeTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_time);
			holder.layout_transform_send_name = (LinearLayout) convertView.findViewById(R.id.layout_transform_send);
			holder.transform_send_nameTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_transform_send_by_name);
			holder.contentTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_content);
			holder.layout_zan_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_zan_count);
			holder.zan_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_zan_count);
			holder.layout_reply_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_reply_count);
			holder.reply_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_reply_count);
			holder.layout_transform_send_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_transform_send_count);
			holder.transform_send_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_transform_send_count);
			holder.layout_share_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_share_count);
			holder.share_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_share_count);
			holder.layout_images = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_comment_images);
			holder.oneImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_one);
			holder.twoImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_two);
			holder.threeImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_three);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final CommentBean commentsDTO = (CommentBean) mList.get(position);
		if (commentsDTO.senderIcon != null && !commentsDTO.senderIcon.equals(""))
		{
			Picasso.with(mActivity).load(commentsDTO.senderIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
		}
		holder.nameTv.setText(commentsDTO.senderName);
		holder.distanceTv.setText(commentsDTO.distance);
		holder.timeTv.setText(commentsDTO.sendTime);
		
		if (commentsDTO.from_name != null && !commentsDTO.from_name.equals(""))
		{
			holder.layout_transform_send_name.setVisibility(View.VISIBLE);
			holder.transform_send_nameTv.setText(commentsDTO.from_name);
		}
		else
		{
			holder.layout_transform_send_name.setVisibility(View.GONE);
		}
		
		holder.contentTv.setText(commentsDTO.content);
		holder.zan_countTv.setText(commentsDTO.like_count + "");
		holder.reply_countTv.setText(commentsDTO.revertCount + "");
		holder.transform_send_countTv.setText(commentsDTO.forward_count + "");
		holder.share_countTv.setText(commentsDTO.share_count + "");
		
	
		
		if (commentsDTO.imageList != null && commentsDTO.imageList.size() > 0)
		{
		    if (commentsDTO.imageList.size() == 1)
		    {
		        holder.layout_images.setVisibility(View.VISIBLE);
		        holder.oneImgV.setVisibility(View.VISIBLE);
		        Picasso.with(mActivity).load(commentsDTO.imageList.get(0).img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.oneImgV);
		    }
		    else if (commentsDTO.imageList.size() == 2)
		    {
		        holder.layout_images.setVisibility(View.VISIBLE);
                holder.oneImgV.setVisibility(View.VISIBLE);
                holder.twoImgV.setVisibility(View.VISIBLE);
                Picasso.with(mActivity).load(commentsDTO.imageList.get(0).img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.oneImgV);
                Picasso.with(mActivity).load(commentsDTO.imageList.get(1).img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.twoImgV);
		    }
		    else if (commentsDTO.imageList.size() > 2)
            {
                holder.layout_images.setVisibility(View.VISIBLE);
                holder.oneImgV.setVisibility(View.VISIBLE);
                holder.twoImgV.setVisibility(View.VISIBLE);
                holder.threeImgV.setVisibility(View.VISIBLE);
                Picasso.with(mActivity).load(commentsDTO.imageList.get(0).img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.oneImgV);
                Picasso.with(mActivity).load(commentsDTO.imageList.get(1).img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.twoImgV);
                Picasso.with(mActivity).load(commentsDTO.imageList.get(2).img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.threeImgV);
            }
		}
		
		// 分享
		/*holder.layout_share_count.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                m_shareManager = new ShareManager((WeiqiangPersonActivity)m_context);

                m_shareManager.shareContentInit("adg", "掌上有礼", "", "", "1", "d");
                m_shareManager.openShare();
            }
        });
		
		
		
		//点赞
		holder.layout_zan_count.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                WindowUtil.getInstance().progressDialogShow(m_context, "赞中...");
                messageManager.registerObserver(WeiqiangAdapter.this);
                messageManager.zanWeiqiang(commentsDTO.commentId);
            }
        });*/
		
		return convertView;
	}

	class Holder
	{
		public ImageView iconImgV;
		public TextView nameTv;
		public TextView distanceTv;
		public ImageView deleteImgV;
		public TextView timeTv;
		public LinearLayout layout_transform_send_name;
		public TextView transform_send_nameTv;
		public TextView contentTv;
		public LinearLayout layout_zan_count;
		public TextView zan_countTv;
		public LinearLayout layout_reply_count;
		public TextView reply_countTv;
		public LinearLayout layout_transform_send_count;
		public TextView transform_send_countTv;
		public LinearLayout layout_share_count;
		public TextView share_countTv;
		public LinearLayout layout_images;
		public ImageView oneImgV;
		public ImageView twoImgV;
		public ImageView threeImgV;
	}
}
