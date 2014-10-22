package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.widget.CMyGridView;

/**
 * 我的动态适配器
 */
public class DynamicAdapter extends KBaseAdapter
{


    public DynamicAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_dynamic_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.dynamic_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.dynamic_item_tv_name);
            holder.timeTv = (TextView) convertView.findViewById(R.id.dynamic_item_tv_time);
            holder.deleteImgV = (ImageView) convertView.findViewById(R.id.dynamic_item_imgv_delete);
            holder.imageGv = (CMyGridView) convertView.findViewById(R.id.dynamic_item_gv_list);
            holder.praiseLl = (LinearLayout) convertView.findViewById(R.id.dynamic_item_ll_praise);
            holder.praiseTv = (TextView) convertView.findViewById(R.id.dynamic_item_tv_praise);
            holder.commentLl = (LinearLayout) convertView.findViewById(R.id.dynamic_item_ll_comment);
            holder.commentTv = (TextView) convertView.findViewById(R.id.dynamic_item_tv_comment);
            holder.relayLl = (LinearLayout) convertView.findViewById(R.id.dynamic_item_ll_relay);
            holder.relayTv = (TextView) convertView.findViewById(R.id.dynamic_item_tv_relay );
            holder.shareLl = (LinearLayout) convertView.findViewById(R.id.dynamic_item_ll_share);
            holder.shareTv = (TextView) convertView.findViewById(R.id.dynamic_item_tv_share);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        
        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImgV;           // 头像
        TextView nameTv;              // 名字
        TextView timeTv;              // 发表时间
        ImageView deleteImgV;         // 删除
        CMyGridView imageGv;          // 图片展示
        LinearLayout praiseLl;        // 点赞
        TextView praiseTv;            // 点赞数
        LinearLayout commentLl;       // 评论
        TextView commentTv;           // 评论数
        LinearLayout relayLl;         // 转发
        TextView relayTv;             // 转发数
        LinearLayout shareLl;         // 分享
        TextView shareTv;             // 分享数
    }
}
