/**
 * 
 */
package com.yuhen.saleapp.chat;

import com.yuhen.saleapp.util.DateUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * 
 * @author ps
 * 
 */
public class ChatDatabase extends SQLiteOpenHelper {
	public static final String TAG = "ChatDatabase";
	private static final int VERSION = 1;

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param factory
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public ChatDatabase(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	public ChatDatabase(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public ChatDatabase(Context context, String name) {
		this(context, name, VERSION);
	}

	/**
	 * //该函数是在第一次创建的时候执行，实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//execSQL用于执行SQL语句
		createTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/** 
     * 新建一个表 
     * @param db 
     */  
    public void createTable(){  
    	SQLiteDatabase db = getWritableDatabase();  
        String sql = "create table if not exists chat_local_info(id int autoincrement," +
        		"chat_date varchar(8),chat_time varchar(8),chat_type char(1),from_user varchar(30)," +
        		"from_user_name varchar(50),to_user varchar(30),to_user_name varchar(50),chat_content varchar(500))";  
        try {  
            db.execSQL(sql);  
        } catch (SQLException e) {  
            Log.e(TAG, "create chat_local_info table failed",e);  
        }finally{
        	db.close();
        }
    }  
      
    /** 
     * 插入数据 
     */  
    public void insert(ContentValues values){  
    	SQLiteDatabase db = getWritableDatabase();  
        String sql = "insert into chat_local_info (chat_date,chat_time,chat_type,from_user," +
        		"from_user_name,to_user,to_user_name,chat_content) values (?,?,?,?,?,?,?,?)";  
        try { 
        	db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, DateUtil.today());
			stmt.bindString(2, DateUtil.today("HHmmss"));
			stmt.bindString(3, values.getAsString("chat_type"));
			stmt.bindString(4, values.getAsString("from_user"));
			stmt.bindString(5, values.getAsString("from_user_name"));
			stmt.bindString(6, values.getAsString("to_user"));
			stmt.bindString(7, values.getAsString("to_user_name"));
			stmt.bindString(8, values.getAsString("chat_content"));
			stmt.execute();  
		    stmt.clearBindings();
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLException e) {  
            Log.e(TAG, "insert chat_local_info failed",e);  
        }finally{
        	db.close();
        }
    }  
      
    /** 
     * 更新数据 
     */  
    public void update() {  
    	SQLiteDatabase db = getWritableDatabase();  
        String sql = "Update TestUsers set name = 'anhong',sex = 'men' where id = 2";  
        try {  
            db.execSQL(sql);  
        } catch (SQLException e) {  
            Log.e(TAG, "update failed");  
        }finally{
        	db.close();
        } 
    }  
      
    /** 
     * 删除数据 
     */  
    public void delete(String chat_id){  
    	SQLiteDatabase db = getWritableDatabase();  
        String sql = "delete from chat_local_info where id = "+ chat_id;  
        try {  
            db.execSQL(sql);  
        } catch (SQLException e) {  
            Log.e(TAG, "delete chat_local_info data ["+chat_id+"]failed", e);  
        }finally{
        	db.close();
        }
    }
    /** 
     * 删除数据 
     */  
    public Object query(String []projects, String selection){  
    	SQLiteDatabase db = getWritableDatabase();  
    	String sql = "select from chat_local_info where id = 2";  
    	try {  
    		db.execSQL(sql);  
    	} catch (SQLException e) {  
    		Log.i(TAG, "delete failed");  
    	}finally{
        	db.close();
        }
    	
    	return null;
    }
}
