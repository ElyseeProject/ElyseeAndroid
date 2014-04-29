package com.cn.elysee.db;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import com.cn.elysee.db.BaseSQLiteDatabase.DBParams;
import com.cn.elysee.db.BaseSQLiteDatabase.DBUpdateListener;
import com.cn.elysee.util.Logger;

import android.content.Context;

/**
 * @author hzx 
 * 2014��4��18��
 * @version V1.0
 */
public class SQLiteDatabasePool
{
	private String testTable = "Sqlite_master"; // ���������Ƿ���õĲ��Ա�����Ĭ��Sqlite_masterΪ���Ա�
	private int initialSQLiteDatabase = 2; // ���ӳصĳ�ʼ��С
	private int incrementalSQLiteDatabase = 2;// ���ӳ��Զ����ӵĴ�С
	private int maxSQLiteDatabase = 10; // ���ӳ����Ĵ�С
	private Vector<PooledSQLiteDatabase> pSQLiteDatabases = null; // ������ӳ������ݿ����ӵ�����
	private Context context;
	private DBParams params;
	private DBUpdateListener mDBUpdateListener; // ����ʱ������
	private Boolean isWrite = false;
	private static HashMap<String, SQLiteDatabasePool> poolMap = new HashMap<String, SQLiteDatabasePool>();

	/**
	 * ����ģʽ
	 * 
	 * @param context
	 *            ������
	 * @param context
	 *            ������
	 * @param params
	 *            ���ݿ����ò���
	 * @param isWrite
	 *            �����ݿ������ isWriteΪtrue,�������ʱ�׳�����
	 */
	public synchronized static SQLiteDatabasePool getInstance(
			Context context, DBParams params, Boolean isWrite)
	{
		String dbName = params.getDbName().trim();
		SQLiteDatabasePool pool = poolMap.get(dbName);
		if (pool == null)
		{
			pool = new SQLiteDatabasePool(context, params, isWrite);
			poolMap.put(dbName.trim(), pool);
		}
		return pool;
	}

	/**
	 * ����ģʽ
	 * 
	 * @param context
	 *            ������
	 */
	public static SQLiteDatabasePool getInstance(Context context)
	{
		DBParams params = new DBParams();
		return getInstance(context, params, false);
	}

	/**
	 * ����ģʽ
	 * 
	 * @param context
	 *            ������
	 * @param dbName
	 *            ���ݿ�����
	 * @param dbVersion
	 *            �汾
	 * @param isWrite
	 *            �����ݿ������ isWriteΪtrue,�������ʱ�׳�����
	 */
	public static SQLiteDatabasePool getInstance(Context context,
			String dbName, int dbVersion, Boolean isWrite)
	{
		DBParams params = new DBParams(dbName, dbVersion);
		return getInstance(context, params, isWrite);
	}

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ������
	 * @param params
	 *            ���ݿ����ò���
	 * @param isWrite
	 *            �����ݿ������ isWriteΪtrue,�������ʱ�׳�����
	 */
	public SQLiteDatabasePool(Context context, DBParams params,
			Boolean isWrite)
	{
		this.context = context;
		this.params = params;
		this.isWrite = isWrite;
	}

	/**
	 * ���������ĵļ�����
	 * 
	 * @param dbUpdateListener
	 */
	public void setOnDbUpdateListener(DBUpdateListener dbUpdateListener)
	{
		this.mDBUpdateListener = dbUpdateListener;
	}

	/**
	 * 
	 * �������ӳصĳ�ʼ��С
	 * 
	 * @return ��ʼ���ӳ��пɻ�õ���������
	 */
	public int getInitialSQLiteDatabase()
	{
		return initialSQLiteDatabase;
	}

	/**
	 * �������ӳصĳ�ʼ��С
	 * 
	 * @param �������ó�ʼ���ӳ������ӵ�����
	 */
	public void setInitialSQLiteDatabase(int initialSQLiteDatabase)
	{
		this.initialSQLiteDatabase = initialSQLiteDatabase;
	}

	/**
	 * �������ӳ��Զ����ӵĴ�С ��
	 * 
	 * @return ���ӳ��Զ����ӵĴ�С
	 */
	public int getIncrementalSQLiteDatabase()
	{
		return incrementalSQLiteDatabase;
	}

	/**
	 * �������ӳ��Զ����ӵĴ�С
	 * 
	 * @param ���ӳ��Զ����ӵĴ�С
	 */
	public void setIncrementalSQLiteDatabase(int incrementalSQLiteDatabase)
	{
		this.incrementalSQLiteDatabase = incrementalSQLiteDatabase;
	}

