package com.bgood.xn.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * 本地应用程序适配器
 */
public class ApplicationManagementAdapter extends BaseAdapter
{
    private LayoutInflater m_inflater = null;
    private Context m_context = null;
    private List<String> m_list = null;
    
    

    public ApplicationManagementAdapter(Context context, List<String> list)
    {
        super();
        this.m_context = context;
        this.m_list = list;
        this.m_inflater = LayoutInflater.from(m_context);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return m_list.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_application_management_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.application_management_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.application_management_item_tv_name);
            holder.sizeTv = (TextView) convertView.findViewById(R.id.application_management_item_tv_size);
            holder.infoTv = (TextView) convertView.findViewById(R.id.application_management_item_tv_info);
            holder.openBtn = (Button) convertView.findViewById(R.id.application_management_item_btn_open);
            
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
        ImageView iconImgV;   // 应用图标
        TextView nameTv;      // 应用名
        TextView sizeTv;      // 应用大小
        TextView infoTv;      // 应用信息
        Button openBtn;       // 打开软件
    }
}
