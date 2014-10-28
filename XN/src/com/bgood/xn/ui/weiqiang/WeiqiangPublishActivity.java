package com.bgood.xn.ui.weiqiang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKLocationManager;
import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangPublishAdapter;
import com.bgood.xn.bean.UserBean.UserState;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:发微墙
 * @date:2014-10-24 上午10:19:23
 * @author:hg_liuzl@163.com
 */

public class WeiqiangPublishActivity extends BaseActivity 
{
	private GridView gridview_images;
	private WeiqiangPublishAdapter adapter;
	private EditText comment_content;
    
    

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_publish);
		(new TitleBar(mActivity)).initTitleBar("发微墙");
		initViews();
	}

	
	private void initViews()
	{
		gridview_images = (GridView) findViewById(R.id.gridview_images);
		gridview_images.setAdapter(adapter);
		comment_content = (EditText) findViewById(R.id.comment_content);
	}
}
