package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bgood.xn.R;
import com.bgood.xn.bean.GroupBean;
/**
 * 
 * @todo:群组
 * @date:2014-12-18 下午6:15:14
 * @author:hg_liuzl@163.com
 */
public class GroupAdapter extends KBaseAdapter {
	public GroupAdapter(List<?> mList, Activity mActivity) {
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
			holder.groupType = (ImageView) convertView.findViewById(R.id.iv_group_type);
			holder.summary = (TextView) convertView.findViewById(R.id.goup_item_summary);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		final GroupBean group = (GroupBean) mList.get(position);
		
		mImageLoader.displayImage(group.photo, holder.icon, options);
		
		holder.name.setText(group.name);
		
	   holder.groupType.setImageDrawable(mActivity.getResources().getDrawable(group.grouptype == 0? R.drawable.img_common_sex_male:R.drawable.img_common_sex_female));

		
		holder.summary.setText(group.intro);
		
		
		return convertView;
	}

	class Holder {
		public TextView summary;
		public TextView name;
		public ImageView icon;
		public ImageView groupType;
	}
}