	/**
	 * 
	 * �������ӳ������Ŀ�����������
	 * 
	 * @return ���ӳ������Ŀ�����������
	 */
	public int getMaxSQLiteDatabase()
	{
		return maxSQLiteDatabase;
	}

	/**
	 * �������ӳ��������õ���������
	 * 
	 * @param �������ӳ��������õ���������ֵ
	 */
	public void setMaxSQLiteDatabase(int maxSQLiteDatabase)
	{
		this.maxSQLiteDatabase = maxSQLiteDatabase;
	}

	/**
	 * ���ò��Ա������
	 * 
	 * @param testTable
	 *            String ���Ա������
	 */

	public void setTestTable(String testTable)
	{
		this.testTable = testTable;
	}

	/**
	 * ��ȡ�������ݿ�������
	 * 
	 * @return �������ݿ�������
	 */

	public String getTestTable()
	{
		return this.testTable;
	}

	/**
	 * 
	 * ����һ�����ݿ����ӳأ����ӳ��еĿ������ӵ������������Ա initialSQLiteDatabase �����õ�ֵ
	 */
	public synchronized void createPool()
	{
		// ȷ�����ӳ�û�д���
		// ������ӳؼ��������ˣ��������ӵ����� sqLiteDatabases ����Ϊ��
		if (pSQLiteDatabases != null) { return; // ��������������򷵻�
		}
		// �����������ӵ����� , ��ʼʱ�� 0 ��Ԫ��
		pSQLiteDatabases = new Vector<PooledSQLiteDatabase>();
		// ���� initialConnections �����õ�ֵ���������ӡ�
		createSQLiteDatabase(this.initialSQLiteDatabase);
		Logger.i(SQLiteDatabasePool.this, " ���ݿ����ӳش����ɹ��� ");
	}

	/**
	 * ������ numSQLiteDatabase ָ����Ŀ�����ݿ����� , ������Щ���� ���� numSQLiteDatabase ������
	 * 
	 * @param numSQLiteDatabase
	 *            Ҫ���������ݿ����ӵ���Ŀ
	 */

	private void createSQLiteDatabase(int numSQLiteDatabase)
	{

		// ѭ������ָ����Ŀ�����ݿ�����
		for (int x = 0; x < numSQLiteDatabase; x++)
		{
			// �Ƿ����ӳ��е����ݿ����ӵ����������ﵽ������ֵ�����Ա maxSQLiteDatabase
			// ָ������� maxSQLiteDatabase Ϊ 0 ��������ʾ��������û�����ơ�
			// ��������������ﵽ��󣬼��˳���
			if (this.maxSQLiteDatabase > 0
					&& this.pSQLiteDatabases.size() >= this.maxSQLiteDatabase)
			{
				break;
			}
			try
			{
				// ����һ��TASQLiteDatabase�����ӳ���
				pSQLiteDatabases.addElement(new PooledSQLiteDatabase(
						newSQLiteDatabase()));
			}
			catch (Exception e)
			{
				Logger.i(SQLiteDatabasePool.this,
						" �������ݿ�����ʧ�ܣ� " + e.getMessage());
			}
			Logger.i(SQLiteDatabasePool.this, "���ݿ����Ӽ����� ......");
		}
	}

	/**
	 * ����һ���µ����ݿ����Ӳ�������
	 * 
	 * @return ����һ���´��������ݿ�����
	 */

	private BaseSQLiteDatabase newSQLiteDatabase()
	{

		// ����һ�����ݿ�����
		BaseSQLiteDatabase sqliteDatabase = new BaseSQLiteDatabase(context, params);
		sqliteDatabase.openDatabase(mDBUpdateListener, isWrite);
		return sqliteDatabase; // ���ش������µ����ݿ�����
	}

	/**
	 * ͨ������ getFreeSQLiteDatabase() ��������һ�����õ����ݿ����� ,
	 * �����ǰû�п��õ����ݿ����ӣ����Ҹ�������ݿ����Ӳ��ܴ� ���������ӳش�С�����ƣ����˺����ȴ�һ���ٳ��Ի�ȡ��
	 * 
	 * @return ����һ�����õ����ݿ����Ӷ���
	 */

	public synchronized BaseSQLiteDatabase getSQLiteDatabase()
	{
		// ȷ�����ӳؼ�������
		if (pSQLiteDatabases == null) { return null; // ���ӳػ�û�������򷵻� null
		}

		BaseSQLiteDatabase sqliteDatabase = getFreeSQLiteDatabase(); // ���һ�����õ����ݿ�����

		// ���Ŀǰû�п���ʹ�õ����ӣ������е����Ӷ���ʹ����
		while (sqliteDatabase == null)
		{
			// ��һ������
			wait(250);
			sqliteDatabase = getFreeSQLiteDatabase(); // �������ԣ�ֱ����ÿ��õ����ӣ����
			// getFreeConnection() ���ص�Ϊ null
			// ���������һ�����Ӻ�Ҳ���ɻ�ÿ�������
		}
		return sqliteDatabase;// ���ػ�õĿ��õ�����
	}

