package com.bgood.xn.utils;

import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PingYinUtil
{
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString)
	{
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		String output = "";
		char[] input = inputString.trim().toCharArray();
		try
		{
			for (int i = 0; i < input.length; i++)
			{
				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+"))
				{
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					if(temp!=null)
					{
					   output += temp[0];
					}
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e)
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 */
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 大写输出
		} else {
			return "#";
		}
	}
	/**
	 * 提取每个汉字的首字母
	 * 
	 * @param str
	 * @return String
	 */
	public static String getPinYinHeadChar(String str)
	{
		String convert = "";
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		for (int j = 0; j < str.length(); j++)
		{
			char word = str.charAt(j);
			// 提取汉字的首字母

			String[] pinyinArray = null;
			try
			{
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word, t3);
			} catch (BadHanyuPinyinOutputFormatCombination e)
			{
				e.printStackTrace();
			}
			if (pinyinArray != null)
			{
				convert += pinyinArray[0].charAt(0);
			} else
			{
				convert += word;
			}
		}
		return convert;
	}
	
}
