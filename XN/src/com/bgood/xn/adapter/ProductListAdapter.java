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
import com.squareup.picasso.Picasso;

/**
 * 橱窗中产品所有页面的适配器
 */
public class ProductListAdapter extends BaseAdapter
{
	private Context m_context;
    private LayoutInflater m_inflater;
    private List<ProductDTO> m_list;
    
    public ProductListAdapter(Context context, List<ProductDTO> list)
    {
        super();
        this.m_context = context;
        this.m_inflater = LayoutInflater.from(m_context);
        this.m_list = list;
    }
    
	@Override
	public int getCount()
	{
		return m_list.size();
	}

	@Override
	public ProductDTO getItem(int position)
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
		ViewHolder holder = null;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = m_inflater.inflate(R.layout.layout_product_list_item, null);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.product_list_item_imgv_icon);
			holder.nameTv = (TextView) convertView.findViewById(R.id.product_list_item_tv_name);
			holder.priceTv = (TextView) convertView.findViewById(R.id.product_list_item_tv_price);
			convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
		
		ProductDTO productDTO = m_list.get(position);
		Picasso.with(m_context).load(productDTO.productSmallIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
		holder.nameTv.setText(productDTO.productName);
		holder.priceTv.setText(productDTO.productPrice);
		
		return convertView;
	}

	class ViewHolder
	{
		ImageView iconImgV;  // 商品图片
		TextView nameTv;     // 商品名
		TextView priceTv;    // 商品价格
	}
}
