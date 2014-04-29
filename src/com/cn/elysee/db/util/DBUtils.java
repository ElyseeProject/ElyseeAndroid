package com.cn.elysee.db.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.cn.elysee.common.StringUtils;
import com.cn.elysee.db.annotation.Column;
import com.cn.elysee.db.annotation.PrimaryKey;
import com.cn.elysee.db.annotation.TableName;
import com.cn.elysee.db.annotation.Transient;
import com.cn.elysee.db.entity.BaseHashMap;
import com.cn.elysee.db.entity.PKProperyEntity;
import com.cn.elysee.db.entity.PropertyEntity;
import com.cn.elysee.db.entity.TableInfoEntity;
import com.cn.elysee.exception.DBException;

import android.database.Cursor;

/**
 * @author hzx 
 * 2014��4��18��
 * @version V1.0
 */
public class DBUtils
{
	/**
	 * ͨ��Cursor��ȡһ��ʵ������
	 * 
	 * @param clazz
	 *            ʵ������
	 * @param cursor
	 *            ���ݼ���
	 * @return ��Ӧʵ��List����
	 */
	public static <T> List<T> getListEntity(Class<T> clazz, Cursor cursor)
	{
		List<T> queryList = EntityBuilder.buildQueryList(clazz, cursor);
		return queryList;
	}

	/**
	 * �������ݱ���һ�е�����
	 * 
	 * @param cursor
	 *            ���ݼ���
	 * @return TAHashMap��������
	 */
	public static BaseHashMap<String> getRowData(Cursor cursor)
	{
		if (cursor != null && cursor.getColumnCount() > 0)
		{
			BaseHashMap<String> hashMap = new BaseHashMap<String>();
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++)
			{
				hashMap.put(cursor.getColumnName(i), cursor.getString(i));
			}
			return hashMap;
		}
		return null;
	}

	/**
	 * ����ʵ���� ��� ʵ�����Ӧ�ı���
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz)
	{
		TableName table = clazz
				.getAnnotation(TableName.class);
		if (table == null || StringUtils.isEmpty(table.name()))
		{
			// ��û��ע���ʱ��Ĭ�������������Ϊ����,���ѵ㣨.���滻Ϊ�»���(_)
			return clazz.getName().toLowerCase().replace('.', '_');
		}
		return table.name();

	}

	/**
	 * ���������ֶ�
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz)
	{
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null)
		{

			for (Field field : fields)
			{ // ��ȡIDע��
				if (field.getAnnotation(PrimaryKey.class) != null)
				{
					primaryKeyField = field;
					break;
				}
			}
			if (primaryKeyField == null)
			{ // û��IDע��
				for (Field field : fields)
				{
					if ("_id".equals(field.getName()))
					{
						primaryKeyField = field;
						break;
					}
				}
				if (primaryKeyField == null)
				{ // ���û��_id���ֶ�
					for (Field field : fields)
					{
						if ("id".equals(field.getName()))
						{
							primaryKeyField = field;
							break;
						}
					}
				}
			}
		}
		else
		{
			throw new RuntimeException("this model[" + clazz + "] has no field");
		}
		return primaryKeyField;
	}

	/**
	 * ����������
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz)
	{
		Field f = getPrimaryKeyField(clazz);
		return f == null ? "id" : f.getName();
	}

	/**
	 * �������ݿ��ֶ�����
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return ���ݿ���ֶ�����
	 */
	public static List<PropertyEntity> getPropertyList(Class<?> clazz)
	{

		List<PropertyEntity> plist = new ArrayList<PropertyEntity>();
		try
		{
			Field[] fields = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for (Field field : fields)
			{
				if (!DBUtils.isTransient(field))
				{
					if (DBUtils.isBaseDateType(field))
					{

						if (field.getName().equals(primaryKeyFieldName)) // ��������
							continue;

						PKProperyEntity property = new PKProperyEntity();

						property.setColumnName(DBUtils
								.getColumnByField(field));
						property.setName(field.getName());
						property.setType(field.getType());
						property.setDefaultValue(DBUtils
								.getPropertyDefaultValue(field));
						plist.add(property);
					}
				}
			}
			return plist;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * �����������sql���
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return �������sql���
	 * @throws TADBException
	 */
	public static String creatTableSql(Class<?> clazz) throws DBException
	{
		TableInfoEntity tableInfoEntity = TableInfofactory.getInstance()
				.getTableInfoEntity(clazz);

		PKProperyEntity pkProperyEntity = null;
		pkProperyEntity = tableInfoEntity.getPkProperyEntity();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(tableInfoEntity.getTableName());
		strSQL.append(" ( ");

		if (pkProperyEntity != null)
		{
			Class<?> primaryClazz = pkProperyEntity.getType();
			if (primaryClazz == int.class || primaryClazz == Integer.class)
				if (pkProperyEntity.isAutoIncrement())
				{
					strSQL.append("\"").append(pkProperyEntity.getColumnName())
							.append("\"    ")
							.append("INTEGER PRIMARY KEY AUTOINCREMENT,");
				}
				else
				{
					strSQL.append("\"").append(pkProperyEntity.getColumnName())
							.append("\"    ").append("INTEGER PRIMARY KEY,");
				}
			else strSQL.append("\"").append(pkProperyEntity.getColumnName())
					.append("\"    ").append("TEXT PRIMARY KEY,");
		}
		else
		{
			strSQL.append("\"").append("id").append("\"    ")
					.append("INTEGER PRIMARY KEY AUTOINCREMENT,");
		}

		Collection<PropertyEntity> propertys = tableInfoEntity
				.getPropertieArrayList();
		for (PropertyEntity property : propertys)
		{
			strSQL.append("\"").append(property.getColumnName());
			strSQL.append("\",");
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" )");
		return strSQL.toString();
	}

	/**
	 * ��� �ֶ��Ƿ��Ѿ�����עΪ �����ݿ��ֶ�
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isTransient(Field field)
	{
		return field.getAnnotation(Transient.class) != null;
	}

	/**
	 * ����Ƿ�������
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isPrimaryKey(Field field)
	{
		return field.getAnnotation(PrimaryKey.class) != null;
	}

	/**
	 * ����Ƿ�����
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isAutoIncrement(Field field)
	{
		PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
		if (null != primaryKey) { return primaryKey.autoIncrement(); }
		return false;
	}

	/**
	 * �Ƿ�Ϊ��������������
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isBaseDateType(Field field)
	{
		Class<?> clazz = field.getType();
		return clazz.equals(String.class) || clazz.equals(Integer.class)
				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.equals(java.util.Date.class)
				|| clazz.equals(java.sql.Date.class) || clazz.isPrimitive();
	}

	/**
	 * ��ȡĳ����
	 * 
	 * @param field
	 * @return
	 */
	public static String getColumnByField(Field field)
	{
		Column column = field.getAnnotation(Column.class);
		if (column != null && column.name().trim().length() != 0) { return column
				.name(); }
		PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
		if (primaryKey != null && primaryKey.name().trim().length() != 0)
			return primaryKey.name();

		return field.getName();
	}

	/**
	 * ���Ĭ��ֵ
	 * 
	 * @param field
	 * @return
	 */
	public static String getPropertyDefaultValue(Field field)
	{
		Column column = field.getAnnotation(Column.class);
		if (column != null && column.defaultValue().trim().length() != 0) { return column
				.defaultValue(); }
		return null;
	}
}
