package com.cn.elysee.util.cache;

import java.lang.ref.WeakReference;

import com.cn.elysee.util.cache.FileCacheWork.BufferWorkerTask;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
@SuppressWarnings("rawtypes")
public class AsyncEntity
{
	//ͨ������������������
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
