package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ReplyCommentBean;
import com.squareup.picasso.Picasso;

/**
 * @todo:微墙评论
 * @date:2014-10-22 上午9:43:36
 * @author:hg_liuzl@163.com
 */

public class WeiqiangCommentDetailAdapter extends KBaseAdapter
{
	public WeiqiangCommentDetailAdapter(List<?> mList, Activity mActivity) {
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
        
       final ReplyCommentBean replyCommentDTO = (ReplyCommentBean) mList.get(position);
        if (replyCommentDTO.senderIcon != null && !replyCommentDTO.senderIcon.equals(""))
        {
            Picasso.with(mActivity).load(replyCommentDTO.senderIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
        }
        holder.nameTv.setText(replyCommentDTO.senderName);
        holder.contentTv.setText(replyCommentDTO.content);
        
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
    }

}
