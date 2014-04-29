package com.cn.elysee.mvc.common;

import com.cn.elysee.common.BaseEntity;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class Request extends BaseEntity
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = -5391677500579149815L;
	private Object tag;
	private Object data;
	private String activityKey;
	private int activityKeyResID;

	public Request()
	{
	}

	public Request(Object tag, Object data)
	{
		this.tag = tag;
		this.data = data;
	}

	public Object getTag()
	{
		return tag;
	}

	public void setTag(Object tag)
	{
		this.tag = tag;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public int getActivityKeyResID()
	{
		return activityKeyResID;
	}

	public void setActivityKeyResID(int activityKeyResID)
	{
		this.activityKeyResID = activityKeyResID;
	}

	public String getActivityKey()
	{
		return activityKey;
	}

	public void setActivityKey(String activityKey)
	{
		this.activityKey = activityKey;
	}

}
