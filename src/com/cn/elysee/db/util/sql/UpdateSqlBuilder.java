package com.cn.elysee.db.util.sql;

import java.lang.annotation.Annotation;
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
public class UpdateSqlBuilder extends SqlBuilder
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
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("UPDATE ");
		stringBuilder.append(tableName).append(" SET ");

		BaseArrayList needUpdate = getUpdateFields();
		for (int i = 0; i < needUpdate.size(); i++)
		{
			NameValuePair nameValuePair = needUpdate.get(i);
			stringBuilder
					.append(nameValuePair.getName())
					.append(" = ")
					.append(StringUtils.isNumeric(nameValuePair.getValue()
							.toString()) ? nameValuePair.getValue() : "'"
							+ nameValuePair.getValue() + "'");
			if (i + 1 < needUpdate.size())
			{
				stringBuilder.append(", ");
			}
		}
		if (!StringUtils.isEmpty(this.where))
		{
			stringBuilder.append(buildConditionString());
		}
		else
		{
			stringBuilder.append(buildWhere(buildWhere(this.entity)));
		}
		return stringBuilder.toString();
	}

	/**
	 * 创建Where语句
	 * 
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws TADBException
	 */
	public BaseArrayList buildWhere(Object entity)
			throws IllegalArgumentException, IllegalAccessException,
			DBException
	{
		Class<?> clazz = entity.getClass();
		BaseArrayList whereArrayList = new BaseArrayList();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{
			field.setAccessible(true);
			if (!DBUtils.isTransient(field))
			{
				if (DBUtils.isBaseDateType(field))
				{
					Annotation annotation = field
							.getAnnotation(PrimaryKey.class);
					if (annotation != null)
					{
						String columnName = DBUtils.getColumnByField(field);
						whereArrayList.add((columnName != null && !columnName
								.equals("")) ? columnName : field.getName(),
								field.get(entity).toString());
					}

				}
			}
		}
		if (whereArrayList.isEmpty()) { throw new DBException(
				"不能创建Where条件，语句"); }
		return whereArrayList;
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
					if (annotation == null || !annotation.autoIncrement())
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
