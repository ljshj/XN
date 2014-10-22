package com.bgood.xn.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.bgood.xn.utils.LogUtils;

/**
 * @author lzlong@zwmob.com
 * @todo  封装过后的数据库帮助 类，提供数据库的增删改查操作
 * @time 2013-11-1 上午10:26:28
 */
public abstract class CommonDB extends SQLiteOpenHelper {
	public static final String QUERY_WHERE_LIEK = " like ? ";  //like关键字
	public static final String QUERY_WHERE_OR= " or ";         //or关键字
	public static final String QUERY_WHERE_AND = " and ";      //and关键字
	public static final String QUERY_WHERE_PARAM = " = ? ";      //参数
	
	public static final String FD_ID = "_id";   //流水号id字段
	
	private static final String [] ID =  {FD_ID};
	private static final String WHERE_ID =  FD_ID+QUERY_WHERE_PARAM;
	
	public CommonDB(Context context,String databaseName,int databaseVersion){
		super(context, databaseName, null, databaseVersion);
	}
	
	@Override
	public abstract void onCreate(SQLiteDatabase db) ;

	@Override
	public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	
	/**
	 * 删除表格所有数据
	 * @param table 
	 */
	public int deleteAll(String table){
		SQLiteDatabase db = getWritableDatabase();
		int count = db.delete(table, null, null);
		db.close();
		return count;
	}
	
	/**
     * 删除指定的记录
     * @param table  表名
     * @param whereClause 条件
     * @param values 匹配值
     */
    public int deleteAll(String table,String whereClause,String[] values){
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete(table, whereClause + QUERY_WHERE_PARAM, values);
        db.close();
        return count;
    }
    
	/**
     * 删除指定的记录
     * @param table  表名
     * @param field 字段名
     * @param value 匹配值
     */
    public int deleteAll(String table,String field,String value){
    	int count = 0;
        count = deleteAll(table, new String[]{field}, new String[]{value});
        return count;
    }
	
	/**
	 * 删除指定的记录
	 * @param table  表名
	 * @param fields 字段名
	 * @param values 匹配值
	 */
	public int deleteAll(String table,String[] fields,String[] values){
		SQLiteDatabase db = getWritableDatabase();
		int count = 0;
		if(fields==null || fields.length==0){
		    count = db.delete(table, null, null);
		}
		else{
			String where = "";
			for(int i=0;i<fields.length;i++){
				if(i>0) where+=QUERY_WHERE_AND;
				where+=fields[i]+QUERY_WHERE_PARAM;
			}
			count = db.delete(table, where, values);
		}
		db.close();
		return count;
	}
	
	/**
	 * 更新数据库
	 * @param table
	 * @param values
	 * @param field
	 * @param value
	 */
	public int update(String table,ContentValues values,String field,String value){
		int count = 0;
		SQLiteDatabase db = getWritableDatabase();
		count = db.update(table, values, field+QUERY_WHERE_PARAM, new String[]{value});
		db.close();
		return count;
	}
	
	/**
     * 更新数据库
     * @param table
     * @param values
     * @param field
     * @param value
     */
    public int update(String table,ContentValues values,String whereClause,String[] wheres){
        SQLiteDatabase db = getWritableDatabase();
        int count = db.update(table, values, whereClause + QUERY_WHERE_PARAM, wheres);
        db.close();
        return count;
    }
	
	/**
	 * 更新或插入数据(如果存在则更新,否则插入)
	 * @param table  表名
	 * @param values  值
	 * @param onlyField  判断记录唯一的字段名
	 */
	public int updateOrInsert(String table,ContentValues values,String onlyField){
		SQLiteDatabase db = null;
		Cursor cursor = null;
		int count = 0;
		try {
			db = getWritableDatabase();
			cursor = db.query(table, ID, onlyField+QUERY_WHERE_PARAM, new String[]{values.getAsString(onlyField)}, null, null, null);
			if(cursor!=null && cursor.getCount()==1){
				cursor.moveToFirst();
				count = db.update(table, values, WHERE_ID, new String[]{String.valueOf(cursor.getInt(0))});
				LogUtils.i("---------update-----------------"+count);
			}
			else{
				count = (int) db.insert(table, null, values);
				LogUtils.i("---------add-----------------"+count);
			}
		} catch (Exception e) {
		}
		finally{
			if(cursor!=null) cursor.close();
			if(db!=null) db.close();
		}
		return count;
	}
	