	/**
	 * �����������ӳ����� pSQLiteDatabases �з���һ�����õĵ����ݿ����ӣ���� ��ǰû�п��õ����ݿ����ӣ������������
	 * incrementalSQLiteDatabase ���� ��ֵ�����������ݿ����ӣ����������ӳ��С� ������������е������Զ���ʹ���У��򷵻�
	 * null
	 * 
	 * @return ����һ�����õ����ݿ�����
	 */

	private BaseSQLiteDatabase getFreeSQLiteDatabase()
	{
		// �����ӳ��л��һ�����õ����ݿ�����
		BaseSQLiteDatabase sqLiteDatabase = findFreeSQLiteDatabase();
		if (sqLiteDatabase == null)
		{
			// ���Ŀǰ���ӳ���û�п��õ�����
			// ����һЩ����
			createSQLiteDatabase(incrementalSQLiteDatabase);
			// ���´ӳ��в����Ƿ��п�������
			sqLiteDatabase = findFreeSQLiteDatabase();
			if (sqLiteDatabase == null)
			{
				// ����������Ӻ��Ի�ò������õ����ӣ��򷵻� null
				return null;
			}
		}
		return sqLiteDatabase;
	}

	/**
	 * �������ӳ������е����ӣ�����һ�����õ����ݿ����ӣ� ���û�п��õ����ӣ����� null
	 * 
	 * @return ����һ�����õ����ݿ�����
	 */

	private BaseSQLiteDatabase findFreeSQLiteDatabase()
	{
		BaseSQLiteDatabase sqliteDatabase = null;
		PooledSQLiteDatabase pSQLiteDatabase = null;

		// ������ӳ����������еĶ���
		Enumeration<PooledSQLiteDatabase> enumerate = pSQLiteDatabases
				.elements();

		// �������еĶ��󣬿��Ƿ��п��õ�����
		while (enumerate.hasMoreElements())
		{
			pSQLiteDatabase = enumerate.nextElement();
			if (!pSQLiteDatabase.isBusy())
			{

				// ����˶���æ�������������ݿ����Ӳ�������Ϊæ
				sqliteDatabase = pSQLiteDatabase.getSqliteDatabase();
				pSQLiteDatabase.setBusy(true);
				// ���Դ������Ƿ����
				if (!testSQLiteDatabase(sqliteDatabase))
				{
					// ��������Ӳ��������ˣ��򴴽�һ���µ����ӣ� // ���滻�˲����õ����Ӷ����������ʧ�ܣ����� null
					sqliteDatabase = newSQLiteDatabase();
					pSQLiteDatabase.setSqliteDatabase(sqliteDatabase);
				}
				break;
				// �����ҵ�һ�����õ����ӣ��˳�
			}
		}
		return sqliteDatabase;// �����ҵ����Ŀ�������
	}

	/**
	 * ����һ�������Ƿ���ã���������ã��ص��������� false ������÷��� true
	 * 
	 * @param sqliteDatabase
	 *            ��Ҫ���Ե����ݿ�����
	 * @return ���� true ��ʾ�����ӿ��ã� false ��ʾ������
	 */
	private boolean testSQLiteDatabase(BaseSQLiteDatabase sqliteDatabase)
	{

		if (sqliteDatabase != null) { return sqliteDatabase
				.testSQLiteDatabase(); }
		// ���ӿ��ã����� true
		return false;
	}

	/**
	 * �˺�������һ�����ݿ����ӵ����ӳ��У����Ѵ�������Ϊ���С� ����ʹ�����ӳػ�õ����ݿ����Ӿ�Ӧ�ڲ�ʹ�ô�����ʱ��������
	 * 
	 * @param �践�ص����ӳ��е����Ӷ���
	 */

	public void releaseSQLiteDatabase(BaseSQLiteDatabase sqLiteDatabase)
	{
		// ȷ�����ӳش��ڣ��������û�д����������ڣ���ֱ�ӷ���
		if (pSQLiteDatabases == null)
		{
			Logger.d(SQLiteDatabasePool.this, " ���ӳز����ڣ��޷����ش����ӵ����ӳ��� !");
			return;
		}
		PooledSQLiteDatabase pSqLiteDatabase = null;

		Enumeration<PooledSQLiteDatabase> enumerate = pSQLiteDatabases
				.elements();

		// �������ӳ��е��������ӣ��ҵ����Ҫ���ص����Ӷ���
		while (enumerate.hasMoreElements())
		{
			pSqLiteDatabase = enumerate.nextElement();

			// ���ҵ����ӳ��е�Ҫ���ص����Ӷ���
			if (sqLiteDatabase == pSqLiteDatabase.getSqliteDatabase())
			{

				// �ҵ��� , ���ô�����Ϊ����״̬
				pSqLiteDatabase.setBusy(false);
				break;
			}
		}
	}

