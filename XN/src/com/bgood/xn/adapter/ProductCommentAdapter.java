package com.bgood.xn.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * 我的动态适配器
 */
public class ProductCommentAdapter extends BaseAdapter
{
    private LayoutInflater m_inflater;
    private Context m_context;
    private List<String> m_list;
    
    public ProductCommentAdapter(Context context, List<String> list)
    {
        super();
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
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_product_comment_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.product_comment_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.product_comment_item_tv_name);
            holder.timeTv = (TextView) convertView.findViewById(R.id.product_comment_item_tv_time);
            holder.deleteImgV = (ImageView) convertView.findViewById(R.id.product_comment_item_imgv_delete);
            holder.contentTv = (TextView) convertView.findViewById(R.id.product_comment_item_tv_content);
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
        TextView contentTv;           // 评论数
    }
}
