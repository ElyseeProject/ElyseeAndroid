package com.cn.elysee.util;

import java.lang.reflect.Field;
import java.util.Comparator;

import com.cn.elysee.annotation.CompareAnnotation;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class BaseComparator<T> implements Comparator<T>
{
	/** �������� */
	public static final int ASC_SORT_TYPE = 1;
	/** �������� */
	public static final int DES_SORT_TYPE = 2;
	/** �����ֶ� */
	private int sortType = 1;
	/** ѡ������������Ա�ʶ */
	private int sortFlag = 0;

	/**
	 * Ĭ�Ϲ��캯��
	 */
	public BaseComparator()
	{

	}

	/**
	 * ���������Ĺ��캯��
	 * 
	 * @param sortType
	 *            �������
	 */
	public BaseComparator(int sortType)
	{
		this.sortType = sortType;
	}

	/**
	 * �����������������ʶ���캯��
	 * 
	 * @param sortType
	 *            �������
	 * @param sortFlag
	 *            �����ʶ
	 */
	public BaseComparator(int sortType, int sortFlag)
	{
		this.sortType = sortType;
		this.sortFlag = sortFlag;
	}

	@Override
	public int compare(T object1, T object2)
	{
		int compareValue1 = 0;
		int compareValue2 = 0;
		try
		{
			compareValue1 = getCompareValue(object1);
			compareValue2 = getCompareValue(object2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			compareValue1 = 0;
			compareValue2 = 0;
		}
		if (sortType == 2)
		{
			return compareValue2 - compareValue1;
		}
		else
		{
			return compareValue1 - compareValue2;
		}

	}

	public int getCompareValue(T object) throws IllegalArgumentException,
			IllegalAccessException
	{
		int compareValue = 0;
		Field[] fields = object.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(CompareAnnotation.class))
				{
					CompareAnnotation taCompareInject = field
							.getAnnotation(CompareAnnotation.class);
					int flag = taCompareInject.sortFlag();
					field.setAccessible(true);
					if (field.getType().equals(Integer.TYPE))
					{
						compareValue = field.getInt(object);
					}
					else if (field.getType().equals(Boolean.TYPE))
					{
						Boolean b = field.getBoolean(object);
						if (b)
						{
							compareValue = 1;
						}
						else
						{
							compareValue = 0;
						}
					}
					else if (field.getType().equals(String.class))
					{
						String fieldValueString = (String) field.get(object);
						compareValue = Integer.parseInt(fieldValueString);
					}
					if (flag == this.sortFlag) { return compareValue; }
				}
			}
		}
		return compareValue;
	}

	public int getSortType()
	{
		return sortType;
	}

	public void setSortType(int sortType)
	{
		this.sortType = sortType;
	}

}
