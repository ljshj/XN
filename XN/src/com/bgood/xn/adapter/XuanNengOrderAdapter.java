package com.bgood.xn.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.squareup.picasso.Picasso;

/**
 * @author ChenGuoqing 2014-7-9上午11:27:04
 */
public class XuanNengOrderAdapter extends BaseAdapter implements XuannengMessageManagerInterface
{

	protected Context m_context;
	protected LayoutInflater m_inflater;
	protected List<CommentsDTO> m_list;
	private int m_delPosition;
	
	ShareManager   m_shareManager;
	
	XuannengMessageManager messageManager = XuannengMessageManager.getInstance();
	

	/**
	 * @param context
	 * @param list
	 */
	public XuanNengOrderAdapter(Context context, List<CommentsDTO> list)
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
			convertView = m_inflater.inflate(R.layout.item_weiqiang_comment, parent, false);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_icon);
			holder.nameTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_name);
			holder.distanceTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_distance);
			holder.deleteImgV = (ImageView) convertView.findViewById(R.id.weiqiang_detail_item_imgv_delete);
			holder.timeTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_time);
			holder.layout_transform_send_name = (LinearLayout) convertView.findViewById(R.id.layout_transform_send);
			holder.transform_send_nameTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_transform_send_by_name);
			holder.contentTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_content);
			holder.layout_zan_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_zan_count);
			holder.zan_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_zan_count);
			holder.layout_reply_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_reply_count);
			holder.reply_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_reply_count);
			holder.layout_transform_send_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_transform_send_count);
			holder.transform_send_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_transform_send_count);
			holder.layout_share_count = (LinearLayout) convertView.findViewById(R.id.weiqiang_detail_item_ll_share_count);
			holder.share_countTv = (TextView) convertView.findViewById(R.id.weiqiang_detail_item_tv_share_count);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final CommentsDTO commentsDTO = m_list.get(position);
		if (commentsDTO.senderIcon != null && !commentsDTO.senderIcon.equals(""))
		{
			Picasso.with(m_context).load(commentsDTO.senderIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
		}
		holder.nameTv.setText(commentsDTO.senderName);
		holder.distanceTv.setText(commentsDTO.distance);
		holder.timeTv.setText(commentsDTO.sendTime);
		
		if (commentsDTO.from_name != null && !commentsDTO.from_name.equals(""))
		{
			holder.layout_transform_send_name.setVisibility(View.VISIBLE);
			holder.transform_send_nameTv.setText(commentsDTO.from_name);
		}
		else
		{
			holder.layout_transform_send_name.setVisibility(View.GONE);
		}
		
		holder.contentTv.setText(commentsDTO.content);
		holder.zan_countTv.setText(commentsDTO.like_count + "");
		holder.reply_countTv.setText(commentsDTO.revertCount + "");
		holder.transform_send_countTv.setText(commentsDTO.forward_count + "");
		holder.share_countTv.setText(commentsDTO.share_count + "");
		
		m_delPosition = position;
		
		convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(m_context, XuanNengDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("comment", commentsDTO);
                intent.putExtras(bundle);
                m_context.startActivity(intent);
            }
        });
		
		holder.deleteImgV.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                WindowUtil.getInstance().progressDialogShow(m_context, "删除中...");
                messageManager.registerObserver(XuanNengOrderAdapter.this);
//                messageManager.deleteWeiqiang(commentsDTO.commentId);
            }
        });
		
		/*// 分享
		holder.layout_share_count.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                m_shareManager = new ShareManager((XuannengHumorOrderActivity)m_context);

                m_shareManager.shareContentInit("adg", "掌上有礼", "", "", "1", "d");
                m_shareManager.openShare();
            }
        });
		
		
		
		//点赞
		holder.layout_zan_count.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                WindowUtil.getInstance().progressDialogShow(m_context, "赞中...");
                messageManager.registerObserver(XuanNengOrderAdapter.this);
                messageManager.zanXuanneng(commentsDTO.commentId);
            }
        });*/
		
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
		public LinearLayout layout_images;
		public LinearLayout layout_zan_count;
		public TextView zan_countTv;
		public LinearLayout layout_reply_count;
		public TextView reply_countTv;
		public LinearLayout layout_transform_send_count;
		public TextView transform_send_countTv;
		public LinearLayout layout_share_count;
		public TextView share_countTv;
	}

	/*@Override
    public void deleteWeiqiangCB(Reulst result_state)
    {
        WindowUtil.getInstance().DismissAllDialog();
        messageManager.unregisterObserver(XuanNengRandomAdapter.this);
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
            Toast.makeText(m_context, "删除成功", Toast.LENGTH_SHORT).show();
            m_list.remove(m_delPosition);
            notifyDataSetChanged();
        } else
        {
            Toast.makeText(m_context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    public void getXuannengJokeListCB(Reulst result_state, List<CommentsDTO> list)
    {
    }

    @Override
    public void getXuannengRankingCB(Reulst result_state, List<CommentsDTO> list)
    {
    }

    @Override
    public void publishXuannengCB(Reulst result_state)
    {
    }

    @Override
    public void relayXuannengCB(Reulst result_state)
    {
    }

    @Override
    public void shareXuannengCB(Reulst result_state)
    {
    }

    @Override
    public void commentaryXuannengCB(Reulst result_state)
    {
    }

    @Override
    public void zanXuannengCB(Reulst result_state)
    {
        WindowUtil.getInstance().DismissAllDialog();
        messageManager.unregisterObserver(XuanNengOrderAdapter.this);
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
            m_list.get(m_delPosition).like_count += 1;
            notifyDataSetChanged();
        } else
        {
            Toast.makeText(m_context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getXuannengPulishListCB(Reulst result_state, List<CommentsDTO> list)
    {
    }
    
    @Override
    public void getJokeCB(Reulst result_state, CommentsDTO commentsDTO)
    {
    }

    @Override
    public void deleteXuannengCB(Reulst result_state)
    {
        
    }
}
