package com.bgood.xn.utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.Display;

/**
 * 工具类
 */
@SuppressLint("SimpleDateFormat")
public class ConfigUtil {
	
	/**
	 * 
	 * @todo  获取设备的density
	 *  @return
	 */
	public static float getDensity(Activity context){
		return context.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * 获取曲线图轴字体大小
	 * @param context
	 * @return
	 */
	public static float getLablesTextSize(Activity context,float t_size){
		float scale = context.getResources().getDisplayMetrics().density;
		return scale * t_size;
	}
	/**
	 * 获取曲线图轴左右边距
	 * @param context
	 * @return
	 */
	public static int getDisSize(Activity context,float dis){
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (scale * dis);
	}
	/**
	 * 获取屏幕宽
	 */
	@SuppressWarnings({ "deprecation" })
	public static int getDisplayW(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}
	/**
	 * 获取tab滑动单位距离
	 * @param context
	 * @return
	 */
	public static int getTabPix(Activity context){
		return getDisplayW(context) / 4;
	}
	
	/**
	 * 获取当前时间（格式:2013-06-18 17:36:00）
	 * @return
	 */
	public static String getNowTimeFormat(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dateFormat.format( now ); 
		return str;
	}
	/**
	 * 获取当前时间（格式:2013-06-18 17:36:00）
	 * @return
	 */
	public static String getNowTime(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
		String str = dateFormat.format( now ); 
		return str;
	}
	
	/**
	 * 获取当前时间（格式:2013-06-18 ）
	 * @return
	 */
	public static String getNowTimeDate(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
		String str = dateFormat.format(now); 
		return str;
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
	 * 获取当前日期
	 * @return
	 */
	public static String getNowDate(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateFormat.format( now ); 
		return str;
	}
	
	/**
	 * 获取yyyy-MM-dd格式的日期字符串
	 * @param date
	 * @return
	 */
	public static String getDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateFormat.format( date ); 
		return str;
	}
	/**
	 * 获取yyyy-MM-dd格式的日期字符串
	 * @param date
	 * @return
	 */
	public static String getDateCh(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String str = dateFormat.format( date ); 
		return str;
	}
	/**  
     * 功能：得到当前年份年初 格式为：xxxx-yy-zz (eg: 2007-01-01)<br>  
     * @return String  
     * @author pure  
     */  
	public static String thisYear() {
		Calendar localTime;     // 当前日期  
		localTime = Calendar.getInstance();
		int x = localTime.get(Calendar.YEAR);   
		return x + "-01" + "-01";   
	}  
	/**
	 * 获取imei
	 * @return
	 */
	public static String getImei(Context app){
		TelephonyManager telephonyManager=(TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE);
		String imei=telephonyManager.getDeviceId();
		return imei;
	}
	/**
	 * 获取电话号码
	 * @param app
	 * @return
	 */
	public static String getPhoneNumber(Context app){
		TelephonyManager telephonyManager=(TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE);
		String NativePhoneNumber = "";
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;

	}
	
	/**
	 * 获取版本号
	 * 
	 * */ 
	public static int getVersionCode (Context app) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	/**
	 * 
	 * 获取版本名称
	 * */ 
	public static String getVersionName (Context app) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 格式化小数（1.11或者-1.11的形式）
	 * @param str
	 * @return
	 */
	public static String formatDouble(String str,int numCount){
		try{
			String[] array = str.split("\\.");
			if(array.length > 1){
				if(array[1].length() <= numCount){
					int c = numCount - array[1].length();
					for (int i = 0; i < c; i++) {
						array[1] += "0";
					}
				}else{
					array[1] = array[1].substring(0, numCount);
				}
			}else{
				str += ".";
				for (int i = 0; i < numCount; i++) {
					str += "0";
				}
				return str;
			}
			return array[0] + "." + array[1];
			
		}catch (Exception e) {
			// TODO: handle exception
			return "0.000";
		}
	}
	/**
	 * 冒泡排序
	 * @param score
	 * @return
	 */
	public static double[] bubbleSort(double[] list){
		double score[] = list;
		for (int i = 0; i < list.length; i++) {
			score[i] = list[i];
		}
		
		for (int i = 0; i < score.length - 1; i++){    //最多做n-1趟排序
			for(int j = 0 ;j < score.length - i - 1; j++){    //对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)
				if(score[j] < score[j + 1]){    //把小的值交换到后面
					double temp = score[j];
					score[j] = score[j + 1];
					score[j + 1] = temp;
				}
			}            
		}
		return score;
	}

	/**
	 * 判断当前网络是否已经连接 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) { 
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
		try { 
			ConnectivityManager connectivity = (ConnectivityManager) context 
					.getSystemService(Context.CONNECTIVITY_SERVICE); 
			if (connectivity != null) { 
				// 获取网络连接管理的对象 
				NetworkInfo info = connectivity.getActiveNetworkInfo(); 
				if (info != null&& info.isConnected()) { 
					// 判断当前网络是否已经连接 
					if (info.getState() == NetworkInfo.State.CONNECTED) { 
						return true; 
					} 
				} 
			} 
		} catch (Exception e) { 
			// TODO: handle exception 
		} 
		return false; 
	} 
	
	/**
	 * 数字金额大写转换，处理分到千亿的金额
	 * 
	 */
	public static String digitUppercase(String digital){
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String s = "";
		digital=digital.replaceFirst("0*", "");//去掉开头的0
		int dotPosition=digital.indexOf(".");
		
		//处理小数部分
		if(dotPosition==-1){
			dotPosition=digital.length();
		}else{
			if (digital.length() > dotPosition + 1) {
				int jiao = Integer.parseInt(digital.substring(dotPosition + 1,dotPosition + 2));
				if (jiao != 0) {
					s = digit[jiao] + "角";
				}
			}
			if (digital.length() > dotPosition + 2) {
				int fen = Integer.parseInt(digital.substring(dotPosition + 2,dotPosition + 3));
				if (fen != 0) {
					s += digit[fen] + "分";
				}
			}
		}
		
		//以下处理整数部分
		if(s.equals("")){
			s="元整";
		}
	    String unit[]= {"元", "万", "亿"};
	    String intpart="";
	    if(dotPosition>12){
	    	return "万亿+";
	    }else{
	    	intpart=digital.substring(0,dotPosition);
	    }
	    int length=intpart.length();
		for (int i = 0; length-i*4-4>=0; i++) {
			String sub = intpart.substring(length-i*4-4, length-i*4);
			String subChinese4=getchinese4(sub);
			if(!subChinese4.equals("")){
				s=subChinese4+unit[i]+s;
			}
		}
		if(length%4!=0){
			String subChinese4=getchinese4(intpart.substring(0, length%4));
			if(!subChinese4.equals("")){
				s=subChinese4+unit[length/4]+s;
			}
		}
		s=s.replace("元元", "元");
		if(s.substring(0, 1).equals("零")){
			s=s.substring(1);
		}
		if(s.equals("元整")){
			return "零元整";
		}
	    return s;
	}
	
	private static String getchinese4(String char4){
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[]={"","拾","佰","仟"};
		String chinese="";
		int lengh=char4.length();
		for(int i=0;i<lengh;i++){
			int number=Integer.parseInt(char4.substring(lengh-i-1, lengh-i));
			if(number!=0){
				chinese=digit[number]+unit[i]+chinese;
			}else{
				if(!chinese.equals("")&&!chinese.substring(0,1).equals("零")){
					chinese=digit[number]+chinese;
				}
			}
		}
		return chinese;
	}
	
	public static String formatDate(String date) {
		try {
			StringBuilder str = new StringBuilder();
			str.append(date.subSequence(0, 4)).append("-")
					.append(date.subSequence(4, 6)).append("-")
					.append(date.subSequence(6, 8));
			return str.toString();
		} catch (Exception e) {
			return date;
		}
	}
	public static String formatTime(String time){
		try {
			StringBuilder str = new StringBuilder();
			str.append(time.subSequence(0, 2)).append(":")
					.append(time.subSequence(2, 4)).append(":")
					.append(time.subSequence(4, 6));
			return str.toString();
		} catch (Exception e) {
			return time;
		}
	}
	
	public static String getHidedAccount(String account) {
		try {
			int headlength=6;
			int taillength=4;
			String head = account.substring(0, headlength);
			String tail = account.substring(account.length() - taillength,
					account.length());
			String hide="";
			for(int i=0;i<account.length()-headlength-taillength&&i<6;i++){
				hide+="*";
			}
//			String hide = stringExtra.substring(3, stringExtra.length() - 4)
//					.replaceAll("[.]", "*");
			return head+hide+tail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getFormatAmount(String amount){
		 NumberFormat format = NumberFormat.getInstance();
		 format.setMaximumFractionDigits(2);
		 format.setMinimumFractionDigits(2);
		 return format.format(Double.parseDouble(amount));
	}
	/** 
     * 数字半角转换为全角 
     *  
     * @param input 
     * @return 
     */  
    public static String ToDBC(String input) {  
    	if(input.contains("0")){
    		input = input.replace("0", "０");
    	}
    	if(input.contains("1")){
    		input = input.replace("1", "１");
    	}
    	if(input.contains("2")){
    		input = input.replace("2", "２");
    	}
    	if(input.contains("3")){
    		input = input.replace("3", "３");
    	}
    	if(input.contains("4")){
    		input = input.replace("4", "４");
    	}
    	if(input.contains("5")){
    		input = input.replace("5", "５");
    	}
    	if(input.contains("6")){
    		input = input.replace("6", "６");
    	}
    	if(input.contains("7")){
    		input = input.replace("7", "７");
    	}
    	if(input.contains("8")){
    		input = input.replace("8", "８");
    	}
    	if(input.contains("9")){
    		input = input.replace("9", "９");
    	}
        return input;  
    }  
	/**
	 * 字符全角化
	 * @param input
	 * @return
	 */
	public static String ToDBC_(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i< c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}if (c[i]> 65280&& c[i]< 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) throws PatternSyntaxException{
	    str=str.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!");//替换中文标号
	    String regEx="[『』]"; // 清除掉特殊字符
	    Pattern p = Pattern.compile(regEx);
	    Matcher m = p.matcher(str);
	 return m.replaceAll("").trim();
	}
	/**
	 * 计算两日期相差的天数
	 * @param early
	 * @param late
	 * @return
	 */
	public static int daysBetween(Date early, Date late) { 
        java.util.Calendar calst = java.util.Calendar.getInstance();   
        java.util.Calendar caled = java.util.Calendar.getInstance();   
        calst.setTime(early);   
         caled.setTime(late);   
         //设置时间为0时   
         calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         calst.set(java.util.Calendar.MINUTE, 0);   
         calst.set(java.util.Calendar.SECOND, 0);   
         caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
         caled.set(java.util.Calendar.MINUTE, 0);   
         caled.set(java.util.Calendar.SECOND, 0);   
        //得到两个日期相差的天数   
         int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
                .getTime().getTime() / 1000)) / 3600 / 24;   
        return days;   
   } 
	/**
	 * 获取距离当前时间的时间差
	 * @param date
	 * @return
	 */
	public static int getBetweenDays(String begin_date,String end_date){
		Date earlydate = new Date();   
		Date latedate = new Date();   
		DateFormat df = DateFormat.getDateInstance();   
		try {   
			earlydate = df.parse(begin_date);   
			latedate = df.parse(end_date);   
		} catch (ParseException e) {   
			e.printStackTrace();   
		} 
		return ConfigUtil.daysBetween(earlydate,latedate);
	}
	
	
	/***
	 * 
	 *  @param begin_date
	 *  @param end_date
	 *  @return
	 */
	public static boolean getJudgeDays(String begin_date,String end_date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date beginDate = sdf.parse(begin_date);
			Date endDate = sdf.parse(end_date);
			long result = beginDate.getTime() - endDate.getTime();
			if(result < 0){
				return true;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取当前年月日
	 * @return
	 */
	public static int[] getNowYMD(){
		Calendar now = Calendar.getInstance();  
		int[] ymd = new int[3];
		ymd[0] = now.get(Calendar.YEAR);
		ymd[1] = (now.get(Calendar.MONTH) + 1);
		ymd[2] = now.get(Calendar.DAY_OF_MONTH);
		return ymd;
	}
	/**
	 * 根据日期获取年月日
	 * @return
	 */
	public static int[] getNowYMD(String date){
		String[] str_date = date.split("-");
		int[] ymd = new int[3];
		ymd[0] = Integer.valueOf(str_date[0]);
		ymd[1] = Integer.valueOf(str_date[1]);
		ymd[2] = Integer.valueOf(str_date[2]);
		return ymd;
	}

}
