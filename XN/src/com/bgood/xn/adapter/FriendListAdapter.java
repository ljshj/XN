//package com.bgood.xn.adapter;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bgood.xn.R;
//
///**
// * @author ChenGuoqing 2014-6-27下午1:36:07
// */
//public class FriendListAdapter extends BaseExpandableListAdapter {
//	private Context context;
//	private LayoutInflater inflater;
//	private List<UserDTO> list;
//
//	public FriendListAdapter(Context context, List<UserDTO> list) {
//		this.context = context;
//		this.inflater = LayoutInflater.from(context);
//		this.list = list;
//	}
//
//	public String[] groups = { "我的好友", "新疆同学", "亲戚", "同事" };
//	public String[][] children = { { "胡算林", "张俊峰", "王志军", "二人" },
//			{ "李秀婷", "蔡乔", "别高", "余音" }, { "摊派新", "张爱明" }, { "马超", "司道光" } };
//
//	@Override
//	public Object getChild(int groupPosition, int childPosition) {
//		return list.get(childPosition);
//		// return children[groupPosition][childPosition];
//	}
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition) {
//		return childPosition;
//	}
//
//	@Override
//	public int getChildrenCount(int groupPosition) {
//		if (list == null)
//			return 0;
//		return list.size();
//	}
//
//	@Override
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//		HolderChild holderChild;
//		if (convertView == null) {
//			holderChild = new HolderChild();
//			convertView = inflater.inflate(
//					R.layout.item_message_friend_list_child, parent, false);
//			holderChild.icon = (ImageView) convertView
//					.findViewById(R.id.friend_list_child_item_icon);
//			holderChild.sex = (ImageView) convertView
//					.findViewById(R.id.friend_list_child_item_sex);
//			holderChild.name = (TextView) convertView
//					.findViewById(R.id.friend_list_child_item_name);
//			holderChild.summary = (TextView) convertView
//					.findViewById(R.id.friend_list_child_item_summary);
//			convertView.setTag(holderChild);
//		} else {
//			holderChild = (HolderChild) convertView.getTag();
//		}
//		holderChild.name.setText(list.get(childPosition).userId);
//		return convertView;
//	}
//
//	class HolderChild {
//		public ImageView icon;
//		public TextView name;
//		public ImageView sex;
//		public TextView summary;
//	}
//
//	@Override
//	public Object getGroup(int groupPosition) {
//		return groups[groupPosition];
//	}
//
//	@Override
//	public int getGroupCount() {
//		return groups.length;
//	}
//
//	@Override
//	public long getGroupId(int groupPosition) {
//		return groupPosition;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//		HolderGroup holderGroup;
//		if (convertView == null) {
//			holderGroup = new HolderGroup();
//			convertView = inflater.inflate(
//					R.layout.item_message_friend_list_group, parent, false);
//			holderGroup.indicator = (ImageView) convertView
//					.findViewById(R.id.friend_list_parent_item_icon);
//			holderGroup.groupName = (TextView) convertView
//					.findViewById(R.id.friend_list_parent_item_name);
//			holderGroup.number = (TextView) convertView
//					.findViewById(R.id.friend_list_parent_item_number);
//			convertView.setTag(holderGroup);
//		} else {
//			holderGroup = (HolderGroup) convertView.getTag();
//		}
//		if (isExpanded)
//			holderGroup.indicator
//					.setBackgroundResource(R.drawable.img_message_friend_expand);
//		else
//			holderGroup.indicator
//					.setBackgroundResource(R.drawable.img_message_friend_close);
//		holderGroup.groupName.setText(groups[groupPosition]);
//		holderGroup.number.setText("" + children[groupPosition].length);
//		return convertView;
//	}
//
//	class HolderGroup {
//		public ImageView indicator;
//		public TextView groupName;
//		public TextView number;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		return true;
//	}
//
//	@Override
//	public boolean hasStableIds() {
//		return true;
//	}
//
//}