	/**
	 * ˢ�����ӳ������е����Ӷ���
	 * 
	 */

	public synchronized void refreshSQLiteDatabase()
	{

		// ȷ�����ӳؼ����´���
		if (pSQLiteDatabases == null)
		{
			Logger.d(SQLiteDatabasePool.this, " ���ӳز����ڣ��޷�ˢ�� !");
			return;
		}

		PooledSQLiteDatabase pSqLiteDatabase = null;
		Enumeration<PooledSQLiteDatabase> enumerate = pSQLiteDatabases
				.elements();
		while (enumerate.hasMoreElements())
		{

			// ���һ�����Ӷ���
			pSqLiteDatabase = enumerate.nextElement();

			// �������æ��� 5 �� ,5 ���ֱ��ˢ��
			if (pSqLiteDatabase.isBusy())
			{
				wait(5000); // �� 5 ��
			}

			// �رմ����ӣ���һ���µ����Ӵ�������
			closeSQLiteDatabase(pSqLiteDatabase.getSqliteDatabase());
			pSqLiteDatabase.setSqliteDatabase(newSQLiteDatabase());
			pSqLiteDatabase.setBusy(false);
		}
	}

	/**
	 * �ر����ӳ������е����ӣ���������ӳء�
	 */

	public synchronized void closeSQLiteDatabase()
	{

		// ȷ�����ӳش��ڣ���������ڣ�����
		if (pSQLiteDatabases == null)
		{
			Logger.d(SQLiteDatabasePool.this, "���ӳز����ڣ��޷��ر� !");
			return;
		}
		PooledSQLiteDatabase pSqLiteDatabase = null;
		Enumeration<PooledSQLiteDatabase> enumerate = pSQLiteDatabases
				.elements();
		while (enumerate.hasMoreElements())
		{
			// ���һ�����Ӷ���
			pSqLiteDatabase = enumerate.nextElement();

			// ���æ���� 5 ��
			if (pSqLiteDatabase.isBusy())
			{
				wait(5000); // �� 5 ��
			}
			// 5 ���ֱ�ӹر���
			closeSQLiteDatabase(pSqLiteDatabase.getSqliteDatabase());
			// �����ӳ�������ɾ����
			pSQLiteDatabases.removeElement(pSqLiteDatabase);
		}
		// �����ӳ�Ϊ��
		pSQLiteDatabases = null;
	}

	/**
	 * �ر�һ�����ݿ�����
	 * 
	 * @param ��Ҫ�رյ����ݿ�����
	 */

	private void closeSQLiteDatabase(BaseSQLiteDatabase sqlLiteDatabase)
	{
		sqlLiteDatabase.close();
	}

	/**
	 * ʹ����ȴ������ĺ�����
	 * 
	 * @param �����ĺ�����
	 */

	private void wait(int mSeconds)
	{
		try
		{
			Thread.sleep(mSeconds);
		}
		catch (InterruptedException e)
		{
		}
	}

	/**
	 * �ڲ�ʹ�õ����ڱ������ӳ������Ӷ������ ��������������Ա��һ�������ݿ�����ӣ���һ����ָʾ�������Ƿ� ����ʹ�õı�־��
	 */

	class PooledSQLiteDatabase
	{
		BaseSQLiteDatabase sqliteDatabase = null;// ���ݿ�����
		boolean busy = false; // �������Ƿ�����ʹ�õı�־��Ĭ��û������ʹ��

		// ���캯��������һ�� TASQLiteDatabase ����һ�� PooledSQLiteDatabase ����
		public PooledSQLiteDatabase(BaseSQLiteDatabase sqliteDatabase)
		{
			this.sqliteDatabase = sqliteDatabase;
		}

		// ���ش˶����е�����
		public BaseSQLiteDatabase getSqliteDatabase()
		{
			return sqliteDatabase;
		}

		// ���ô˶���ģ�����
		public void setSqliteDatabase(BaseSQLiteDatabase sqliteDatabase)
		{
			this.sqliteDatabase = sqliteDatabase;
		}

		// ��ö��������Ƿ�æ
		public boolean isBusy()
		{
			return busy;
		}

		// ���ö������������æ
		public void setBusy(boolean busy)
		{
			this.busy = busy;
		}
	}
}
