package com.cn.elysee.db.util;

import com.cn.elysee.db.util.sql.DeleteSqlBuilder;
import com.cn.elysee.db.util.sql.InsertSqlBuilder;
import com.cn.elysee.db.util.sql.QuerySqlBuilder;
import com.cn.elysee.db.util.sql.SqlBuilder;
import com.cn.elysee.db.util.sql.UpdateSqlBuilder;

/**
 * @author hzx 
 * 2014年4月18日
 * @Description Sql构建器工厂,生成sql语句构建器
 * @version V1.0
 */
public class SqlBuilderFactory
{
	private static SqlBuilderFactory instance;
	/**
	 * 调用getSqlBuilder(int operate)返回插入sql语句构建器传入的参数
	 */
	public static final int INSERT = 0;
	/**
	 * 调用getSqlBuilder(int operate)返回查询sql语句构建器传入的参数
	 */
	public static final int SELECT = 1;
	/**
	 * 调用getSqlBuilder(int operate)返回删除sql语句构建器传入的参数
	 */
	public static final int DELETE = 2;
	/**
	 * 调用getSqlBuilder(int operate)返回更新sql语句构建器传入的参数
	 */
	public static final int UPDATE = 3;

	/**
	 * 单例模式获得Sql构建器工厂
	 * 
	 * @return sql构建器
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
	 * 获得sql构建器
	 * 
	 * @param operate
	 * @return 构建器
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