	/**
     * 更新或插入数据(如果存在则更新,否则插入)
     * @param table  表名
     * @param values  值
     * @param onlyField  判断记录唯一的字段名
     */
    public int updateOrInsert(String table,ContentValues values,String whereClause,String[] wheres){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int count = 0;
        try {
            db = getWritableDatabase();
            cursor = db.query(table, ID, whereClause, wheres, null, null, null);
            if(cursor!=null && cursor.getCount()==1){
                cursor.moveToFirst();
                count = db.update(table, values, WHERE_ID, new String[]{String.valueOf(cursor.getInt(0))});
            }
            else{
                db.insert(table, null, values);
                count = 1;
            }
        } catch (Exception e) {
            LogUtils.w(e);
        }
        finally{
            if(cursor!=null) cursor.close();
            if(db!=null) db.close();
        }
        return count;
    }
	
	/**
	 * 更新或插入数据(如果存在则更新,否则插入)
	 * @param table  表名
	 * @param values  值
	 * @param onlyField  判断记录唯一的字段名
	 */
	public void updateOrInsert(String table,ArrayList<ContentValues> values,String onlyField){
		if(values!=null && values.size()>0){
			SQLiteDatabase db = null;
			Cursor cursor = null;
			try {
				db = getWritableDatabase();
				String[] value = new String[values.size()];
				int len = values.size();
				for(int i=0;i<len;i++){
					value[i] = values.get(i).getAsString(onlyField);
				}
				cursor = queryInAll(db,table,new String[]{FD_ID,onlyField}, onlyField, value, null);
				HashMap<String, String> keyValues = null;
				if(cursor!=null&&cursor.getCount()>0){
					keyValues = new HashMap<String, String>(18);
					do{
						keyValues.put(cursor.getString(1), cursor.getString(0));
					}
					while(cursor.moveToNext());
				}
				String va = null;
				if(null==keyValues || keyValues.size()==0){
					for(ContentValues v : values){
						db.insert(table, null, v);
					}
				}
				else{
					for(ContentValues v : values){
						va = keyValues.get(v.get(onlyField));
						if(TextUtils.isEmpty(va)){
							db.insert(table, null, v);
						}
						else{
							db.update(table, v, WHERE_ID, new String[]{va});
						}
					}
				}
			} catch (Exception e) {
				LogUtils.w(e);
			}
			finally{
				if(cursor!=null) cursor.close();
				if(db!=null) db.close();
			}
		}
	}
	
	/**
	 * 向表格插入一条记录
	 * @param table  表名
	 * @param values 值
	 */
	public long insert(String table,ContentValues values){
		SQLiteDatabase db = getWritableDatabase();
		long number = db.insert(table, null, values);
		db.close();
		return number;
	}
	
	/**
	 * 支持事务的多条数据插入
	 * @param table
	 * @param values
	 * @return
	 */
	public int insert(String table,ArrayList<ContentValues> values){
		if(values!=null && values.size()>0){
			SQLiteDatabase db = getWritableDatabase();
			db.beginTransaction();
			try {
				for(ContentValues value : values){
					if (db.insert(table, null, value) < 0) {
                        return 0;
                    }
				}
				db.setTransactionSuccessful();
			} finally{
				db.endTransaction();
				db.close();
			}
			return values.size();
		}
		return 0;
	}
	
