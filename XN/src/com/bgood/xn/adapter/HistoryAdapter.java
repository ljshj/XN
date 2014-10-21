package com.bgood.xn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * @author ChenGuoqing 2014-7-18下午4:46:59
 */
public class HistoryAdapter extends BaseAdapter
{

	private Context context;
	private String[] strings;
	private LayoutInflater inflater;

	/**
	 * @param context
	 * @param list
	 */
	public HistoryAdapter(Context context, String[] strings)
	{
		this.context = context;
		this.strings = strings;
		inflater = LayoutInflater.from(context);
	}

	public void clear()
	{
		this.strings = null;
	};

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Holder holder = null;
		if (convertView == null)
		{
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_history, parent, false);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else
		{
			holder = (Holder) convertView.getTag();
		}
		holder.text.setText(strings[position]);
		return convertView;
	}

	class Holder
	{
		public TextView text;
	}

	@Override
	public int getCount()
	{
		if (strings == null)
			return 0;
		return strings.length;
	}

	@Override
	public String getItem(int position)
	{
		return strings[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}
