package com.cn.elysee.db.util.sql;

import java.lang.reflect.Field;

import org.apache.http.NameValuePair;

import com.cn.elysee.common.StringUtils;
import com.cn.elysee.db.annotation.PrimaryKey;
import com.cn.elysee.db.entity.BaseArrayList;
import com.cn.elysee.db.util.DBUtils;
import com.cn.elysee.exception.DBException;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class InsertSqlBuilder extends SqlBuilder
{

	@Override
	public void onPreGetStatement() throws DBException,
			IllegalArgumentException, IllegalAccessException
	{
		if (getUpdateFields() == null)
		{
			setUpdateFields(getFieldsAndValue(entity));
		}
		super.onPreGetStatement();
	}

	@Override
	public String buildSql() throws DBException, IllegalArgumentException,
			IllegalAccessException
	{
		StringBuilder columns = new StringBuilder(256);
		StringBuilder values = new StringBuilder(256);
		columns.append("INSERT INTO ");
		columns.append(tableName).append(" (");
		values.append("(");
		BaseArrayList updateFields = getUpdateFields();
		if (updateFields != null)
		{
			for (int i = 0; i < updateFields.size(); i++)
			{
				NameValuePair nameValuePair = updateFields.get(i);
				columns.append(nameValuePair.getName());
				values.append(StringUtils
						.isNumeric(nameValuePair.getValue() != null ? nameValuePair
								.getValue().toString() : "") ? nameValuePair
						.getValue() : "'" + nameValuePair.getValue() + "'");
				if (i + 1 < updateFields.size())
				{
					columns.append(", ");
					values.append(", ");
				}
			}
		}
		else
		{
			throw new DBException("插入数据有误！");
		}
		columns.append(") values ");
		values.append(")");
		columns.append(values);
		return columns.toString();
	}

	/**
	 * 从实体加载,更新的数据
	 * 
	 * @return
	 * @throws TADBException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static BaseArrayList getFieldsAndValue(Object entity)
			throws DBException, IllegalArgumentException,
			IllegalAccessException
	{
		BaseArrayList arrayList = new BaseArrayList();
		if (entity == null) { throw new DBException("没有加载实体类！"); }
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{

			if (!DBUtils.isTransient(field))
			{
				if (DBUtils.isBaseDateType(field))
				{
					PrimaryKey annotation = field
							.getAnnotation(PrimaryKey.class);
					if (annotation != null && annotation.autoIncrement())
					{

					}
					else
					{
						String columnName = DBUtils.getColumnByField(field);
						field.setAccessible(true);
						arrayList
								.add((columnName != null && !columnName
										.equals("")) ? columnName : field
										.getName(),
										field.get(entity) == null ? null
												: field.get(entity).toString());
					}

				}
			}
		}
		return arrayList;
	}

}
