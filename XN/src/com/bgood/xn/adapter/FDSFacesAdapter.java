package com.bgood.xn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bgood.xn.R;

public class FDSFacesAdapter extends BaseAdapter
{
	private String[] list;
	private Context context;
	String facesString;

	public FDSFacesAdapter(Context context, String[] list)
	{
		this.list = list;
		this.context = context;
		// this.self = this;
	}

	public int getCount()
	{
		return list.length;
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView textView;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.facestv, null); // 实例化convertView
			textView = (TextView) convertView.findViewById(R.id.faceview);
			convertView.setTag(textView);
		}

		else
		{
			textView = (TextView) convertView.getTag();
		}

		String facestr = list[position];
		CharSequence replacefacestr = FaceManager.getInstance().strToFaces(facestr);
		textView.setText(replacefacestr);

		return convertView;

	}

}
