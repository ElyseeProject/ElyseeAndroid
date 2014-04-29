package com.cn.elysee.db.entity;

import java.util.ArrayList;
import java.util.List;

import com.cn.elysee.common.BaseEntity;

/**
 * @author hzx 2014Äê4ÔÂ18ÈÕ
 * @version V1.0
 */
public class TableInfoEntity extends BaseEntity
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = -1869016484236866996L;
	private String tableName = "";
	private String className = "";
	private PKProperyEntity pkProperyEntity = null;

	ArrayList<PropertyEntity> propertieArrayList = new ArrayList<PropertyEntity>();

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public ArrayList<PropertyEntity> getPropertieArrayList()
	{
		return propertieArrayList;
	}

	public void setPropertieArrayList(List<PropertyEntity> propertyList)
	{
		this.propertieArrayList = (ArrayList<PropertyEntity>) propertyList;
	}

	public PKProperyEntity getPkProperyEntity()
	{
		return pkProperyEntity;
	}

	public void setPkProperyEntity(PKProperyEntity pkProperyEntity)
	{
		this.pkProperyEntity = pkProperyEntity;
	}
}
