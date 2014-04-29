package com.cn.elysee.db.entity;

/**
 * @author hzx 
 * 2014年4月18日
 * @Description TADBProperty 数据库的字段
 * @version V1.0
 */
public class PropertyEntity
{
	protected String name;
	protected String columnName;
	protected Class<?> type;
	protected Object defaultValue;
	protected boolean isAllowNull = true;
	protected int index; // 暂时不写
	protected boolean primaryKey = false;
	protected boolean autoIncrement = false;

	public PropertyEntity()
	{

	}

	public PropertyEntity(String name, Class<?> type, Object defaultValue,
			boolean primaryKey, boolean isAllowNull, boolean autoIncrement,
			String columnName)
	{
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.primaryKey = primaryKey;
		this.isAllowNull = isAllowNull;
		this.autoIncrement = autoIncrement;
		this.columnName = columnName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Class<?> getType()
	{
		return type;
	}

	public void setType(Class<?> type)
	{
		this.type = type;
	}

	public Object getDefaultValue()
	{
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public boolean isPrimaryKey()
	{
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey)
	{
		this.primaryKey = primaryKey;
	}

	public boolean isAllowNull()
	{
		return isAllowNull;
	}

	public void setAllowNull(boolean isAllowNull)
	{
		this.isAllowNull = isAllowNull;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public boolean isAutoIncrement()
	{
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement)
	{
		this.autoIncrement = autoIncrement;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}
}
