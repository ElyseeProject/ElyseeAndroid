package com.cn.elysee.db.util.sql;

import java.util.regex.Pattern;

import android.text.TextUtils;

import com.cn.elysee.exception.DBException;

/**
 * @author hzx 
 * 2014��4��18��
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
	 * ������ѯ���ֶ�
	 * 
	 * @param distinct
	 *            �����ظ������Ϊtrue������,false���ù�
	 * @param table
	 *            ����
	 * @param columns
	 *            ��Ҫ��ѯ����
	 * @param selection
	 *            ��ʽ������Ϊ SQL WHERE�Ӿ�(����WHERE����)�� ����null���ظ�����������С�
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy���
	 * @param having
	 *            having���
	 * @param orderBy
	 *            orderBy���
	 * @param limit
	 *            limit���
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
