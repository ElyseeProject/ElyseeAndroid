package com.cn.elysee.mvc.command;

import com.cn.elysee.util.Logger;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public final class CommandQueueManager
{
	private static CommandQueueManager instance;
	private boolean initialized = false;
	private ThreadPool pool;
	private CommandQueue queue;

	private CommandQueueManager()
	{
	}

	public static CommandQueueManager getInstance()
	{
		if (instance == null)
		{
			instance = new CommandQueueManager();
		}
		return instance;
	}

	public void initialize()
	{
		Logger.i(CommandQueueManager.this, "׼����ʼ����");
		if (!initialized)
		{
			Logger.i(CommandQueueManager.this, "���ڳ�ʼ����");
			queue = new CommandQueue();
			pool = ThreadPool.getInstance();
			Logger.i(CommandQueueManager.this, "��ɳ�ʼ����");

			pool.start();
			initialized = true;
		}
		Logger.i(CommandQueueManager.this, "��ʼ����ɣ�");
	}

	/**
	 * �Ӷ����л�ȡCommand
	 * 
	 * @return TAICommand
	 */
	public ICommand getNextCommand()
	{
		Logger.i(CommandQueueManager.this, "��ȡCommand��");
		ICommand cmd = queue.getNextCommand();
		Logger.i(CommandQueueManager.this, "��ȡCommand" + cmd + "��ɣ�");
		return cmd;
	}

	/**
	 * ���Command��������
	 */
	public void enqueue(ICommand cmd)
	{
		Logger.i(CommandQueueManager.this, "���" + cmd + "��ʼ");
		queue.enqueue(cmd);
		Logger.i(CommandQueueManager.this, "���" + cmd + "���");
	}

	/**
	 * �������
	 */
	public void clear()
	{
		queue.clear();
	}

	/**
	 * �رն���
	 */
	public void shutdown()
	{
		if (initialized)
		{
			queue.clear();
			pool.shutdown();
			initialized = false;
		}
	}
}
