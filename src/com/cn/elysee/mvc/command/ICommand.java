package com.cn.elysee.mvc.command;

import com.cn.elysee.mvc.common.IResponseListener;
import com.cn.elysee.mvc.common.Request;
import com.cn.elysee.mvc.common.Response;

/**
 * @author hzx 
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public interface ICommand
{
	Request getRequest();

	void setRequest(Request request);

	Response getResponse();

	void setResponse(Response response);

	void execute();

	IResponseListener getResponseListener();

	void setResponseListener(IResponseListener listener);

	void setTerminated(boolean terminated);

	boolean isTerminated();
}
