package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.utils.ToolUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @todo:未审核的幽默秀
 * @date:2014-12-2 上午10:33:43
 * @author:hg_liuzl@163.com
 */
public class JokeUnVerifyAdapter extends KBaseAdapter {

	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public JokeUnVerifyAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
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
			convertView = mInflater.inflate(R.layout.item_joke_unverify, parent, false);
			holder.ivAuthorImg = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.tvAuthorName = (TextView) convertView.findViewById(R.id.tv_nick);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.gridView = (GridView) convertView.findViewById(R.id.gv_show_img);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvJokeStatus = (TextView) convertView.findViewById(R.id.tv_joke_status);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final JokeBean jokeBean = (JokeBean) mList.get(position);
		
		mImageLoader.displayImage(jokeBean.photo,holder.ivAuthorImg, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				holder.ivAuthorImg.setAnimation(anim);
				anim.start();
			}
		});
		
		
		holder.tvAuthorName.setText(jokeBean.username);
		holder.tvTime.setText(ToolUtils.getFormatDate(jokeBean.date_time));
		if(jokeBean.status == 1){
			holder.tvJokeStatus.setText("审核中");
			holder.tvJokeStatus.setBackgroundColor(mActivity.getResources().getColor(R.color.orange_normal));
		}else{
			holder.tvJokeStatus.setText("未通过审核");
			holder.tvJokeStatus.setBackgroundColor(mActivity.getResources().getColor(R.color.red));
		}
		
		holder.tvContent.setText(jokeBean.content);
		
		showImgs(jokeBean.imgs,holder.gridView);
		
		return convertView;
	}

	final class Holder
	{
		public ImageView ivAuthorImg;
		public TextView tvAuthorName;
		public TextView tvTime;
		public TextView tvJokeStatus;
		public TextView tvContent;
		public GridView gridView;
		
	}
	
	/**处理九宫格图片**/
	@SuppressWarnings("null")
	private void showImgs(List<ImageBean> list,GridView gv){
		if(null==list && list.size()==0){	//如果没有图片
			gv.setVisibility(View.GONE);
		}else{
			gv.setVisibility(View.VISIBLE);
			ImageAdapter adapter = new ImageAdapter(list, mActivity);
			gv.setAdapter(adapter);
		}
	}
}
	