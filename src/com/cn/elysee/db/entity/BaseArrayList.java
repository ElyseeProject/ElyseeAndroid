package com.cn.elysee.db.entity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.cn.elysee.common.StringUtils;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class BaseArrayList extends ArrayList<NameValuePair>
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(NameValuePair nameValuePair)
	{
		if (!StringUtils.isEmpty(nameValuePair.getValue()))
		{
			return super.add(nameValuePair);
		}
		else
		{
			return false;
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, String value)
	{
		return add(new BasicNameValuePair(key, value));
	}
}
