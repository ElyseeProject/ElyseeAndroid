
package com.cn.elysee.util.cache;

import com.cn.elysee.common.AsyncTask;
import com.cn.elysee.common.BaseEntity;


/**
 * @author hzx
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class FileResponseEntity extends BaseEntity
{
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 155171755558141369L;
	private AsyncTask<?, ?, ?> task;
	private Object object;

	public AsyncTask<?, ?, ?> getTask()
	{
		return task;
	}

	public void setTask(AsyncTask<?, ?, ?> task)
	{
		this.task = task;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}

}
