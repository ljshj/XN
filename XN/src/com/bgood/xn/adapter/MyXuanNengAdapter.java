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
 * 我的炫能列表适配器
 */
public class MyXuanNengAdapter extends BaseAdapter
{
    private LayoutInflater m_inflater = null;
    private Context m_context = null;
    private List<String> m_list = null;

    public MyXuanNengAdapter(Context context, List<String> list)
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
            convertView = m_inflater.inflate(R.layout.layout_my_xuanneng_gv_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.my_xuanneng_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.my_xuanneng_item_tv_name);
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
        ImageView iconImgV;  // 图片
        TextView nameTv;     // 名字
    }
}
