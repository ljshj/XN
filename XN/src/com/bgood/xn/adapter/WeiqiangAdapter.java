package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
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
			holder.ivAuthorImg = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvComments = (TextView) convertView.findViewById(R.id.tv_comments);
			holder.gridView = (GridView) convertView.findViewById(R.id.gv_show_img);
			
			
			holder.llTransArea = (LinearLayout) convertView.findViewById(R.id.ll_old_area);
			holder.tvOldAuthorName = (TextView) convertView.findViewById(R.id.tv_old_user);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.oldgridView = (GridView) convertView.findViewById(R.id.gv_old_show_img);
			
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
		
		holder.ivAuthorImg.setOnClickListener(mListener);
		holder.ivAuthorImg.setTag(weiqiangBean);
		
		holder.tvAuthorName.setText(weiqiangBean.name);
		holder.tvAuthorName.setOnClickListener(mListener);
		holder.tvAuthorName.setTag(weiqiangBean);
		
		
		holder.tvTime.setText(ToolUtils.getFormatDate(weiqiangBean.date_time));
		
		if(!TextUtils.isEmpty(weiqiangBean.fromname)){	//如果转发人存在
			holder.llTransArea.setVisibility(View.VISIBLE);
			holder.tvOldAuthorName.setText(weiqiangBean.fromname);
			holder.tvOldAuthorName.setOnClickListener(mListener);
			holder.tvOldAuthorName.setTag(weiqiangBean);
			holder.tvContent.setText(weiqiangBean.content);
			holder.tvComments.setText(weiqiangBean.Comments);
			showImgs(weiqiangBean.imgs,holder.oldgridView);
			holder.gridView.setVisibility(View.GONE);
			holder.tvContent.setVisibility(TextUtils.isEmpty(weiqiangBean.content)?View.GONE:View.VISIBLE);
			holder.tvComments.setVisibility(TextUtils.isEmpty(weiqiangBean.Comments)?View.GONE:View.VISIBLE);
		}else{
			holder.llTransArea.setVisibility(View.GONE);
			holder.tvComments.setText(weiqiangBean.content);
			holder.tvComments.setVisibility(TextUtils.isEmpty(weiqiangBean.content)?View.GONE:View.VISIBLE);
			holder.gridView.setVisibility(View.GONE);
			showImgs(weiqiangBean.imgs,holder.gridView);
		}
		
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
		public TextView tvTime;
		public TextView tvComments;
		
		public LinearLayout llTransArea;
		public TextView tvOldAuthorName;
		public TextView tvContent;
		public GridView gridView,oldgridView;
		
		public TextView tvZanCount;
		public TextView tvReplyCount;
		public TextView tvTranspontCount;
		public TextView tvShareCount;
	}
	
	/**处理九宫格图片**/
	@SuppressWarnings("null")
	private void showImgs(List<ImageBean> list,GridView gv){
		if(null==list && list.size()==0){	//如果没有图片
			gv.setVisibility(View.GONE);
		}else{
			gv.setVisibility(View.VISIBLE);
//			if(list.size()==1){	
//				gv.setNumColumns(1);
//			}else if(list.size()<=4){
//				gv.setNumColumns(2);
//			}else{
//				gv.setNumColumns(3);
//			}
			ImageAdapter adapter = new ImageAdapter(list, mActivity);
			gv.setAdapter(adapter);
		}
	}
}
