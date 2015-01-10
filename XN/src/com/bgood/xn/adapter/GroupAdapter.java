package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bgood.xn.R;
import com.bgood.xn.bean.GroupBean;
import com.nostra13.universalimageloader.core.ImageLoader;
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
			holder.tvGroupType = (TextView) convertView.findViewById(R.id.tv_group_type);
			holder.summary = (TextView) convertView.findViewById(R.id.goup_item_summary);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		final GroupBean group = (GroupBean) mList.get(position);
		
		if(null == group){
			return null;
		}
		
		// ImageLoader.getInstance().displayImage(group.photo, holder.icon, options);
		
		holder.icon.setImageResource(R.drawable.group_icon);
		holder.name.setText(group.name);

	    if("0".equals(group.grouptype)){	//固定群
	    	holder.tvGroupType.setText("固定群");
	    	holder.tvGroupType.setBackgroundResource(R.drawable.icon_fixation_group_bg);
	    }else if("1".equals(group.grouptype)){	//临时群
	    	holder.tvGroupType.setText("临时群");
	    	holder.tvGroupType.setBackgroundResource(R.drawable.icon_temp_group_bg);
	    }
	    
	    
		
		holder.summary.setText(group.intro);
		
		
		return convertView;
	}

	final class Holder {
		public TextView summary;
		public TextView name;
		public ImageView icon;
		public TextView tvGroupType;
	}
}
