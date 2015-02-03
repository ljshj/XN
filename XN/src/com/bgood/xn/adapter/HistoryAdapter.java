package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * 
 * @todo:搜索历史适配类
 * @date:2014-11-21 下午2:15:28
 * @author:hg_liuzl@163.com
 */
public class HistoryAdapter extends KBaseAdapter
{


	public HistoryAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	public void clear()
	{
		this.mList.clear();
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Holder holder = null;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_history, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		
		final String searchText = (String) mList.get(position);
		holder.text.setText(searchText);
		return convertView;
	}

	class Holder
	{
		public TextView text;
	}
}
