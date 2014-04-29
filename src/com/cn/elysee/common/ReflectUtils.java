package com.cn.elysee.common;

import java.lang.reflect.Field;
import java.util.Date;

import com.cn.elysee.annotation.Transparent;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class ReflectUtils
{
	/**
	 * ���ʵ�������Ƿ��Ѿ�����עΪ ����ʶ�� 
	 * 2014��4��21��
	 * @param field
	 * @return
	 */
	public static boolean isTransient(Field field)
	{
		return field.getAnnotation(Transparent.class) != null;
	}

	/**
	 * �Ƿ�Ϊ�������������� 
	 * 2014��4��21��
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
	 * ��������� 
	 * 2014��4��21��
	 * @param field
	 * @return
	 */
	public static String getFieldName(Field field)
	{
		com.cn.elysee.annotation.Field column = field
				.getAnnotation(com.cn.elysee.annotation.Field.class);
		if (column != null && column.name().trim().length() != 0) { return column
				.name(); }
		return field.getName();
	}
}
