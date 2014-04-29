package com.cn.elysee.db.entity;

import com.cn.elysee.common.BaseEntity;

/**
 * @author hzx 
 * 2014Äê4ÔÂ18ÈÕ
 * @version V1.0
 */
public class DBMasterEntity extends BaseEntity
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 3501664356559626906L;
	private String type;
	private String name;
	private String tbl_name;
	private String sql;
	private int rootpage;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTbl_name()
	{
		return tbl_name;
	}

	public void setTbl_name(String tbl_name)
	{
		this.tbl_name = tbl_name;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public int getRootpage()
	{
		return rootpage;
	}

	public void setRootpage(int rootpage)
	{
		this.rootpage = rootpage;
	}

	
}
