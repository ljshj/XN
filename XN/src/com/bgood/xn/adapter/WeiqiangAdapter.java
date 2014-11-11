package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.utils.ToolUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
/**
 * 
 * @todo:微墙适配器
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class WeiqiangAdapter extends KBaseAdapter 
{
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public WeiqiangAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
		super(mList, mActivity, listener);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final Holder holder;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.weiqiang_item_layout, parent, false);
			holder.ivAuthorImg = (ImageView) convertView.findViewById(R.id.iv_author);
			holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_weiqiang_author);
			holder.distanceTv = (TextView) convertView.findViewById(R.id.tv_weiqiang_distance);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_weiqiang_time);
			holder.tvOldAuthorName = (TextView) convertView.findViewById(R.id.tv_content_fromuser);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvZanCount = (TextView) convertView.findViewById(R.id.tv_zan_count);
			holder.tvReplyCount = (TextView) convertView.findViewById(R.id.tv_comment_count);
			holder.tvTranspontCount = (TextView) convertView.findViewById(R.id.tv_transpont_count);
			holder.tvShareCount = (TextView) convertView.findViewById(R.id.tv_share_count);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final WeiQiangBean weiqiangBean = (WeiQiangBean) mList.get(position);
		
		mImageLoader.displayImage(weiqiangBean.photo,holder.ivAuthorImg, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				holder.ivAuthorImg.setAnimation(anim);
				anim.start();
			}
		});
		
		
		holder.tvAuthorName.setText(weiqiangBean.name);
		
		holder.distanceTv.setText(weiqiangBean.distance);
		holder.distanceTv.setVisibility(View.GONE);
		
		holder.tvTime.setText(ToolUtils.getFormatDate(weiqiangBean.date_time));
		
		if (!TextUtils.isEmpty(weiqiangBean.fromname))
		{
			holder.tvOldAuthorName.setVisibility(View.VISIBLE);
			holder.tvOldAuthorName.setText(weiqiangBean.fromname);
		}
		else
		{
			holder.tvOldAuthorName.setVisibility(View.GONE);
		}
		
		holder.ivDelete.setOnClickListener(mListener);
		holder.ivDelete.setTag(weiqiangBean);
		holder.ivDelete.setVisibility(View.GONE);
		
		holder.tvContent.setText(weiqiangBean.content);
		
		holder.tvZanCount.setText(weiqiangBean.like_count);
		holder.tvZanCount.setOnClickListener(mListener);
		holder.tvZanCount.setTag(weiqiangBean);
		
		holder.tvReplyCount.setText(weiqiangBean.comment_count);
		holder.tvReplyCount.setOnClickListener(mListener);
		holder.tvReplyCount.setTag(weiqiangBean);
		
		holder.tvTranspontCount.setText(weiqiangBean.forward_count);
		holder.tvTranspontCount.setOnClickListener(mListener);
		holder.tvTranspontCount.setTag(weiqiangBean);
		
		holder.tvShareCount.setText(weiqiangBean.share_count);
		holder.tvShareCount.setOnClickListener(mListener);
		holder.tvShareCount.setTag(weiqiangBean);
		
		
		return convertView;
	}

	final class Holder
	{
		public ImageView ivAuthorImg;
		public TextView tvAuthorName;
		public TextView distanceTv;
		public ImageView ivDelete;
		public TextView tvTime;
		public TextView tvOldAuthorName;
		public TextView tvContent;
		public TextView tvZanCount;
		public TextView tvReplyCount;
		public TextView tvTranspontCount;
		public TextView tvShareCount;
	}
}
