package com.cn.elysee.db;

import com.cn.elysee.db.BaseSQLiteDatabase.DBUpdateListener;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author hzx 
 * 2014��4��18��
 * @version V1.0
 */
public class DBHelper extends SQLiteOpenHelper
{

	/**
	 * ���ݿ���¼�����
	 */
	private DBUpdateListener mTadbUpdateListener;

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            ��ѡ�����ݿ��α깤���࣬����ѯ(query)���ύʱ���ö���ᱻ������ʵ����һ���α�
	 * @param version
	 *            ���ݿ�汾
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            ��ѡ�����ݿ��α깤���࣬����ѯ(query)���ύʱ���ö���ᱻ������ʵ����һ���α�
	 * @param version
	 *            ���ݿ�汾
	 * @param tadbUpdateListener
	 *            ���ݿ���¼�����
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version, DBUpdateListener tadbUpdateListener)
	{
		super(context, name, factory, version);
		this.mTadbUpdateListener = tadbUpdateListener;
	}

	/**
	 * �������ݿ���¼�����
	 * 
	 * @param mTadbUpdateListener
	 *            ���ݿ���¼�����
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
