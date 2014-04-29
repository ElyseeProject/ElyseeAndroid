package com.cn.elysee.util.cache;

import java.lang.ref.WeakReference;

import com.cn.elysee.util.cache.FileCacheWork.BufferWorkerTask;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
@SuppressWarnings("rawtypes")
public class AsyncEntity
{
	//通过弱引用来建立对象
	private final WeakReference<BufferWorkerTask> bufferWorkerTaskReference;

	public AsyncEntity(BufferWorkerTask inpputWorkerTask)
	{
		bufferWorkerTaskReference = new WeakReference<BufferWorkerTask>(
				inpputWorkerTask);
	}

	public BufferWorkerTask getBufferWorkerTask()
	{
		return bufferWorkerTaskReference.get();
	}
}
