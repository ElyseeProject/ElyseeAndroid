package com.cn.elysee.mvc.command;

import com.cn.elysee.util.Logger;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class ThreadPool
{
	// �̵߳��������
	private static final int MAX_THREADS_COUNT = 2;
	private CommandThread threads[] = null;
	private boolean started = false;
	private static ThreadPool instance;

	private ThreadPool()
	{

	}

	public static ThreadPool getInstance()
	{
		if (instance == null)
		{
			instance = new ThreadPool();
		}
		return instance;
	}

	public void start()
	{
		if (!started)
		{
			Logger.i(ThreadPool.this, "�̳߳ؿ�ʼ���У�");
			int threadCount = MAX_THREADS_COUNT;

			threads = new CommandThread[threadCount];
			for (int threadId = 0; threadId < threadCount; threadId++)
			{
				threads[threadId] = new CommandThread(threadId);
				threads[threadId].start();
			}
			started = true;
			Logger.i(ThreadPool.this, "�̳߳�������ɣ�");
		}
	}

	public void shutdown()
	{
		Logger.i(ThreadPool.this, "�ر������̣߳�");
		if (started)
		{
			for (CommandThread thread : threads)
			{
				thread.stop();
			}
			threads = null;
			started = false;
		}
		Logger.i(ThreadPool.this, "�ر��������̣߳�");
	}
}
