package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
/**
 * 
 * @todo:产品编辑适配器
 * @date:2014-10-22 上午9:34:39
 * @author:hg_liuzl@163.com
 */
public class ProductEditListAdapter extends KBaseAdapter
{
    public ProductEditListAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_product_edit_list_item, null);
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
