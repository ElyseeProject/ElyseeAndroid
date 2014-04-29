package com.cn.elysee.mvc.command;

import com.cn.elysee.mvc.common.IResponseListener;
import com.cn.elysee.mvc.common.Request;
import com.cn.elysee.mvc.common.Response;

/**
 * @author hzx 
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class IdentityCommand extends Command
{

	@Override
	protected void executeCommand()
	{
		Request request = getRequest();
		Response response = new Response();
		response.setTag(request.getTag());
		response.setData(request.getData());
		response.setActivityKey(request.getActivityKey());
		response.setActivityKeyResID(request.getActivityKeyResID());
		setResponse(response);
		notifyListener(true);
	}

	protected void notifyListener(boolean success)
	{
		IResponseListener responseListener = getResponseListener();
		if (responseListener != null)
		{
			sendMessage(command_success);
		}
	}

}
