package com.cn.elysee.db.entity;

/**
 * @author hzx 
 * 2014Äê4ÔÂ18ÈÕ
 * @version V1.0
 */
public class PKProperyEntity extends PropertyEntity
{
	public PKProperyEntity()
	{

	}

	public PKProperyEntity(String name, Class<?> type, Object defaultValue,
			boolean primaryKey, boolean isAllowNull, boolean autoIncrement,
			String columnName)
	{
		super(name, type, defaultValue, primaryKey, isAllowNull, autoIncrement,
				columnName);
	}
}
