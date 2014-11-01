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

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.adapter.CityAdapter;
import com.bgood.xn.bean.AddressBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.utils.PingYinUtil;
import com.bgood.xn.widget.ZZCityQuickAlphabeticBar;

/**
 * 选择城市页面
 */
public class CityActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState
{
	private Button m_backBtn = null;
	private TextView m_titleTv = null;
	private ListView m_cityLv = null;
	private ProgressBar m_progressBar = null;
	private ZZCityQuickAlphabeticBar m_alphaBar = null;
	private  CityAdapter m_cityAdapter = null;
    private List<AddressBean> m_cityList = null;    // 省份/直辖市列表
    private String[] searchsHead;
	private String[] searchs;
	private String[] searchsCH;
	private AddressBean m_addressDTO = null;
	private AddressBean cityAddressDTO = null;
	private int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_city);
		
		getIntentData();
		findView();
		setListener();
		
		new GetCityListTask(CityActivity.this).execute(m_cityLv);
	}
	


	/**
     * 得到上层传值数据方法
     */
    private void getIntentData()
    {
        Intent intent = getIntent();
        m_addressDTO = (AddressBean)intent.getSerializableExtra("AddressDTO");
        index = intent.getIntExtra("index", 0);
    }
	
	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		m_backBtn = (Button) findViewById(R.id.city_btn_back);
		m_titleTv = (TextView) findViewById(R.id.city_tv_title);
		m_progressBar = (ProgressBar) findViewById(R.id.city_progress);
		m_cityLv = (ListView) findViewById(R.id.city_lv_privince);
		m_alphaBar = (ZZCityQuickAlphabeticBar) findViewById(R.id.city_qab_fast_scroller);
		
		if (index == 0)
        {
            m_titleTv.setText("家乡（市）");
        }
        else
        {
            m_titleTv.setText("所在地（市）");
        }
	}
	
	/**
	 * 控件事件监听方法
	 */
	private void setListener()
	{
		m_backBtn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				CityActivity.this.finish();
			}
		});
		
		m_cityLv.setOnItemClickListener(this);
	}
	
    /**
	 * 通过异步加载类得到地址列表
	 */
	private class GetCityListTask extends AsyncTask<ListView, String, List<AddressBean>>
	{
		private Context context;
		
		public GetCityListTask(Context context)
		{
			super();
			this.context = context;
		}
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			m_progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<AddressBean> doInBackground(ListView... params)
		{
			data();
			for (int i = 0; i < m_cityList.size(); i++)
	        {
	        	Collections.sort(m_cityList, m_cityList.get(i));
	        }
			
			initSearchDate();
			return m_cityList;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(List<AddressBean> result)
		{
			super.onPostExecute(result);
			m_progressBar.setVisibility(View.GONE);
			
			setAdapter(result);
		}
		
	}
    
    private void initSearchDate()
	{
		if (m_cityList == null)
			return;
		searchs = new String[m_cityList.size()];
		searchsHead = new String[m_cityList.size()];
		searchsCH = new String[m_cityList.size()];
		StringBuilder stringBuilder = new StringBuilder();
		for (int j = 0; j < m_cityList.size(); j++)
		{
			AddressBean record = m_cityList.get(j);
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
    	m_cityLv.setVisibility(View.VISIBLE);
    	m_alphaBar.setVisibility(View.VISIBLE);
    	m_cityAdapter = new CityAdapter(m_cityList,CityActivity.this);
    	m_cityLv.setAdapter(m_cityAdapter);
		if (m_alphaBar != null)
		{
			m_alphaBar.init(CityActivity.this);
			m_alphaBar.setListView(m_cityLv);
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
		for (int index = 0; index < m_cityList.size(); index++)
		{
			// String name = getAlpha(list.get(i).getAsString(SORT_KEY));
			String name = PingYinUtil.getAlpha(PingYinUtil.getPingYin(m_cityList.get(index).getRegionName().toString().trim()));
			if (!alphaIndexer.containsKey(name))
			{// 只记录在list中首次出现的位置
				alphaIndexer.put(name, index);
			}
		}
		if (m_alphaBar != null)
		{
			m_alphaBar.setAlphaIndexer(alphaIndexer);
		}
		m_cityAdapter.notifyDataSetChanged();
	}
    
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
        cityAddressDTO = (AddressBean) arg0.getItemAtPosition(position);
        
        loadData(m_addressDTO.getRegionName() + cityAddressDTO.getRegionName());
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
			
			m_cityList = new ArrayList<AddressBean>();
			
			for (int i = 0; i < provinces.getLength(); i++) 
			{
				System.out.println("**************************************");
				Element eleNode = (Element) provinces.item(i);
				
				if (Integer.parseInt(m_addressDTO.getRegionId()) == i)
				{
					NodeList citys = eleNode.getElementsByTagName("city");
					for (int j = 0; j < citys.getLength(); j++) 
					{
						Element city = (Element) citys.item(j);
						String cityName = city.getAttribute("name");
						
						AddressBean addressDTO = new AddressBean();
						addressDTO.setRegionName(cityName);
						m_cityList.add(addressDTO);
					}
				}
				
				
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
    
    /**
     * 加载数据方法
     * @param value 修改内容
     */
    private void loadData(String value)
    {
    	UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity,index == 0 ?"hometown":"localplace", value);
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				 Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
		            if (index == 0)
		            {
		                SharedPreferences sharedPreferences = CityActivity.this.getSharedPreferences("HomeAddress", MODE_PRIVATE);
		                Editor editor = sharedPreferences.edit();
		                editor.putString("address", m_addressDTO.getRegionName() + cityAddressDTO.getRegionName());
		                editor.commit();
		            }
		            else
		            {
		                SharedPreferences sharedPreferences = CityActivity.this.getSharedPreferences("LoaAddress", MODE_PRIVATE);
		                Editor editor = sharedPreferences.edit();
		                editor.putString("address", m_addressDTO.getRegionName() + cityAddressDTO.getRegionName());
		                editor.commit();
		            }
		            
			}else{
				Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
			}
			
			
			}
	}

}
