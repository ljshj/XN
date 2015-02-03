package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.AddressBean;
import com.bgood.xn.utils.PingYinUtil;

/**
 * 城市列表适配器
 */
public class CityAdapter extends KBaseAdapter
{
	public CityAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		if (null == convertView)
		{
			/* new views */
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_city_list_item, null);
			holder.m_alpha = (TextView) convertView.findViewById(R.id.city_list_item_tv_alpha);
			holder.m_name = (TextView) convertView.findViewById(R.id.city_list_item_tv_name);
			holder.m_icon = (ImageView) convertView.findViewById(R.id.imageView);
			holder.item_hint = (ImageView) convertView.findViewById(R.id.city_list_item_imgv_hint);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final AddressBean addressDTO = (AddressBean) mList.get(position);
		holder.m_name.setText(addressDTO.regionName);
		holder.item_hint.setVisibility(View.VISIBLE);
		String currentStr = PingYinUtil.getAlpha(PingYinUtil.getPingYin(addressDTO.regionName));
		String previewStr = "";
		if((position - 1)>=0){
			AddressBean preViewAddress = (AddressBean) mList.get(position - 1);
			previewStr = PingYinUtil.getAlpha(PingYinUtil.getPingYin(preViewAddress.regionName));
		}
		/**
		 * 判断显示#、A-Z的TextView隐藏与可见
		 */
		if (previewStr.equals(currentStr))
		{ 
			holder.m_alpha.setVisibility(View.GONE);
		} else
		{
			// 当前联系人的sortKey！=上一个联系人的sortKey，说明当前联系人是新组。
			holder.m_alpha.setVisibility(View.VISIBLE);
			holder.m_alpha.setText(currentStr);
		}


		return convertView;
	}

	public class ViewHolder
	{
		TextView m_alpha;
		ImageView m_icon;
		TextView m_name;// name
		ImageView item_hint;// name
	};

}