	/**
	 * 获取数量
	 * @param table
	 * @param fields
	 * @param values
	 * @param removeRepeat
	 */
	public int count(String table,String[] fields,String[] values,boolean removeRepeat){
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = getReadableDatabase();
			StringBuffer buffer = new StringBuffer(" select  ");
			if(removeRepeat){
				buffer.append(" distinct ");
			}
			buffer.append(" count(*) from "+table);
			String where = "";
			if(fields!=null && values!=null){	
				where+=" where ";
				for(int i=0;i<fields.length;i++){
					if(i>0) where+=QUERY_WHERE_AND;
					where+=" "+fields[i]+ "=? ";
				}
			}
			buffer.append(where);
			cursor = db.rawQuery(buffer.toString(), values);
			if(cursor==null){
				return 0;
			}
			cursor.moveToFirst();
			return cursor.getInt(0);
		} catch (Exception e) {
			LogUtils.w(e);
			return 0;
		}
		finally{
			if(cursor!=null) cursor.close();
			if(db!=null) db.close();
		}
	}
	
	/**
	 * 查询表的所有数据
	 * @param table 表名
	 * @return
	 */
	public Cursor queryAll(String table){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, null, null, null, null, null, FD_ID);
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
     * 查询表的所有数据
     * @param table 表名
     * @return
     */
    public Cursor queryAll(String table,String oderBy){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, null, null, null, null, null, oderBy);
        if(c!=null)c.moveToFirst();
        db.close();
        return c;
    }
	
	/**
	 * 分页查询表的所有数据
	 * @param table 表名
	 * @param curpage 当前页
	 * @param pagesize 每页的大小
	 * @return
	 */
	public Cursor queryAll(String table,int curpage,int pagesize){
		SQLiteDatabase db = getReadableDatabase();
		String limit = (curpage-1)*pagesize+","+pagesize;
		Cursor c = db.query(table, null, null, null, null, null, FD_ID, limit);
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
     * 分页查询表的所有数据
     * @param table 表名
     * @param curpage 当前页
     * @param pagesize 每页的大小
     * @return
     */
    public Cursor queryAll(String table,String orderBy,int curpage,int pagesize){
        SQLiteDatabase db = getReadableDatabase();
        String limit = (curpage-1)*pagesize+","+pagesize;
        Cursor c = db.query(table, null, null, null, null, null, orderBy, limit);
        if(c!=null)c.moveToFirst();
        db.close();
        return c;
    }
	
	/**
	 * 分页查询表的所有数据
	 * @param table 表名
	 * @param curpage 当前页
	 * @param pagesize 每页的大小
	 * @return
	 */
	public Cursor queryAll(String table,String field,Object value, int curpage,int pagesize){
		SQLiteDatabase db = getReadableDatabase();
		String limit = (curpage-1)*pagesize+","+pagesize;
		Cursor c = db.query(table, null, field+QUERY_WHERE_PARAM, new String[]{String.valueOf(value)}, null, null, FD_ID, limit);
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
	 * 分页查询表的所有数据
	 * @param table 表名
	 * @param curpage 当前页
	 * @param pagesize 每页的大小
	 * @return
	 */
	public Cursor queryAll(String table,String[] fields,String[] values, int curpage,int pagesize){
		SQLiteDatabase db = getReadableDatabase();
		String limit = (curpage-1)*pagesize+","+pagesize;
		Cursor c = null;
		if(fields==null || fields.length==0){
			c = db.query(table, null, null, null, null, null, FD_ID);
		}else{
			String where = "";
			for(int i=0;i<fields.length;i++){
				if(i>0) where+=QUERY_WHERE_AND;
				where+=" "+fields[i]+" = ? ";
			}
			c = db.query(table, null, where, values, null, null,FD_ID,limit);
		}
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
	 * 根据多个条件，查询
	 * @param table1    表1
	 * @param table2    表2
	 * @param field1   相等字段
	 * @param field2   相等字段
	 * @param fields 条件字段
	 * @param values 条件值
	 * @param order  排序
	 * @return
	 */
	public Cursor queryAndAll(String table1,String table2,String field1,String field2,String[] fields,String[] values,String order){
		SQLiteDatabase db = getReadableDatabase();
		String where = "";
		for(int i=0;i<fields.length;i++){
			if(i>0) where+=QUERY_WHERE_AND;
			where+=" "+fields[i]+" = ? ";
		}
		where+=" "+QUERY_WHERE_AND+" tb1"+"."+field1+" = tb2"+"."+field2;
		Cursor c = db.query(table1+" as tb1,"+table2+" as tb2 ", null, where, values, null, null, order);
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
	 * 根据多个条件，查询
	 * @param table1  表1
	 * @param table2  表2
	 * @param field1  相等字段
	 * @param field2  相等字段
	 * @param fields 条件字段
	 * @param values 条件值
	 * @return
	 */
	public Cursor queryAndAll(String table1,String table2,String field1,String field2,String[] fields,String[] values){
		return queryAndAll(table1, table2, field1, field2, fields, values,null);
	}
	
	/**
	 * 查询表的某些字段
	 * @param table  表名
	 * @param fields 字段
	 * @return
	 */
	public Cursor queryAll(String table,String[] fields){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(table, fields, null, null, null, null, null);
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
	 * 根据条件查询数据
	 * @param table   表名
	 * @param fields  条件字段
	 * @param values  条件值
	 * @return
	 */
	public Cursor queryAndAll(String table,String[] fields,String[] values){
		return queryAndAll(table,null,fields,values,null);
	}
	
	/**
     * 根据条件查询数据
     * @param table   表名
     * @param fields  条件字段
     * @param values  条件值
     * @return
     */
    public Cursor queryAndAll(String table,String[] fields,String[] values,String oder){
        return queryAndAll(table,null,fields,values,oder);
    }
	
	/**
	 * 根据条件查询数据
	 * @param table   表名
	 * @param fields  条件字段
	 * @param values  条件值
	 * @return
	 */
	public Cursor queryAndAll(String table,String field,Object value){
		return queryAndAll(table,null,new String[]{field},new String[]{String.valueOf(value)},null);
	}
	
	/**
	 * 根据条件查询数据
	 * @param table   表名
	 * @param fields  条件字段
	 * @param values  条件值
	 * @param order 排序方式
	 * @return
	 */
	public Cursor queryAndAll(String table,String[] coloumns,String[] fields,String[] values,String order){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = null;
		if(fields==null || fields.length==0){
			c = db.query(table, null, null, null, null, null, null);
		}else{
			String where = "";
			for(int i=0;i<fields.length;i++){
				if(i>0) where+=QUERY_WHERE_AND;
				where+=" "+fields[i]+" = ? ";
			}
			c = db.query(table, coloumns, where, values, null, null,order);
		}
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
     * 根据条件查询数据
     * @param table   表名
     * @param fields  条件字段
     * @param values  条件值
     * @param order 排序方式
     * @return
     */
    public Cursor queryAndAll(String table,String[] coloumns,String[] fields,String[] values){
        return queryAndAll(table, coloumns, fields, values, null);
    }
    
    /**
     * 根据条件查询数据
     * @param table   表名
     * @param fields  条件字段
     * @param values  条件值
     * @param order 排序方式
     * @return
     */
    public Cursor queryAndAll(String table,String[] coloumns,String field,Object value){
        return queryAndAll(table, coloumns, new String[]{field}, new String[]{String.valueOf(value)}, null);
    }
	
	/**
	 * 查询某个字段属性某一些值的记录
	 * @param table   表名
	 * @param field   字段名
	 * @param values  一些值
	 * @param order   排序方式
	 * @return
	 */
	public Cursor queryInAll(String table,String field,String[] values,String order){
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = null;
		String value = "";
		for(int i=0;i<values.length;i++){
			if(i!=0) value+=",";
			value+="'"+values[i]+"'";
		}
		c = db.query(table, null, field+" in("+value+") ",null, null, null, null);
		if(c!=null)c.moveToFirst();
		db.close();
		return c;
	}
	
	/**
	 * 查询某个字段属性某一些值的记录
	 * @param table   表名
	 * @param field   字段名
	 * @param values  一些值
	 * @param order   排序方式
	 * @return
	 */
	private Cursor queryInAll(SQLiteDatabase db,String table,String[] columns,String field,String[] values,String order){
		Cursor c = null;
		String value = "";
		for(int i=0;i<values.length;i++){
			if(i!=0) value+=",";
			value+="'"+values[i]+"'";
		}
		c = db.query(table, columns, field+" in("+value+") ",null, null, null, null);
		if(c!=null)c.moveToFirst();
		return c;
	}
	
	/**
	 * 查询一个表的最大Id;
	 * @param table  表名
	 * @param value  查询关键字
	 * @return
	 */
	public int queryMaxId(String table,String feilds){
		int maxId = 0;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(table, null, null, null, null, null, feilds+" DESC");
		if(cursor.moveToNext())
		{
		   maxId = cursor.getInt(cursor.getColumnIndex(feilds));
		}
		return maxId;
		
		
	}
}
