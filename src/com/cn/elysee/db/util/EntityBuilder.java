package com.cn.elysee.db.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.database.Cursor;

/**
 * @author hzx 
 * 2014��4��18��
 * @version V1.0
 */
public class EntityBuilder
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
	@SuppressWarnings("unchecked")
	public static <T> List<T> buildQueryList(Class<T> clazz, Cursor cursor)
	{
		List<T> queryList = new ArrayList<T>();
		if (cursor.moveToFirst())
		{
			do
			{
				queryList.add((T) buildQueryOneEntity(clazz, cursor));
			}
			while (cursor.moveToNext());
		}
		return queryList;
	}

	/**
	 * ͨ��Cursor��ȡһ��ʵ��
	 * 
	 * @param clazz
	 *            ʵ������
	 * @param cursor
	 *            ���ݼ���
	 * @return ��Ӧʵ��
	 */
	@SuppressWarnings("unchecked")
	public static <T> T buildQueryOneEntity(Class<?> clazz, Cursor cursor)
	{
		Field[] fields = clazz.getDeclaredFields();
		T entityT = null;
		try
		{
			entityT = (T) clazz.newInstance();
			for (Field field : fields)
			{
				field.setAccessible(true);
				if (!DBUtils.isTransient(field))
				{
					if (DBUtils.isBaseDateType(field))
					{

						String columnName = DBUtils.getColumnByField(field);
						field.setAccessible(true);
						setValue(field, columnName, entityT, cursor);

					}
				}

			}
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return entityT;
	}

	/**
	 * ����ֵ���ֶ�
	 * 
	 * @param field
	 *            ��Ҫ���õ��ֶ�
	 * @param columnName
	 *            ���ݿ��ֶ���
	 * @param entityT
	 *            ʵ��ģ��
	 * @param cursor
	 *            ���ݼ���
	 */
	@SuppressLint("UseValueOf")
	private static <T> void setValue(Field field, String columnName, T entityT,
			Cursor cursor)
	{
		try
		{
			int columnIndex = cursor
					.getColumnIndexOrThrow((columnName != null && !columnName
							.equals("")) ? columnName : field.getName());
			Class<?> clazz = field.getType();
			if (clazz.equals(String.class))
			{
				field.set(entityT, cursor.getString(columnIndex));
			}
			else if (clazz.equals(Integer.class) || clazz.equals(int.class))
			{
				field.set(entityT, cursor.getInt(columnIndex));
			}
			else if (clazz.equals(Float.class) || clazz.equals(float.class))
			{
				field.set(entityT, cursor.getFloat(columnIndex));
			}
			else if (clazz.equals(Double.class) || clazz.equals(double.class))
			{
				field.set(entityT, cursor.getDouble(columnIndex));
			}
			else if (clazz.equals(Short.class) || clazz.equals(Short.class))
			{
				field.set(entityT, cursor.getShort(columnIndex));
			}
			else if (clazz.equals(Long.class) || clazz.equals(long.class))
			{
				field.set(entityT, cursor.getLong(columnIndex));
			}
			else if (clazz.equals(Byte.class) || clazz.equals(byte.class))
			{
				field.set(entityT, cursor.getBlob(columnIndex));
			}
			else if (clazz.equals(Boolean.class))
			{
				Boolean testBoolean = new Boolean(cursor.getString(columnIndex));
				field.set(entityT, testBoolean);
			}
			else if (clazz.equals(Date.class))
			{
				Date date = new Date(cursor.getString(columnIndex));
				field.set(entityT, date);
			}
			else if (clazz.equals(Character.class) || clazz.equals(char.class))
			{
				Character c1 = cursor.getString(columnIndex).trim()
						.toCharArray()[0];
				field.set(entityT, c1);
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}
}
