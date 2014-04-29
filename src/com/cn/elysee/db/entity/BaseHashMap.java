package com.cn.elysee.db.entity;

import java.util.Date;
import java.util.HashMap;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class BaseHashMap<T extends Object> extends HashMap<String, T>
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public T put(String key, T value)
	{
		if (hasValue(value))
		{
			return super.put(key, value);
		}
		else
		{
			return null;
		}
	};

	public boolean hasValue(Object value)
	{
		if (value != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public T get(Object key)
	{
		return super.get(key);
	}

	public String getString(String key)
	{
		return String.valueOf(get(key));
	}

	public int getInt(String key)
	{
		return Integer.valueOf(getString(key));
	}

	public boolean getBoolean(String key)
	{
		return Boolean.valueOf(getString(key));
	}

	public double getDouble(String key)
	{
		return Double.valueOf(getString(key));
	}

	public float getFloat(String key)
	{
		return Float.valueOf(getString(key));
	}

	public long getLong(String key)
	{
		return Long.valueOf(getString(key));
	}

	/**
	 * 获取时间
	 * 
	 * @param key
	 * @return
	 */
	public Date getDate(String key)
	{
		return new Date(getString(key));
	}

	public char getChar(String key)
	{
		return getString(key).trim().toCharArray()[0];
	}

	public byte[] getBlob(String key)
	{
		return getString(key).getBytes();
	}

	public short getShort(String key)
	{
		return Short.valueOf(getString(key));
	}

}
