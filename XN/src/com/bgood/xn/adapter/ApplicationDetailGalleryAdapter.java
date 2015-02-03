package com.bgood.xn.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bgood.xn.R;

/**
 * 应用详情图片适配器
 */
public class ApplicationDetailGalleryAdapter extends BaseAdapter
{
	private Context m_context = null;
	private List<String> m_list = null;
	private LayoutInflater m_inflater = null;

	public ApplicationDetailGalleryAdapter(Context context, List<String> list)
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
			convertView = m_inflater.inflate(R.layout.layout_application_detail_item, null);
			holder.imageImgV = (ImageView) convertView.findViewById(R.id.application_detail_item_imgv_info);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder
	{
		ImageView imageImgV;
	}

}
