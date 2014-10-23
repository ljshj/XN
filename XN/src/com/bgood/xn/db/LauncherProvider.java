package com.bgood.xn.db;
//
//package com.hct.paddesk.db;
//
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.text.TextUtils;
//
//import com.hct.paddesk.db.DBHelper.Shop;
//import com.hct.paddesk.db.DBHelper.ShopNav;
//import com.hct.paddesk.db.DBHelper.SoftwareStatus;
//import com.hct.paddesk.utils.LogUtils;
//
//import java.util.HashMap;
//
///**
// * content provider
// * 
// * @author liaolj 2012-6-29 下午4:23:15
// */
//public class LauncherProvider extends ContentProvider {
//    // 数据库帮助类
//    private DBHelper dbHelper;
//    // Uri工具类
//    private static final UriMatcher sUriMatcher;
//    // 查询、更新条件
//    private static final int NAV = 1;
//    private static final int NAV_ID = 2;
//    private static final int AD = 3;
//    private static final int AD_ID = 4;
//    private static final int SHOP = 5;
//    private static final int SHOP_ID = 6;
//    private static final int SOFTWARE = 7;
//    private static final int SOFTWARE_ID = 8;
//    // 查询列集合
//    private static HashMap<String, String> navProjectionMap;
//    // 查询列集合
//    private static HashMap<String, String> adProjectionMap;
//    // 查询列集合
//    private static HashMap<String, String> shopProjectionMap;
//    // 查询列集合
//    private static HashMap<String, String> softwareProjectionMap;
//    static {
//        // Uri匹配工具类
//        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        sUriMatcher.addURI(ShopNav.AUTHORITY, "nav", NAV);
//        sUriMatcher.addURI(ShopNav.AUTHORITY, "nav/#", NAV_ID);
//        sUriMatcher.addURI(DBHelper.AD.AUTHORITY, "ad", AD);
//        sUriMatcher.addURI(DBHelper.AD.AUTHORITY, "ad/#", AD_ID);
//        sUriMatcher.addURI(Shop.AUTHORITY, "shop", SHOP);
//        sUriMatcher.addURI(Shop.AUTHORITY, "shop/#", SHOP_ID);
//        sUriMatcher.addURI(Shop.AUTHORITY, "software_status", SOFTWARE);
//        sUriMatcher.addURI(Shop.AUTHORITY, "software_status/#", SOFTWARE_ID);
//        // 实例化查询列集合
//        navProjectionMap = new HashMap<String, String>();
//        // 添加查询列
//        navProjectionMap.put(DBHelper.FD_ID, DBHelper.FD_ID);
//        navProjectionMap.put(ShopNav.APK_VERSION, ShopNav.APK_VERSION);
//        navProjectionMap.put(ShopNav.DOWN_URL, ShopNav.DOWN_URL);
//        navProjectionMap.put(ShopNav.FULL_ICON_PIC_URL, ShopNav.FULL_ICON_PIC_URL);
//        navProjectionMap.put(ShopNav.ICON_NAME, ShopNav.ICON_NAME);
//        navProjectionMap.put(ShopNav.INDEX_CODE, ShopNav.INDEX_CODE);
//        navProjectionMap.put(ShopNav.CLASS_NAME, ShopNav.CLASS_NAME);
//        navProjectionMap.put(ShopNav.EXTRA_DATA, ShopNav.EXTRA_DATA);
//        navProjectionMap.put(ShopNav.INTRODUCTION, ShopNav.INTRODUCTION);
//        navProjectionMap.put(ShopNav.NAV_ID, ShopNav.NAV_ID);
//        navProjectionMap.put(ShopNav.PACKAGE_NAME, ShopNav.PACKAGE_NAME);
//        navProjectionMap.put(ShopNav.PAD_VERSION, ShopNav.PAD_VERSION);
//        navProjectionMap.put(ShopNav.PARENT_ID, ShopNav.PARENT_ID);
//        navProjectionMap.put(ShopNav.SORT_ID, ShopNav.SORT_ID);
//        navProjectionMap.put(ShopNav.STATUS, ShopNav.STATUS);
//        navProjectionMap.put(ShopNav.ERROR_REASON, ShopNav.ERROR_REASON);
//        navProjectionMap.put(ShopNav.TYPE, ShopNav.TYPE);
//        
//        adProjectionMap = new HashMap<String, String>();
//        adProjectionMap.put(DBHelper.FD_ID, DBHelper.FD_ID);
//        adProjectionMap.put(DBHelper.AD.AD_ID, DBHelper.AD.AD_ID);
//        adProjectionMap.put(DBHelper.AD.BEGIN_TIME, DBHelper.AD.BEGIN_TIME);
//        adProjectionMap.put(DBHelper.AD.DES, DBHelper.AD.DES);
//        adProjectionMap.put(DBHelper.AD.DOWN_URL, DBHelper.AD.DOWN_URL);
//        adProjectionMap.put(DBHelper.AD.END_TIME, DBHelper.AD.END_TIME);
//        adProjectionMap.put(DBHelper.AD.PLAY_TIME, DBHelper.AD.PLAY_TIME);
//        adProjectionMap.put(DBHelper.AD.SHOW_POSITION, DBHelper.AD.SHOW_POSITION);
//        adProjectionMap.put(DBHelper.AD.SORT_INDEX, DBHelper.AD.SORT_INDEX);
//        adProjectionMap.put(DBHelper.AD.TYPE, DBHelper.AD.TYPE);
//        adProjectionMap.put(DBHelper.AD.STATUS, DBHelper.AD.STATUS);
//        
//        shopProjectionMap = new HashMap<String, String>();
//        shopProjectionMap.put(DBHelper.FD_ID, DBHelper.FD_ID);
//        shopProjectionMap.put(Shop.SHOP_NAME, Shop.SHOP_NAME);
//        
//        softwareProjectionMap = new HashMap<String, String>();
//        softwareProjectionMap.put(DBHelper.FD_ID, DBHelper.FD_ID);
//        softwareProjectionMap.put(SoftwareStatus.SHOFTWARE_ID, SoftwareStatus.SHOFTWARE_ID);
//        softwareProjectionMap.put(SoftwareStatus.OCCUR_TIME, SoftwareStatus.OCCUR_TIME);
//    }
//
//    public boolean onCreate() {
//        dbHelper = new DBHelper(getContext());
//        return true;
//    }
//
//    /**
//     * 添加
//     */
//    public Uri insert(Uri uri, ContentValues values) {
//    	LogUtils.d("-----insert------provider:"+uri.toString()+"  "+sUriMatcher.match(uri));
//        switch (sUriMatcher.match(uri)) {
//            case NAV:
//            case NAV_ID:
//                long rowId = dbHelper.insert(DBHelper.TB_SHOP_NAV, values);
//                if (rowId > 0) {// 如果插入成功返回uri
//                    Uri empUri = ContentUris.withAppendedId(ShopNav.CONTENT_URI, rowId);
//                    getContext().getContentResolver().notifyChange(empUri, null);
//                    return empUri;
//                }
//                break;
//            case AD:
//            case AD_ID:
//                rowId = dbHelper.insert(DBHelper.TB_AD, values);
//                LogUtils.d("-----insert------provider ad:"+rowId);
//                if (rowId > 0) {// 如果插入成功返回uri
//                    Uri empUri = ContentUris.withAppendedId(DBHelper.AD.CONTENT_URI, rowId);
//                    getContext().getContentResolver().notifyChange(empUri, null);
//                    return empUri;
//                }
//                break;
//            case SHOP:
//            case SHOP_ID:
//                rowId = dbHelper.insert(DBHelper.TB_SHOP, values);
//                if (rowId > 0) {// 如果插入成功返回uri
//                    Uri empUri = ContentUris.withAppendedId(DBHelper.Shop.CONTENT_URI, rowId);
//                    getContext().getContentResolver().notifyChange(empUri, null);
//                    return empUri;
//                }
//                break;
//            case SOFTWARE:
//            case SOFTWARE_ID:
//                rowId = dbHelper.insert(DBHelper.TB_SOFTWARE_STATUS, values);
//                if (rowId > 0) {// 如果插入成功返回uri
//                    Uri empUri = ContentUris.withAppendedId(DBHelper.Shop.CONTENT_URI, rowId);
//                    getContext().getContentResolver().notifyChange(empUri, null);
//                    return empUri;
//                }
//                break;
//        }
//        return null;
//    }
//
//    /**
//     * 删除
//     */
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        int count = 0;
//        LogUtils.d("-----delete------provider:"+uri.toString());
//        switch (sUriMatcher.match(uri)) {
//            case NAV:
//                count = dbHelper.deleteAll(DBHelper.TB_SHOP_NAV, selection, selectionArgs);
//                break;
//            case NAV_ID:
//                String noteId = uri.getPathSegments().get(1);
//                count = dbHelper.deleteAll(DBHelper.TB_SHOP_NAV, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            case AD:
//                count = dbHelper.deleteAll(DBHelper.TB_AD, selection, selectionArgs);
//                break;
//            case AD_ID:
//                noteId = uri.getPathSegments().get(1);
//                count = dbHelper.deleteAll(DBHelper.TB_AD, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            case SHOP:
//                count = dbHelper.deleteAll(DBHelper.TB_SHOP, selection, selectionArgs);
//                break;
//            case SHOP_ID:
//                noteId = uri.getPathSegments().get(1);
//                count = dbHelper.deleteAll(DBHelper.TB_SHOP, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            case SOFTWARE:
//                count = dbHelper.deleteAll(DBHelper.TB_SOFTWARE_STATUS, selection, selectionArgs);
//                break;
//            case SOFTWARE_ID:
//                noteId = uri.getPathSegments().get(1);
//                count = dbHelper.deleteAll(DBHelper.TB_SOFTWARE_STATUS, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//
//    /**
//     * 获得类型
//     */
//    public String getType(Uri uri) {
//        int match = sUriMatcher.match(uri);
//        switch(match){
//            case AD:
//                return ShopNav.CONTENT_TYPE;
//            case AD_ID:
//                return ShopNav.CONTENT_ITEM_TYPE;
//            case NAV:
//                return DBHelper.AD.CONTENT_TYPE;
//            case NAV_ID:
//                return DBHelper.AD.CONTENT_ITEM_TYPE;
//            case SHOP:
//                return DBHelper.Shop.CONTENT_TYPE;
//            case SHOP_ID:
//                return DBHelper.Shop.CONTENT_ITEM_TYPE;
//            case SOFTWARE:
//                return DBHelper.SoftwareStatus.CONTENT_TYPE;
//            case SOFTWARE_ID:
//                return DBHelper.SoftwareStatus.CONTENT_ITEM_TYPE;
//        }
//        return null;
//    }
//
//    /**
//     * 查询
//     */
//    public Cursor query(Uri uri, String[] projection, String selection,
//            String[] selectionArgs, String sortOrder) {
//        LogUtils.d("-----query------provider:"+uri.toString());
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        String orderBy;
//        switch (sUriMatcher.match(uri)) {
//            case NAV:
//                qb.setTables(DBHelper.TB_SHOP_NAV);
//                qb.setProjectionMap(navProjectionMap);
//                orderBy = TextUtils.isEmpty(sortOrder)?ShopNav.DEFAULT_SORT_ORDER:sortOrder;
//                break;
//            case NAV_ID:
//                qb.setTables(DBHelper.TB_SHOP_NAV);
//                qb.setProjectionMap(navProjectionMap);
//                qb.appendWhere(DBHelper.FD_ID + "=" + uri.getPathSegments().get(1));
//                orderBy = TextUtils.isEmpty(sortOrder)?ShopNav.DEFAULT_SORT_ORDER:sortOrder;
//                break;
//            case AD:
//                qb.setTables(DBHelper.TB_AD);
//                qb.setProjectionMap(adProjectionMap);
//                orderBy = sortOrder;
//                break;
//            case AD_ID:
//                qb.setTables(DBHelper.TB_AD);
//                qb.setProjectionMap(adProjectionMap);
//                qb.appendWhere(DBHelper.FD_ID + "=" + uri.getPathSegments().get(1));
//                orderBy = sortOrder;
//                break;
//            case SHOP:
//                qb.setTables(DBHelper.TB_SHOP);
//                qb.setProjectionMap(shopProjectionMap);
//                orderBy = sortOrder;
//                break;
//            case SHOP_ID:
//                qb.setTables(DBHelper.TB_SHOP);
//                qb.setProjectionMap(shopProjectionMap);
//                qb.appendWhere(DBHelper.FD_ID + "=" + uri.getPathSegments().get(1));
//                orderBy = sortOrder;
//                break;
//            case SOFTWARE:
//                qb.setTables(DBHelper.TB_SOFTWARE_STATUS);
//                qb.setProjectionMap(softwareProjectionMap);
//                orderBy = sortOrder;
//                break;
//            case SOFTWARE_ID:
//                qb.setTables(DBHelper.TB_SOFTWARE_STATUS);
//                qb.setProjectionMap(softwareProjectionMap);
//                qb.appendWhere(DBHelper.FD_ID + "=" + uri.getPathSegments().get(1));
//                orderBy = sortOrder;
//                break;
//            default:
//                throw new IllegalArgumentException("Uri错误！ " + uri);
//        }
//        SQLiteDatabase db = null;
//        Cursor c = null;
//        try {
//            db = dbHelper.getReadableDatabase();
//            c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
//            c.setNotificationUri(getContext().getContentResolver(), uri);
//        } catch (Exception e) {
//        }
//        finally{
////            if(null!=db) db.close();
//        }
//        return c;
//    }
//
//    /**
//     * 更新
//     */
//    public int update(Uri uri, ContentValues values, String selection,
//            String[] selectionArgs) {
//        LogUtils.d("-----update------provider:"+sUriMatcher.match(uri));
//        int count = 0;
//        switch (sUriMatcher.match(uri)) {
//            case NAV:
//                count = dbHelper.update(DBHelper.TB_SHOP_NAV, values, selection, selectionArgs);
//                break;
//            case NAV_ID:
//                String noteId = uri.getPathSegments().get(1);
//                count = dbHelper.update(DBHelper.TB_SHOP_NAV, values, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            case AD:
//                count = dbHelper.update(DBHelper.TB_AD, values, selection, selectionArgs);
//                break;
//            case AD_ID:
//                noteId = uri.getPathSegments().get(1);
//                count = dbHelper.update(DBHelper.TB_AD, values, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            case SHOP:
//                count = dbHelper.update(DBHelper.TB_SHOP, values, selection, selectionArgs);
//                break;
//            case SHOP_ID:
//                noteId = uri.getPathSegments().get(1);
//                count = dbHelper.update(DBHelper.TB_SHOP, values, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            case SOFTWARE:
//                count = dbHelper.update(DBHelper.TB_SOFTWARE_STATUS, values, selection, selectionArgs);
//                break;
//            case SOFTWARE_ID:
//                noteId = uri.getPathSegments().get(1);
//                count = dbHelper.update(DBHelper.TB_SOFTWARE_STATUS, values, DBHelper.FD_ID + "=" + noteId
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
//                        selectionArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("错误的 URI " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//}
