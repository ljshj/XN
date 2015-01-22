package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.system.BGApp;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 橱窗中产品所有页面的适配器
 */
public class ShowcaseAllProductAdapter extends KBaseAdapter
{
	
	public ShowcaseAllProductAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_showcase_all_product_item, null);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.showcase_all_product_item_imgv_icon);
			holder.nameTv = (TextView) convertView.findViewById(R.id.showcase_all_product_item_tv_name);
			holder.priceTv = (TextView) convertView.findViewById(R.id.showcase_all_product_item_tv_price);
			convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
		
		final ProductBean productDTO = (ProductBean) mList.get(position);
		
		BGApp.getInstance().setImageSqure(productDTO.img,holder.iconImgV);
		
		holder.nameTv.setText(productDTO.product_name);
		holder.priceTv.setText(productDTO.getPrice());
		return convertView;
	}

	class ViewHolder
	{
		ImageView iconImgV;  // 商品图片
		TextView nameTv;     // 商品名
		TextView priceTv;    // 商品价格
	}
}
