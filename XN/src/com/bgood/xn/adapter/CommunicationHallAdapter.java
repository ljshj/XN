package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * 交流厅
 */
public class CommunicationHallAdapter extends KBaseAdapter{
	public CommunicationHallAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_group_main, parent, false);
			holder.icon = (ImageView) convertView.findViewById(R.id.goup_item_icon);
			holder.name = (TextView) convertView.findViewById(R.id.goup_item_name);
			holder.summary = (TextView) convertView.findViewById(R.id.goup_item_summary);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}

	class Holder {
		public TextView summary;
		public TextView name;
		public ImageView icon;
	}

}
