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
import com.bgood.xn.bean.CommentBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.utils.ToolUtils;
import com.squareup.picasso.Picasso;
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
		Holder holder;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_weiqiang, parent, false);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_icon);
			holder.nameTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_name);
			holder.distanceTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_distance);
			holder.deleteImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_delete);
			holder.timeTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_time);
			holder.layout_transform_send_name = (LinearLayout) convertView.findViewById(R.id.layout_transform_send);
			holder.transform_send_nameTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_transform_send_by_name);
			holder.contentTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_content);
			holder.zan_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_zan_count);
			holder.reply_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_reply_count);
			holder.transform_send_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_transform_send_count);
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
		
		final WeiQiangBean weiqiangBean = (WeiQiangBean) mList.get(position);
		
		if (TextUtils.isEmpty(weiqiangBean.photo))
		{
			Picasso.with(mActivity).load(weiqiangBean.photo).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
		}
		holder.nameTv.setText(weiqiangBean.name);
		holder.distanceTv.setText(weiqiangBean.distance);
		holder.distanceTv.setVisibility(View.GONE);
		holder.timeTv.setText(ToolUtils.getFormatDate(weiqiangBean.date_time));
		
		if (!TextUtils.isEmpty(weiqiangBean.fromname))
		{
			holder.layout_transform_send_name.setVisibility(View.VISIBLE);
			holder.transform_send_nameTv.setText(weiqiangBean.fromname);
		}
		else
		{
			holder.layout_transform_send_name.setVisibility(View.GONE);
		}
		
		holder.deleteImgV.setOnClickListener(mListener);
		holder.deleteImgV.setTag(weiqiangBean);
		holder.deleteImgV.setVisibility(View.GONE);
		
		holder.contentTv.setText(weiqiangBean.content);
		holder.zan_countTv.setText(weiqiangBean.like_count);
		holder.zan_countTv.setOnClickListener(mListener);
		holder.zan_countTv.setTag(weiqiangBean);
		
		holder.reply_countTv.setText(weiqiangBean.comment_count);
		holder.reply_countTv.setOnClickListener(mListener);
		holder.reply_countTv.setTag(weiqiangBean);
		
		holder.transform_send_countTv.setText(weiqiangBean.forward_count);
		holder.transform_send_countTv.setOnClickListener(mListener);
		holder.transform_send_countTv.setTag(weiqiangBean);
		
		holder.share_countTv.setText(weiqiangBean.share_count);
		holder.share_countTv.setOnClickListener(mListener);
		holder.share_countTv.setTag(weiqiangBean);
		
		
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
		public TextView zan_countTv;
		public TextView reply_countTv;
		public TextView transform_send_countTv;
		public TextView share_countTv;
		public LinearLayout layout_images;
		public ImageView oneImgV;
		public ImageView twoImgV;
		public ImageView threeImgV;
	}
}
