package com.bgood.xn.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;

import com.bgood.xn.utils.FormatTransfer;

public class HttpManager {

	public static final String STR_ENCODE = "utf-8";
	//超时时间
	public static final int CONN_TIMEOUT = 30 * 1000;	
	@SuppressWarnings("static-access")
	public static BaseNetWork postHttpRequest(HttpRequestInfo info,BaseNetWork bNetWork)
			throws IOException {
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
	
	
	/**
	 * 以POST方式发请求，把返回的结果以字符串的方式返回
	 * 
	 * @param posturl
	 *            请求的url
	 * @return http响应内容转换成字符串
	 * @throws IOException
	 */
	public static String postHttpRequest(HttpRequestInfo info)
			throws IOException {
		String contentAsString = null;
		URL url = new URL(info.getRequestUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(8000 /* milliseconds */);
		conn.setConnectTimeout(8000 /* milliseconds */);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		String paramsStr = info.getParamsStr();
		if (paramsStr != null) {
			conn.getOutputStream().write(paramsStr.getBytes(STR_ENCODE));
			conn.getOutputStream().flush();
			conn.getOutputStream().close();
		}
		conn.connect();
		switch (conn.getResponseCode()) {
		case 200:
			InputStream is = conn.getInputStream();
			contentAsString = readIt(is, conn.getContentLength());
			is.close();
			break;
		default:
			// TODO:错误处理
			contentAsString = "服务器返回错误";
			break;
		}
		return contentAsString;
	}

	// Reads an InputStream and converts it to a String.
	private static String readIt(InputStream stream, int len)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(stream,Charset.forName(STR_ENCODE)));
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		return new String(sb.toString());
	}

	public static String postHttpsRequest(HttpRequestInfo info) {
		String contentAsString = null;
		URL url = null;
		try {
			url = new URL(info.getRequestUrl());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sc.init(null, new TrustManager[] { new MyTrustManager() },
					new SecureRandom());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
		HttpsURLConnection conn = null;
		try {
			conn = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.setReadTimeout(8000 /* milliseconds */);// 设置10秒居然会超时
		conn.setConnectTimeout(8000 /* milliseconds */);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.setDoInput(true);
		conn.setDoOutput(true);
		String paramsStr = info.getParamsStr();
		if (paramsStr != null) {
			try {
				conn.getOutputStream().write(paramsStr.getBytes(STR_ENCODE));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.getOutputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			switch (conn.getResponseCode()) {
			case 200:
				InputStream is = conn.getInputStream();
				contentAsString = readIt(is, conn.getContentLength());
				is.close();
				break;
			default:
				// TODO:错误处理
				contentAsString = "服务器返回错误";
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return contentAsString;
	}
}

class SSLSocketFactoryEx extends SSLSocketFactory {

	SSLContext sslContext = SSLContext.getInstance("TLS");

	public SSLSocketFactoryEx(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(truststore);

		TrustManager tm = new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
			}
		};
		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}

class MyHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		// TODO Auto-generated method stub
		return true;
	}
}

class MyTrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}
}
