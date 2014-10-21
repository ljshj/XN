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
 * 省份列表适配器
 */
public class PrivinceAdapter extends BaseAdapter
{
	private Context m_context;
	private List<AddressDTO> m_list;
	private LayoutInflater m_inflater;

	public PrivinceAdapter(Context context, List<AddressDTO> list)
	{
		this.m_context = context;
		this.m_list = list;
		m_inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return m_list.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return m_list.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return arg0;
	}

	/* contact views */
	public View getContactView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		if (null == convertView)
		{
			/* new views */
			holder = new ViewHolder();
			convertView = m_inflater.inflate(R.layout.layout_privince_list_item, null);
			holder.m_alpha = (TextView) convertView.findViewById(R.id.privince_list_item_tv_alpha);
			holder.m_name = (TextView) convertView.findViewById(R.id.privince_list_item_tv_name);
			holder.m_icon = (ImageView) convertView.findViewById(R.id.imageView);
			holder.item_hint = (ImageView) convertView.findViewById(R.id.privince_list_item_imgv_hint);
			// keywordsEditText = searchCon.m_keywords;
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		AddressDTO addressDTO = m_list.get(position);
		if (addressDTO.getRegionName() == null || addressDTO.getRegionName().trim().length() <= 0)
			holder.m_name.setText(addressDTO.getRegionName());
		else
			holder.m_name.setText(addressDTO.getRegionName());
		holder.item_hint.setVisibility(View.VISIBLE);
		String currentStr = ZZPingYinUtil.getAlpha(ZZPingYinUtil.getPingYin(addressDTO.getRegionName()));
		String previewStr = (position - 1) >= 0 ? ZZPingYinUtil.getAlpha(ZZPingYinUtil.getPingYin(m_list.get(position - 1).getRegionName())) : " ";
		
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		convertView = getContactView(position, convertView, parent);
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
