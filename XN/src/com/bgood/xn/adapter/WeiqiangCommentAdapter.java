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
            convertView = mInflater.inflate(R.layout.item_weiqiang_detail, parent, false);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name);
            holder.contentTv = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else
        {
            holder = (Holder) convertView.getTag();
        }
        
       final WeiqiangCommentBean wComment = (WeiqiangCommentBean) mList.get(position);
        if (TextUtils.isEmpty(wComment.photo))
        {
            Picasso.with(mActivity).load(wComment.photo).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
        }
        holder.nameTv.setText(wComment.name);
        holder.contentTv.setText(wComment.content);
        
		return convertView;
	}

	class Holder
    {
        public ImageView iconImgV;
        public TextView nameTv;
        public TextView contentTv;
    }
}
