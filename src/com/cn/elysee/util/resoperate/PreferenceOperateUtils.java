package com.cn.elysee.util.resoperate;

import java.lang.reflect.Field;

import com.cn.elysee.common.ReflectUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class PreferenceOperateUtils
{
	private Context mContext;
	private SharedPreferences mSharedPreferences = null;
	private Editor edit = null;

	/**
	 * 创建 DefaultSharedPreferences
	 * 
	 * @param context
	 */
	public PreferenceOperateUtils(Context context)
	{
		this(context, PreferenceManager.getDefaultSharedPreferences(context));
	}

	/**
	 * 通过文件名 创建 SharedPreferences
	 * 
	 * @param context
	 * @param filename
	 *            文件名
	 */
	public PreferenceOperateUtils(Context context, String filename)
	{
		this(context, context.getSharedPreferences(filename,
				Context.MODE_WORLD_WRITEABLE));
	}

	/**
	 * 通过SharedPreferences创建 SharedPreferences
	 * 
	 * @param context
	 * @param sharedPreferences
	 */
	public PreferenceOperateUtils(Context context,
			SharedPreferences sharedPreferences)
	{
		this.mContext = context;
		this.mSharedPreferences = sharedPreferences;
		edit = mSharedPreferences.edit();
	}

	public void setString(String key, String value)
	{
		edit.putString(key, value);
		edit.commit();
	}

	public void setInt(String key, int value)
	{
		edit.putInt(key, value);
		edit.commit();
	}

	public void setBoolean(String key, Boolean value)
	{
		edit.putBoolean(key, value);
		edit.commit();
	}

	public void setByte(String key, byte[] value)
	{
		setString(key, String.valueOf(value));
	}

	public void setShort(String key, short value)
	{
		setString(key, String.valueOf(value));
	}

	public void setLong(String key, long value)
	{
		edit.putLong(key, value);
		edit.commit();
	}

	public void setFloat(String key, float value)
	{
		edit.putFloat(key, value);
		edit.commit();
	}

	public void setDouble(String key, double value)
	{
		setString(key, String.valueOf(value));
	}

	public void setString(int resID, String value)
	{
		setString(this.mContext.getString(resID), value);

	}

	public void setInt(int resID, int value)
	{
		setInt(this.mContext.getString(resID), value);
	}

	public void setBoolean(int resID, Boolean value)
	{
		setBoolean(this.mContext.getString(resID), value);
	}

	public void setByte(int resID, byte[] value)
	{
		setByte(this.mContext.getString(resID), value);
	}

	public void setShort(int resID, short value)
	{
		setShort(this.mContext.getString(resID), value);
	}

	public void setLong(int resID, long value)
	{
		setLong(this.mContext.getString(resID), value);
	}

	public void setFloat(int resID, float value)
	{
		setFloat(this.mContext.getString(resID), value);
	}

	public void setDouble(int resID, double value)
	{
		setDouble(this.mContext.getString(resID), value);
	}

	public String getString(String key, String defaultValue)
	{
		return mSharedPreferences.getString(key, defaultValue);
	}

	public int getInt(String key, int defaultValue)
	{
		return mSharedPreferences.getInt(key, defaultValue);
	}

	public boolean getBoolean(String key, Boolean defaultValue)
	{
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public byte[] getByte(String key, byte[] defaultValue)
	{
		try
		{
			return getString(key, "").getBytes();
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	public short getShort(String key, Short defaultValue)
	{
		try
		{
			return Short.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	public long getLong(String key, Long defaultValue)
	{
		return mSharedPreferences.getLong(key, defaultValue);
	}

	public float getFloat(String key, Float defaultValue)
	{
		return mSharedPreferences.getFloat(key, defaultValue);
	}

	public double getDouble(String key, Double defaultValue)
	{
		try
		{
			return Double.valueOf(getString(key, ""));
		}
		catch (Exception e)
		{
		}
		return defaultValue;
	}

	public String getString(int resID, String defaultValue)
	{
		return getString(this.mContext.getString(resID), defaultValue);
	}

	public int getInt(int resID, int defaultValue)
	{
		return getInt(this.mContext.getString(resID), defaultValue);
	}

	public boolean getBoolean(int resID, Boolean defaultValue)
	{
		return getBoolean(this.mContext.getString(resID), defaultValue);
	}

	public byte[] getByte(int resID, byte[] defaultValue)
	{
		return getByte(this.mContext.getString(resID), defaultValue);
	}

	public short getShort(int resID, Short defaultValue)
	{
		return getShort(this.mContext.getString(resID), defaultValue);
	}

	public long getLong(int resID, Long defaultValue)
	{
		return getLong(this.mContext.getString(resID), defaultValue);
	}

	public float getFloat(int resID, Float defaultValue)
	{
		return getFloat(this.mContext.getString(resID), defaultValue);
	}

	public double getDouble(int resID, Double defaultValue)
	{
		return getDouble(this.mContext.getString(resID), defaultValue);
	}

	public void setConfig(Object entity)
	{
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields)
		{

			if (!ReflectUtils.isTransient(field))
			{
				if (ReflectUtils.isBaseDateType(field))
				{
					String columnName = ReflectUtils.getFieldName(field);
					field.setAccessible(true);
					setValue(field, columnName, entity);
				}
			}
		}
	}

	private void setValue(Field field, String columnName, Object entity)
	{
		try
		{
			Class<?> clazz = field.getType();
			if (clazz.equals(String.class))
			{
				setString(columnName, (String) field.get(entity));
			}
			else if (clazz.equals(Integer.class) || clazz.equals(int.class))
			{
				setInt(columnName, (Integer) field.get(entity));
			}
			else if (clazz.equals(Float.class) || clazz.equals(float.class))
			{
				setFloat(columnName, (Float) field.get(entity));
			}
			else if (clazz.equals(Double.class) || clazz.equals(double.class))
			{
				setDouble(columnName, (Double) field.get(entity));
			}
			else if (clazz.equals(Short.class) || clazz.equals(Short.class))
			{
				setShort(columnName, (Short) field.get(entity));
			}
			else if (clazz.equals(Long.class) || clazz.equals(long.class))
			{
				setLong(columnName, (Long) field.get(entity));
			}
			else if (clazz.equals(Boolean.class))
			{
				setBoolean(columnName, (Boolean) field.get(entity));
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

	public <T> T getConfig(Class<T> clazz)
	{
		Field[] fields = clazz.getDeclaredFields();
		T entity = null;
		try
		{
			entity = clazz.newInstance();
			for (Field field : fields)
			{
				field.setAccessible(true);
				if (!ReflectUtils.isTransient(field))
				{
					if(ReflectUtils.isBaseDateType(field))
					{
						String columnName = ReflectUtils.getFieldName(field);
						field.setAccessible(true);
						getValue(field, columnName, entity);
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
		return entity;
	}

	private <T> void getValue(Field field, String columnName, T entity)
	{
		try
		{
			Class<?> clazz = field.getType();
			if (clazz.equals(String.class))
			{
				field.set(entity, getString(columnName, ""));
			}
			else if (clazz.equals(Integer.class) || clazz.equals(int.class))
			{
				field.set(entity, getInt(columnName, 0));
			}
			else if (clazz.equals(Float.class) || clazz.equals(float.class))
			{
				field.set(entity, getFloat(columnName, 0f));
			}
			else if (clazz.equals(Double.class) || clazz.equals(double.class))
			{
				field.set(entity, getDouble(columnName, 0.0));
			}
			else if (clazz.equals(Short.class) || clazz.equals(Short.class))
			{
				field.set(entity, getShort(columnName, (short) 0));
			}
			else if (clazz.equals(Long.class) || clazz.equals(long.class))
			{
				field.set(entity, getLong(columnName, 0l));
			}
			else if (clazz.equals(Byte.class) || clazz.equals(byte.class))
			{
				field.set(entity, getByte(columnName, new byte[8]));
			}
			else if (clazz.equals(Boolean.class))
			{
				field.set(entity, getBoolean(columnName, false));
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

	public void remove(String key)
	{
		edit.remove(key);
		edit.commit();
	}

	public void remove(String... keys)
	{
		for (String key : keys)
			remove(key);
	}

	public void clear()
	{
		edit.clear();
		edit.commit();
	}
}
