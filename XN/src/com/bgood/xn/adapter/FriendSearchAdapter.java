//package com.bgood.xn.adapter;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bgood.xn.R;
//
///**
// * @author ChenGuoqing 2014-6-26下午3:28:01
// */
//public class FriendSearchAdapter extends BaseAdapterParent<UserDTO>
//{
//
//	public FriendSearchAdapter(Context context, List<UserDTO> list)
//	{
//		super(context, list);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//		Holder holder;
//		if (convertView == null)
//		{
//			holder = new Holder();
//			convertView = inflater.inflate(R.layout.item_friend_seach, parent, false);
//			holder.icon = (ImageView) convertView.findViewById(R.id.friend_search_item_icon);
//			holder.name = (TextView) convertView.findViewById(R.id.friend_search_item_name);
//			holder.summary = (TextView) convertView.findViewById(R.id.friend_search_item_summary);
//			convertView.setTag(holder);
//		} else
//		{
//			holder = (Holder) convertView.getTag();
//		}
//		return convertView;
//	}
//
//	class Holder
//	{
//		public TextView summary;
//		public TextView name;
//		public ImageView icon;
//	}
//}
