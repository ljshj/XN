package com.bgood.xn.network.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.utils.FormatTransfer;
import com.bgood.xn.utils.LogUtils;

public class HttpManager {

	public static final String STR_ENCODE = "utf-8";
	//超时时间
	public static final int CONN_TIMEOUT = 30 * 1000;	
	@SuppressWarnings("static-access")
	public static BaseNetWork postHttpRequest(HttpRequestInfo info,BaseNetWork bNetWork)throws IOException {
		BaseNetWork mBaseNetWork = null;
		URL url = new URL(info.getRequestUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "Application");
		conn.setRequestProperty("Content-Length", bNetWork.GetMeasureLength()+ "");
		conn.setReadTimeout(CONN_TIMEOUT);
		conn.setConnectTimeout(CONN_TIMEOUT);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		bNetWork.send(dos);
		conn.connect();
		switch (conn.getResponseCode()) {
		case 200:
			InputStream is = conn.getInputStream();
			byte[] le = new byte[4];
			is.read(le, 0, 4);
			int size = FormatTransfer.lBytesToInt(le) - 4;
			int lenght = 0;
			byte[] bs = new byte[size + 4];
			System.arraycopy(le, 0, bs, 0, 4);
			while(lenght < size)
			{
			    lenght += is.read(bs, 4 + lenght, size-lenght);
			}
			try {
				mBaseNetWork = BaseNetWork.parseB(bs, bs.length);
				LogUtils.i("----------------------"+mBaseNetWork.getBody());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if (is != null)
			{
			    is.close();
			    is = null;
			}
			if (conn != null)
			{
				conn.disconnect();
				conn = null;
			}
			break;
		default:
			
			break;
		}
		return mBaseNetWork;
	}
	
	
	
	
	public static BaseNetWork getHttpRequest(HttpRequestInfo info,BaseNetWork bNetWork)throws IOException {
		BaseNetWork mBNetWork = null;
		File f = bNetWork.getFile();
		if(!f.exists()){
			return mBNetWork;
		}
		FileInputStream stream = null;
		InputStream is = null;
		OutputStream ot = null;
		try
		{
			URL url = new URL(info.getRequestUrl());
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setDoOutput(true);
			request.setRequestMethod("GET");
			
			stream = new FileInputStream(f);
			byte[] file_arr = new byte[(int) f.length()];
			stream.read(file_arr);
			ot = request.getOutputStream();
			ot.write(file_arr);
				
			ot.flush();
			request.connect();
			if (200 == request.getResponseCode())
			{
				is = request.getInputStream(); // 获取输入�?
				byte[] data = readStream(is); // 把输入流转换成字节数�?
				String json = new String(data); // 把字符数组转换成字符�?
				mBNetWork = new BaseNetWork();
				JSONObject object = new JSONObject(json);
				mBNetWork.setBody(object);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (stream != null)
			{
				try
				{
					stream.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				stream = null;
			}
			if (is != null)
			{
				try
				{
					is.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				is = null;
			}
			if (ot != null)
			{
				try
				{
					ot.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				ot = null;
			}
		}
		return mBNetWork;
	}
	/**
	 * 把输入流转换成字节数数组
	 * @return 字节数组
	 */
	private static byte[] readStream(InputStream inputStream) throws Exception
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		if (null != inputStream)
		{
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1)
			{
				bout.write(buffer, 0, len);
			}
			bout.close();
			inputStream.close();
		}
		return bout.toByteArray();
	}
}
