package com.bgood.xn.utils.city;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.adapter.KBaseAdapter;

/**
 * @todo:城市适配类
 * @date:2014-10-31 下午1:53:03
 * @author:hg_liuzl@163.com
 */
public class CityAdapter extends KBaseAdapter {
	public CityAdapter(List<CityBean> mList, Activity mActivity) {
		super(mList, mActivity);
	}
	
	ViewHolder viewHolder;
	public View getView(int position, View convertView, ViewGroup parent)
	{ 
		
		if(null == convertView)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_address_select, null);
			viewHolder.tView = (TextView) convertView.findViewById(R.id.option_tv);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final CityBean item = (CityBean) mList.get(position);
		viewHolder.tView.setText(item.getName());
		
		return convertView;
	}
	
	final class ViewHolder{
		TextView tView;
	}
	
}