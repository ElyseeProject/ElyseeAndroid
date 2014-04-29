package com.cn.elysee.util.cache;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class CallBackHandler<T>
{
	/**
	 * 缓存运行开始
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 */
	public void onStart(T t, Object data)
	{
	}

	/**
	 * 缓存运行开始
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 * @param inputStream
	 *            标识对应的响应数据
	 */
	public void onSuccess(T t, Object data, byte[] buffer)
	{
	}

	/**
	 * 缓存运行失败
	 * 
	 * @param t
	 *            响应的对象
	 * @param data
	 *            数据唯一标识
	 */
	public void onFailure(T t, Object data)
	{

	}
}
