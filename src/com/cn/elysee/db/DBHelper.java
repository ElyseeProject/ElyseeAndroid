package com.cn.elysee.db;

import com.cn.elysee.db.BaseSQLiteDatabase.DBUpdateListener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class DBHelper extends SQLiteOpenHelper
{

	/**
	 * 数据库更新监听器
	 */
	private DBUpdateListener mTadbUpdateListener;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库名字
	 * @param factory
	 *            可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标
	 * @param version
	 *            数据库版本
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库名字
	 * @param factory
	 *            可选的数据库游标工厂类，当查询(query)被提交时，该对象会被调用来实例化一个游标
	 * @param version
	 *            数据库版本
	 * @param tadbUpdateListener
	 *            数据库更新监听器
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version, DBUpdateListener tadbUpdateListener)
	{
		super(context, name, factory, version);
		this.mTadbUpdateListener = tadbUpdateListener;
	}

	/**
	 * 设置数据库更新监听器
	 * 
	 * @param mTadbUpdateListener
	 *            数据库更新监听器
	 */
	public void setOndbUpdateListener(DBUpdateListener tadbUpdateListener)
	{
		this.mTadbUpdateListener = tadbUpdateListener;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (mTadbUpdateListener != null)
		{
			mTadbUpdateListener.onUpgrade(db, oldVersion, newVersion);
		}
	}

}
