package com.cn.elysee.mvc.command;

import java.util.concurrent.LinkedBlockingQueue;

import com.cn.elysee.util.Logger;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class CommandQueue
{
	private LinkedBlockingQueue<ICommand> theQueue = new LinkedBlockingQueue<ICommand>();

	public CommandQueue()
	{
		Logger.i(CommandQueue.this, "��ʼ��Command����");
	}

	public void enqueue(ICommand cmd)
	{
		Logger.i(CommandQueue.this, "���Command������");
		theQueue.add(cmd);
	}

	public synchronized ICommand getNextCommand()
	{
		Logger.i(CommandQueue.this, "��ȡCommand");
		ICommand cmd = null;
		try
		{
			Logger.i(CommandQueue.this, "CommandQueue::to-take");
			cmd = theQueue.take();
			Logger.i(CommandQueue.this, "CommandQueue::taken");
		}
		catch (InterruptedException e)
		{
			Logger.i(CommandQueue.this, "û�л�ȡ��Command");
			e.printStackTrace();
		}
		Logger.i(CommandQueue.this, "����Command" + cmd);
		return cmd;
	}

	public synchronized void clear()
	{
		Logger.i(CommandQueue.this, "�������Command");
		theQueue.clear();
	}
}
