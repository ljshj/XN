package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.WeiqiangCommentBean;
import com.bgood.xn.utils.ToolUtils;
import com.squareup.picasso.Picasso;

/**
 * @todo:微墙评论适配器
 * @date:2014-10-22 上午9:43:36
 * @author:hg_liuzl@163.com
 */

public class WeiqiangCommentAdapter extends KBaseAdapter
{
	public WeiqiangCommentAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    Holder holder;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.weiqiang_comment_item, parent, false);
            holder.ivComment = (ImageView) convertView.findViewById(R.id.iv_comment_author);
            holder.tvCommentAuthor = (TextView) convertView.findViewById(R.id.tv_comment_author);
            holder.tvCommentTime = (TextView) convertView.findViewById(R.id.tv_comment_time);
            holder.tvCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        } else
        {
            holder = (Holder) convertView.getTag();
        }
        
       final WeiqiangCommentBean wComment = (WeiqiangCommentBean) mList.get(position);
        if (TextUtils.isEmpty(wComment.photo))
        {
            Picasso.with(mActivity).load(wComment.photo).placeholder(R.drawable.icon_default).error(R.drawable.icon_default).into( holder.ivComment);
        }
        holder.tvCommentAuthor.setText(wComment.name);
        holder.tvCommentTime.setText(ToolUtils.getFormatDate(wComment.commenttime));
        holder.tvCommentContent.setText(wComment.content);
        
		return convertView;
	}

	class Holder
    {
        public ImageView ivComment;
        public TextView tvCommentAuthor;
        public TextView tvCommentTime;
        public TextView tvCommentContent;
    }
}
