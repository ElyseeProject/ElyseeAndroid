
package com.cn.elysee.mvc.command;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.cn.elysee.exception.NoSuchCommandException;
import com.cn.elysee.mvc.common.IResponseListener;
import com.cn.elysee.mvc.common.Request;
import com.cn.elysee.util.Logger;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public class CommandExecutor
{
	private final HashMap<String, Class<? extends ICommand>> commands = new HashMap<String, Class<? extends ICommand>>();

	private static final CommandExecutor instance = new CommandExecutor();
	private boolean initialized = false;

	public CommandExecutor()
	{
		ensureInitialized();
	}

	public static CommandExecutor getInstance()
	{
		return instance;
	}

	public void ensureInitialized()
	{
		if (!initialized)
		{
			initialized = true;
			Logger.i(CommandExecutor.this, "CommandExecutor��ʼ��");
			CommandQueueManager.getInstance().initialize();
			Logger.i(CommandExecutor.this, "CommandExecutor��ʼ��");
		}
	}

	/**
	 * ����������ֹ����Ϊ����
	 */
	public void terminateAll()
	{

	}

	/**
	 * ��������
	 * 
	 * @param commandKey
	 *            ����ID
	 * @param request
	 *            �ύ�Ĳ���
	 * @param listener
	 *            ��Ӧ������
	 * @throws TANoSuchCommandException
	 */
	public void enqueueCommand(String commandKey, Request request,
			IResponseListener listener) throws NoSuchCommandException
	{
		final ICommand cmd = getCommand(commandKey);
		enqueueCommand(cmd, request, listener);
	}

	public void enqueueCommand(ICommand command, Request request,
			IResponseListener listener) throws NoSuchCommandException
	{
		if (command != null)
		{
			command.setRequest(request);
			command.setResponseListener(listener);
			CommandQueueManager.getInstance().enqueue(command);
		}
	}

	public void enqueueCommand(ICommand command, Request request)
			throws NoSuchCommandException
	{
		enqueueCommand(command, null, null);
	}

	public void enqueueCommand(ICommand command)
			throws NoSuchCommandException
	{
		enqueueCommand(command, null);
	}

	private ICommand getCommand(String commandKey)
			throws NoSuchCommandException
	{
		ICommand rv = null;

		if (commands.containsKey(commandKey))
		{
			Class<? extends ICommand> cmd = commands.get(commandKey);
			if (cmd != null)
			{
				int modifiers = cmd.getModifiers();
				if ((modifiers & Modifier.ABSTRACT) == 0
						&& (modifiers & Modifier.INTERFACE) == 0)
				{
					try
					{
						rv = cmd.newInstance();
					} catch (Exception e)
					{
						throw new NoSuchCommandException("û����" + commandKey
								+ "����");
					}
				} else
				{
					throw new NoSuchCommandException("û����" + commandKey
							+ "����");
				}
			}
		}

		return rv;
	}

	public void registerCommand(String commandKey,
			Class<? extends ICommand> command)
	{
		if (command != null)
		{
			commands.put(commandKey, command);
		}
	}

	public void unregisterCommand(String commandKey)
	{
		commands.remove(commandKey);
	}
}
