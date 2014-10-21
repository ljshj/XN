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
 * 产品编辑列表适配器
 */
public class ProductEditListAdapter extends BaseAdapter
{
    private LayoutInflater m_inflater;
    private Context m_context;
    private List<ProductDTO> m_list;
    
    public ProductEditListAdapter(Context context, List<ProductDTO> list)
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
            convertView = m_inflater.inflate(R.layout.layout_product_edit_list_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.product_edit_list_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_name);
            holder.priceTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_price);
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
        TextView priceTv;             // 价格
    }
}
