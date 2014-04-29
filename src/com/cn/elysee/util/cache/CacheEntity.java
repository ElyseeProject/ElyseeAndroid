
package com.cn.elysee.util.cache;

import com.cn.elysee.common.BaseEntity;


/**
 * @author hzx
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class CacheEntity <T> extends BaseEntity
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 1L;
	private T t;
	private AsyncEntity asyncEntity;

	public T getT()
	{
		return t;
	}

	public void setT(T t)
	{
		this.t = t;
	}

	public AsyncEntity getAsyncEntity()
	{
		return asyncEntity;
	}

	public void setAsyncEntity(AsyncEntity asyncEntity)
	{
		this.asyncEntity = asyncEntity;
	}

}
