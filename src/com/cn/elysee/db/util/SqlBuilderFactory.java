package com.cn.elysee.db.util;

import com.cn.elysee.db.util.sql.DeleteSqlBuilder;
import com.cn.elysee.db.util.sql.InsertSqlBuilder;
import com.cn.elysee.db.util.sql.QuerySqlBuilder;
import com.cn.elysee.db.util.sql.SqlBuilder;
import com.cn.elysee.db.util.sql.UpdateSqlBuilder;

/**
 * @author hzx 
 * 2014��4��18��
 * @Description Sql����������,����sql��乹����
 * @version V1.0
 */
public class SqlBuilderFactory
{
	private static SqlBuilderFactory instance;
	/**
	 * ����getSqlBuilder(int operate)���ز���sql��乹��������Ĳ���
	 */
	public static final int INSERT = 0;
	/**
	 * ����getSqlBuilder(int operate)���ز�ѯsql��乹��������Ĳ���
	 */
	public static final int SELECT = 1;
	/**
	 * ����getSqlBuilder(int operate)����ɾ��sql��乹��������Ĳ���
	 */
	public static final int DELETE = 2;
	/**
	 * ����getSqlBuilder(int operate)���ظ���sql��乹��������Ĳ���
	 */
	public static final int UPDATE = 3;

	/**
	 * ����ģʽ���Sql����������
	 * 
	 * @return sql������
	 */
	public static SqlBuilderFactory getInstance()
	{
		if (instance == null)
		{
			instance = new SqlBuilderFactory();
		}
		return instance;
	}

	/**
	 * ���sql������
	 * 
	 * @param operate
	 * @return ������
	 */
	public synchronized SqlBuilder getSqlBuilder(int operate)
	{
		SqlBuilder sqlBuilder = null;
		switch (operate)
		{
		case INSERT:
			sqlBuilder = new InsertSqlBuilder();
			break;
		case SELECT:
			sqlBuilder = new QuerySqlBuilder();
			break;
		case DELETE:
			sqlBuilder = new DeleteSqlBuilder();
			break;
		case UPDATE:
			sqlBuilder = new UpdateSqlBuilder();
			break;
		default:
			break;
		}
		return sqlBuilder;
	}
}
