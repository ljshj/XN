package com.bgood.xn.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.squareup.picasso.Picasso;

/**
 * @author ChenGuoqing 2014-7-9下午4:12:46
 */
public class WeiqiangCommentDetailAdapter extends BaseAdapter
{
    
    protected Context m_context;
    protected LayoutInflater m_inflater;
    protected List<ReplyCommentDTO> m_list;

	/**
	 * @param context
	 * @param list
	 */
	public WeiqiangCommentDetailAdapter(Context context, List<ReplyCommentDTO> list)
	{
	    this.m_context = context;
        this.m_list = list;
        this.m_inflater = LayoutInflater.from(m_context);
	}
	
	@Override
    public int getCount()
    {
        return m_list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    Holder holder;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = m_inflater.inflate(R.layout.item_weiqiang_detail, parent, false);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name);
            holder.contentTv = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else
        {
            holder = (Holder) convertView.getTag();
        }
        
        ReplyCommentDTO replyCommentDTO = m_list.get(position);
        if (replyCommentDTO.senderIcon != null && !replyCommentDTO.senderIcon.equals(""))
        {
            Picasso.with(m_context).load(replyCommentDTO.senderIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
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
