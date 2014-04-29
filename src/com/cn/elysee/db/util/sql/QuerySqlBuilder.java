package com.cn.elysee.db.util.sql;

import java.util.regex.Pattern;

import android.text.TextUtils;

import com.cn.elysee.exception.DBException;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class QuerySqlBuilder extends SqlBuilder
{

	protected Pattern sLimitPattern = Pattern
			.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

	@Override
	public String buildSql() throws DBException, IllegalArgumentException,
			IllegalAccessException
	{
		return buildQueryString();
	}

	/**
	 * 创建查询的字段
	 * 
	 * @param distinct
	 *            限制重复，如过为true则限制,false则不用管
	 * @param table
	 *            表名
	 * @param columns
	 *            需要查询的列
	 * @param selection
	 *            格式化的作为 SQL WHERE子句(不含WHERE本身)。 传递null返回给定表的所有行。
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy语句
	 * @param having
	 *            having语句
	 * @param orderBy
	 *            orderBy语句
	 * @param limit
	 *            limit语句
	 * @return
	 */
	public String buildQueryString()
	{
		if (TextUtils.isEmpty(groupBy) && !TextUtils.isEmpty(having)) { throw new IllegalArgumentException(
				"HAVING clauses are only permitted when using a groupBy clause"); }
		if (!TextUtils.isEmpty(limit)
				&& !sLimitPattern.matcher(limit).matches()) { throw new IllegalArgumentException(
				"invalid LIMIT clauses:" + limit); }

		StringBuilder query = new StringBuilder(120);
		query.append("SELECT ");
		if (distinct)
		{
			query.append("DISTINCT ");
		}
		query.append("* ");
		query.append("FROM ");
		query.append(tableName);
		appendClause(query, " WHERE ", where);
		appendClause(query, " GROUP BY ", groupBy);
		appendClause(query, " HAVING ", having);
		appendClause(query, " ORDER BY ", orderBy);
		appendClause(query, " LIMIT ", limit);
		return query.toString();
	}

}
