package com.bgood.xn.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ChenGuoqing 2014-7-4下午5:17:57
 */
public class MD5
{
	public static String getMD5(String val) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] m = md5.digest();// 加密
		return getString(m);
	}

	private static String getString(byte[] b)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			sb.append(b[i]);
		}
		return sb.toString();
	}
}