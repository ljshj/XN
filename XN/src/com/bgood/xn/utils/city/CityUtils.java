package com.bgood.xn.utils.city;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @todo:获取城市信息
 * @date:2014-10-31 下午1:52:20
 * @author:hg_liuzl@163.com
 */
public class CityUtils {
	/**
	 * 
	 * @todo 获取省份信息
	 * @return
	 */
    public List<CityBean> getProvince(Activity mActivity){
    	DBManager dbm = new DBManager(mActivity);
        dbm.openDatabase();
	 	SQLiteDatabase db = dbm.getDatabase();
	 	List<CityBean> list = new ArrayList<CityBean>();
		
	 	try {    
	        String sql = "select * from province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2); 
		        String name=new String(bytes,"gbk");
		        CityBean CityBean=new CityBean();
		        CityBean.setName(name);
		        CityBean.setPcode(code);
		        list.add(CityBean);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk");
	        CityBean cityBean=new CityBean();
	        cityBean.setName(name);
	        cityBean.setPcode(code);
	        list.add(cityBean);
	        
	    } catch (Exception e) {  
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
	 	return list;
	}
    
    /**
     * 
     * @todo 获取 地区信息
     * @time 2014-5-17 下午4:31:08
     * @author liuzenglong163@gmail.co
     * @return
     */
    public List<CityBean>  getCity(Activity mActivity,String pcode){
    	DBManager dbm = new DBManager(mActivity);
        dbm.openDatabase();
	 	SQLiteDatabase db = dbm.getDatabase();
	 	List<CityBean> list = new ArrayList<CityBean>();
		
	 	try {    
	        String sql = "select * from city where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2); 
		        String name=new String(bytes,"gbk");
		        CityBean cityBean=new CityBean();
		        cityBean.setName(name);
		        cityBean.setPcode(code);
		        list.add(cityBean);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk");
	        CityBean CityBean=new CityBean();
	        CityBean.setName(name);
	        CityBean.setPcode(code);
	        list.add(CityBean);
	        
	    } catch (Exception e) {  
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
	 	return list;
	}
    
    /**
     * 
     * @todo 获取州县信息
     * @time 2014-5-17 下午4:31:22
     * @author liuzenglong163@gmail.com
     * @param dbm
     * @param db
     * @param mActivity
     * @param pcode
     * @return
     */
    public List<CityBean>  getCouty(Activity mActivity,String pcode){
    	DBManager dbm = new DBManager(mActivity);
        dbm.openDatabase();
	 	SQLiteDatabase db = dbm.getDatabase();
	 	List<CityBean> list = new ArrayList<CityBean>();
		
	 	try {    
	        String sql = "select * from district where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
		        String code=cursor.getString(cursor.getColumnIndex("code")); 
		        byte bytes[]=cursor.getBlob(2); 
		        String name=new String(bytes,"gbk");
		        final CityBean cityBean=new CityBean();
		        cityBean.setName(name);
		        cityBean.setPcode(code);
		        list.add(cityBean);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code")); 
	        byte bytes[]=cursor.getBlob(2); 
	        String name=new String(bytes,"gbk");
	        CityBean cityBean=new CityBean();
	        cityBean.setName(name);
	        cityBean.setPcode(code);
	        list.add(cityBean);
	        
	    } catch (Exception e) {  
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
	 	return list;
	}
}
