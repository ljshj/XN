package com.bgood.xn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.ActionView;
import com.bgood.xn.view.photoview.ImagePagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * 
 * @todo:炫能幽默秀适配器
 * @date:2014-10-22 上午9:41:14
 * @author:hg_liuzl@163.com
 */
public class JokeAdapter extends KBaseAdapter 
{

	public JokeAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
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
			
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final JokeBean jokeBean = (JokeBean) mList.get(position);
		 ImageLoader.getInstance().displayImage(jokeBean.photo,holder.ivAuthorImg);
		
		holder.tvAuthorName.setText(jokeBean.username);
		
		
		holder.tvTime.setText(ToolUtils.getFormatDate(jokeBean.date_time));
		
		if(!TextUtils.isEmpty(jokeBean.fromname)){	//如果转发人存在
			holder.llTransArea.setVisibility(View.VISIBLE);
			holder.tvOldAuthorName.setText(jokeBean.fromname);
			holder.tvContent.setText(jokeBean.content);
			holder.tvComments.setText(jokeBean.Comments);
			showImgs(jokeBean.imgs,holder.oldgridView);
			holder.gridView.setVisibility(View.GONE);
			holder.tvContent.setVisibility(TextUtils.isEmpty(jokeBean.content)?View.GONE:View.VISIBLE);
			holder.tvComments.setVisibility(TextUtils.isEmpty(jokeBean.Comments)?View.GONE:View.VISIBLE);
		}else{
			holder.llTransArea.setVisibility(View.GONE);
			holder.tvComments.setText(jokeBean.content);
			holder.tvComments.setVisibility(TextUtils.isEmpty(jokeBean.content)?View.GONE:View.VISIBLE);
			holder.gridView.setVisibility(View.GONE);
			showImgs(jokeBean.imgs,holder.gridView);
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
		
		public ActionView avZan,avReply,avTranspnt,avShare;
	}
	
	/**处理九宫格图片**/
	@SuppressWarnings("null")
	private void showImgs(final List<ImageBean> list,GridView gv){
		if (list == null || list.size() == 0) { // 没有图片资源就隐藏GridView
			gv.setVisibility(View.GONE);
		} else {
			gv.setVisibility(View.VISIBLE);
			gv.setAdapter(new NoScrollGridAdapter(list, mActivity));
		}
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 点击回帖九宫格，查看大图
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				imageBrower(position, list);
			}
		});
	}
	
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, List<ImageBean> imgList) {
		ArrayList<String> arrays = new ArrayList<String>();
		for(ImageBean img:imgList){
			arrays.add(img.img);
		}
		Intent intent = new Intent(mActivity, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arrays);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mActivity.startActivity(intent);
	}
}
