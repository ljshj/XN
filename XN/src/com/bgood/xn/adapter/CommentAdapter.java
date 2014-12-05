package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.CommentBean;
import com.bgood.xn.utils.ToolUtils;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @todo:微墙评论适配器
 * @date:2014-10-22 上午9:43:36
 * @author:hg_liuzl@163.com
 */

public class CommentAdapter extends KBaseAdapter
{
	public CommentAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	   final Holder holder;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.weiqiang_comment_item, parent, false);
            holder.ivComment = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tvCommentAuthor = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.tvCommentTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        } else
        {
            holder = (Holder) convertView.getTag();
        }
        
       final CommentBean wComment = (CommentBean) mList.get(position);
        
        mImageLoader.displayImage(wComment.photo,holder.ivComment, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				holder.ivComment.setAnimation(anim);
				anim.start();
			}
		});
        
        holder.tvCommentAuthor.setText(wComment.name);
        holder.tvCommentTime.setText(ToolUtils.getFormatDate(wComment.commenttime));
        holder.tvCommentContent.setText(wComment.content);
        
		return convertView;
	}

	final class Holder
    {
        ImageView ivComment;
        TextView tvCommentAuthor;
        TextView tvCommentTime;
        TextView tvCommentContent;
    }
}
