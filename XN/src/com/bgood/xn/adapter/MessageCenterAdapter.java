package com.bgood.xn.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * @author ChenGuoqing 2014-6-26下午3:06:37
 */
public class MessageCenterAdapter extends BaseAdapterParent<MessageCenterMessageDTO> {

	public MessageCenterAdapter(Context context, List<MessageCenterMessageDTO> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_message_message_center, parent, false);
			holder.icon = (ImageView) convertView.findViewById(R.id.message_usercenter_item_icon);
			holder.name = (TextView) convertView.findViewById(R.id.message_usercenter_item_name);
			holder.content = (TextView) convertView.findViewById(R.id.message_usercenter_item_content);
			holder.time = (TextView) convertView.findViewById(R.id.message_usercenter_item_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}

	class Holder {
		public ImageView icon;
		public TextView name;
		public TextView content;
		public TextView time;
	}

}
