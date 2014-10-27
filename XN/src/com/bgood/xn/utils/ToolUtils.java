
package com.bgood.xn.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.bgood.xn.R;

public class ToolUtils {
	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isAvailable()) {
			return true;
		}
		return false;
	}
	
	public static SpannableStringBuilder setTextColor(Resources re,String text,int start,int color){
        SpannableStringBuilder style=new SpannableStringBuilder(text); 
        if(!TextUtils.isEmpty(text) && start<text.length()){
            style.setSpan(new ForegroundColorSpan(re.getColor(color)),start,text.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return style;
    }

	/**
	 * 判断wifi是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为移动的号码
	 * 
	 * @return
	 */
	public static boolean isMobile(String imsi) {
		if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取Mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
//		WifiManager wifi = (WifiManager) context
//				.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo info = wifi.getConnectionInfo();
//		return info.getMacAddress();
		return "00:22:F4:10:61:7E";
	}

	/**
	 * 判断service是否启动
	 * 
	 * @param contex
	 * @return
	 */
	public static boolean isServiceStart(Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> myServiceList = myManager
				.getRunningServices(50);
		for (ActivityManager.RunningServiceInfo info : myServiceList) {
			if (context.getPackageName().equals(info.service.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断activity是否启动
	 * 
	 * @param contex
	 * @return
	 */
	public static boolean isAcvitityStart(Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> myActivityList = myManager
				.getRunningTasks(1000);
		for (ActivityManager.RunningTaskInfo info : myActivityList) {
			if (context.getPackageName().equals(
					info.baseActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	
	/**data yyyy-mm-dd HH:mm:ss*/
	public static SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * @return
	 */
	public static String formatDate(Date date) {
		if (null == date) return "";
		return dateFormate.format(date);
	}
	/**
	 * @return
	 */
	public static String formatDate(String date) {
		if (null == date) return "";
		try {
			Pattern pp = Pattern.compile("^[\\d]{4}[-]{1}[\\d]{1,2}[-]{1}[\\d]{1,2}");
			Matcher match1 = pp.matcher(date);
			if (match1.find()) {
				return match1.group();
			}
			match1 = null;
			return "";
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * @return
	 */
	public static String formatDate() {
		Date date = new Date();
		return dateFormate.format(date);
	}
	
	/**
	 * 比较时间是为今天
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
    public static boolean compareDate(String date){
	    try {
            return android.text.format.DateUtils.isToday(new Date(date).getTime());
        } catch (Exception e) {
        }
	    return false;
	}
		
	/**
	 * judge the Email style
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		if (null == str) return false;
		boolean cf = isContainChr(str);
		if (cf) return false;
		Pattern pp = Pattern.compile("^[\\w|_]+[\\w|.]*@{1}[A-Za-z0-9_.]+\\.{1}\\w+");
		Matcher match1 = pp.matcher(str);
		boolean ff = match1.matches();
		pp = null;
		match1 = null;
		return ff;
	}
	
	/**
	 * judge the Email style
	 * @param str
	 * @return
	 */
	public static boolean isAllNumber(String str) {
		if (null == str) return false;
		Pattern pp = Pattern.compile("^[0-9]+");
		Matcher match1 = pp.matcher(str);
		boolean ff = match1.matches();
		pp = null;
		match1 = null;
		return ff;
	}
	/**
	 * judge the telephone style
	 * @param str
	 * @return
	 */
	public static boolean isTelephone(String str) {
		if (null == str) return false;
		Pattern pp = Pattern.compile("^\\d+$");
		Matcher match1 = pp.matcher(str);
		boolean ff = match1.matches();
		pp = null;
		match1 = null;
		return ff;
	}
	
	/**
	 * judge the telephone style
	 * @param str
	 * @return
	 */
	public static boolean isTWTelephone(String str) {
		if (null == str) return false;
		Pattern pp = Pattern.compile("09[0-9]{2}[0-9]{6}");
		Matcher match1 = pp.matcher(str);
		boolean ff = match1.matches();
		pp = null;
		match1 = null;
		return ff;
	}
	
	
	
	/**
	 * validate the userCode
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		
		if (null == name) return false;
		
		Pattern pp1 = Pattern.compile("^[\\w|_]{1}[@|\\.|\\w]{5,29}");
		Matcher match = pp1.matcher(name);
		boolean cf = isContainChr(name);
		boolean ff = match.find();
		pp1 = null;
		match = null;
		return !cf && ff;
	}
	/**
	 * 是否为中文
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
             || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
	
	/**
	 * 获取字符串长度,两个英文字母为一个汉字 
	 * @param s
	 * @return
	 */
	public static int lenChar(String s) {
		if (null == s || "".equals(s.trim()))
			return 0;
		int len = s.length();
		int chinese = 0;
		int engs = 0;
		for (int i = 0; i<len; i++) {
			boolean cf = isChinese(s.charAt(i));
			if (cf) 
				chinese ++;
			else {
				engs ++;
			}
		}
		
		return chinese = chinese + engs/2;
	}
	/**
	 * 是否包含了汉字
	 * @param str
	 * @return
	 */
	public static boolean isContainChr(String str) {
		boolean cf = false;
		for (int i = 0; i<str.length(); i++) {
			cf = isChinese(str.charAt(i));
			if (cf) break;
		}
		return cf;
	}
	
	/**
	 * 获取本机的ip地址
	 * @return
	 */
	public static String getIp(){
		try {
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
				NetworkInterface intf = (NetworkInterface)en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			LogUtils.w("getLocalIpAddress",e);
		}
		return null;
	}
	
	/**
	 * 把arraylist转换成string
	 * @param lists
	 * @return
	 */
	public static String arrayListToString(List<? extends Object> lists){
		StringBuffer buffer = new StringBuffer();
		if(lists!=null && lists.size()>0){
			for(Object o : lists){
				buffer.append(o.toString());
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 
	 * @target 获取字符串
	 * @author Andy.Liu
	 * @createTime 2012-8-30  上午11:51:54
	 * @param str
	 * @param len  从什么位置开始截取
	 * @param isEnd 是否从头部截取(false),还是从尾部截取(true)
	 * @return
	 */
	public static String splitString(String str,int len,boolean isEnd){
		String spliteStr = "";
		if(TextUtils.isEmpty(str))
			return spliteStr;
		else{
			int length = str.length();
			if(length>len)
				spliteStr = !isEnd ? str.substring(len) : str.substring(length-len);
			else
				spliteStr = str;	
				
			return spliteStr;
		}
	}
	
	/**
	 * 
	 * @target 获取字符串
	 * @author Andy.Liu
	 * @createTime 2012-8-30  上午11:51:54
	 * @param str
	 * @param begin  从倒数什么位置开始截取
	 * @param end    从倒数什么位置结束
	 * @return
	 */
	public static String splitString(String str,int begin,int end){
		String spliteStr = "";
		if(TextUtils.isEmpty(str))
			return spliteStr;
		else{
			int length = str.length();
			if(end < begin && begin < length)
				spliteStr = str.substring(length - begin,length - end);
			else
				spliteStr = str;	
				
			return spliteStr;
		}
	}
	
//	/**
//	 * 获取版本号
//	 * @return 当前应用的版本号
//	 */
//	public static String getVersion(Context context) {
//	    try {
//	        PackageManager manager = context.getPackageManager();
//	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//	        String version = info.versionName;
//	        return context.getString(R.string.app_version,version);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return context.getString(R.string.app_get_fail);
//	    }
//	}
	
	/**
	 * 判断当前环境是否为台湾地址
	 */
    public static boolean isTaiwan() {
        String country = Locale.getDefault().getCountry();
        if("TW".equalsIgnoreCase(country)){
            return true;
        }else{
            return false;
        }
    } 
    
    
    /**
	 * 设置最后刷新时间（格式:今天 17:36:00）
	 * date (MM-dd HH:mm:ss)
	 * @return
	 */
	public static String getRefreshTime(String date){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
		String str = dateFormat.format(now); 
		try{
		if(date == null || date.equals("")){
			dateFormat = new SimpleDateFormat("HH:mm:ss");
			str = dateFormat.format(now); 
			return "";
//			return "今天  " + str;
		}
		String[] p = date.split(" ");
		String days[] = p[0].split("-");
		String m_b = days[0];
		String d_b = days[1];
		
		
		String[] p_ = date.split(" ");
		String days_n[] = p_[0].split("-");
		String m_n = days_n[0];
		String d_n = days_n[1];
		if(m_b.equals("m_n")){
			int margin = Integer.parseInt(d_n) - Integer.parseInt(d_b);
			dateFormat = new SimpleDateFormat("HH:mm:ss");
			str = dateFormat.format(now); 
			if(margin == 0){
				return "今天 " + str;
			}else if(margin == 1){
				return "昨天 " + str;
			}else if(margin == 2){
				return "前天 " + str;
			}else{
				dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
				str = dateFormat.format(now); 
				return str;
			}
		}else{
			dateFormat = new SimpleDateFormat("HH:mm:ss");
			str = dateFormat.format(now); 
			return "今天  " + str;
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}

	
	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean judgeTelephone(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 电话号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	
	/**
	 * 时间格式
	 * date (MM-dd HH:mm:ss)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getFormatDate(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date currentDate = new Date();
		Date fromDate = null;
		StringBuffer sb = new StringBuffer();
		try {
			fromDate = dateFormat.parse(date);
			
			if(fromDate.getYear() == currentDate.getYear() && fromDate.getMonth() == currentDate.getMonth()){	//如果是同年同月
				if(fromDate.getDate() == currentDate.getDate()){
					sb.append("今天");
				}else if(fromDate.getDate() == currentDate.getDate()-1){
					sb.append("昨天");
				}else if(fromDate.getDate() == currentDate.getDate()-2){
					sb.append("前天");
				}else{
					sb.append(fromDate.getMonth()+1).append("月").append(fromDate.getDate()).append("日");
				}
				sb.append(" ").append(fromDate.getHours()).append(":").append(fromDate.getMinutes());
				sb.append(fromDate.getHours()>12 ? "PM":"AM");
				return sb.toString();
				
			}else if(fromDate.getYear() == currentDate.getYear() && fromDate.getMonth() != currentDate.getMonth()){//同年不同月
				return new SimpleDateFormat("MM-dd HH:mm").format(fromDate);
			}else if(fromDate.getYear() == currentDate.getYear() && fromDate.getMonth() == currentDate.getMonth()){//不同年且不同月
				return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(fromDate);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @todo:获取当前时间
	 * @date:2014-10-27 下午4:33:32
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public static String getNowTime() {
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String nowTime = sdf.format(date);
		
		return getFormatDate(nowTime);
	}
}
