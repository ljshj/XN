package com.bgood.xn.ui.user.info;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bgood.xn.R;
import com.bgood.xn.adapter.PrivinceAdapter;
import com.bgood.xn.bean.AddressBean;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.PingYinUtil;
import com.bgood.xn.view.LoadingProgress;
import com.bgood.xn.widget.TitleBar;
import com.bgood.xn.widget.ZZPrivinceQuickAlphabeticBar;

/**
 * 省份选择页面
 */
public class PrivinceActivity extends BaseActivity implements OnItemClickListener
{
	private ListView m_privinceLv = null;
	private ZZPrivinceQuickAlphabeticBar m_alphaBar = null;
	
	private PrivinceAdapter m_privinceAdapter;
    
    private List<AddressBean> m_privinceList = null;    // 省份/直辖市列表
    
    private String[] searchsHead;
	private String[] searchs;
	private String[] searchsCH;
	
	private int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_privince);
		
		index = getIntent().getIntExtra("index", 0);
		(new TitleBar(mActivity)).initTitleBar(index == 0 ?"家乡(省)":"所在地(省)");
		
		findView();
		
		new GetAddressListTask(PrivinceActivity.this).execute(m_privinceLv);

	}

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		m_privinceLv = (ListView) findViewById(R.id.privince_lv_privince);
		m_privinceLv.setOnItemClickListener(this);
		m_alphaBar = (ZZPrivinceQuickAlphabeticBar) findViewById(R.id.privince_qab_fast_scroller);
	}
	
    /**
	 * 通过异步加载类得到地址列表
	 */
	private class GetAddressListTask extends AsyncTask<ListView, String, List<AddressBean>>
	{
		private Context context;
		
		public GetAddressListTask(Context context)
		{
			super();
			this.context = context;
		}
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			LoadingProgress.getInstance().show(context);
		}

		@Override
		protected List<AddressBean> doInBackground(ListView... params)
		{
			data();
			for (int i = 0; i < m_privinceList.size(); i++)
	        {
	        	Collections.sort(m_privinceList, m_privinceList.get(i));
	        }
			
			initSearchDate();
			
			return m_privinceList;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(List<AddressBean> result)
		{
			super.onPostExecute(result);
			LoadingProgress.getInstance().dismiss();
			setAdapter(result);
		}
	}
    
    private void initSearchDate()
	{
		if (m_privinceList == null)
			return;
		searchs = new String[m_privinceList.size()];
		searchsHead = new String[m_privinceList.size()];
		searchsCH = new String[m_privinceList.size()];
		StringBuilder stringBuilder = new StringBuilder();
		for (int j = 0; j < m_privinceList.size(); j++)
		{
			AddressBean record = m_privinceList.get(j);
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append(record.getRegionName());
			String s = stringBuilder.toString();

			if (s == null || s.trim().length() <= 0)
				continue;
			String str = s.toLowerCase();
			searchsCH[j] = str;
			searchs[j] = PingYinUtil.getPingYin(str);
			searchsHead[j] = PingYinUtil.getPinYinHeadChar(str);
		}
	}
    
    private void setAdapter(List<AddressBean> records)
	{
    	m_privinceLv.setVisibility(View.VISIBLE);
    	m_alphaBar.setVisibility(View.VISIBLE);
    	m_privinceAdapter = new PrivinceAdapter(m_privinceList,PrivinceActivity.this);
    	m_privinceLv.setAdapter(m_privinceAdapter);
		if (m_alphaBar != null)
		{
			m_alphaBar.init(PrivinceActivity.this);
			m_alphaBar.setListView(m_privinceLv);
			m_alphaBar.setHight(m_alphaBar.getHeight());
			m_alphaBar.setVisibility(View.VISIBLE);
		}
		this.updateAdapter();

	}
    
    public void updateAdapter()
	{
		/* for alpha index */
		HashMap<String, Integer> alphaIndexer;// 保存每个索引在list中的位置【#-0，A-4，B-10】
		alphaIndexer = new HashMap<String, Integer>();
		for (int index = 0; index < m_privinceList.size(); index++)
		{
			// String name = getAlpha(list.get(i).getAsString(SORT_KEY));
			String name = PingYinUtil.getAlpha(PingYinUtil.getPingYin(m_privinceList.get(index).getRegionName().toString().trim()));
			if (!alphaIndexer.containsKey(name))
			{// 只记录在list中首次出现的位置
				alphaIndexer.put(name, index);
			}
		}
		if (m_alphaBar != null)
		{
			m_alphaBar.setAlphaIndexer(alphaIndexer);
		}
		m_privinceAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		AddressBean addressDTO = (AddressBean) arg0.getItemAtPosition(position);
		Intent intent = new Intent(PrivinceActivity.this, CityActivity.class);
		intent.putExtra("AddressDTO", addressDTO);
		intent.putExtra("index", index);
		startActivity(intent);		
	}
	
	private void data()
	{
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream inputStream= this.getResources().getAssets().open("address.xml");
			Document document = builder.parse(inputStream);

			// 得到所有的province节点
			NodeList provinces = document.getElementsByTagName("province");
			
			m_privinceList = new ArrayList<AddressBean>();
			
			for (int i = 0; i < provinces.getLength(); i++) 
			{
				System.out.println("**************************************");
				Element eleNode = (Element) provinces.item(i);
				String provincename = eleNode.getAttribute("name");
				
				AddressBean addressDTO = new AddressBean();
				addressDTO.setRegionName(provincename);
				addressDTO.setRegionId(i+"");
				m_privinceList.add(addressDTO);
			}
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
    
}
