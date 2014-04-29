package com.cn.elysee.util.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;

import javax.net.ssl.SSLException;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import android.os.SystemClock;

/**
 * @author hzx 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class RetryHandler implements HttpRequestRetryHandler
{

	private static final int RETRY_SLEEP_TIME_MILLIS = 1500;
	private static HashSet<Class<?>> exceptionWhitelist = new HashSet<Class<?>>();
	private static HashSet<Class<?>> exceptionBlacklist = new HashSet<Class<?>>();

	static
	{
		exceptionWhitelist.add(NoHttpResponseException.class);
		exceptionWhitelist.add(UnknownHostException.class);
		exceptionWhitelist.add(SocketException.class);

		exceptionBlacklist.add(InterruptedIOException.class);
		exceptionBlacklist.add(SSLException.class);
	}

	private final int maxRetries;

	public RetryHandler(int maxRetries)
	{
		this.maxRetries = maxRetries;
	}

	@Override
	public boolean retryRequest(IOException exception, int executionCount,
			HttpContext context)
	{
		boolean retry = true;

		Boolean b = (Boolean) context
				.getAttribute(ExecutionContext.HTTP_REQ_SENT);
		boolean sent = (b != null && b.booleanValue());

		if (executionCount > maxRetries)
		{
			retry = false;
		}
		else if (isInList(exceptionBlacklist, exception))
		{
			retry = false;
		}
		else if (isInList(exceptionWhitelist, exception))
		{
			retry = true;
		}
		else if (!sent)
		{
			retry = true;
		}

		if (retry)
		{
			HttpUriRequest currentReq = (HttpUriRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			String requestType = currentReq.getMethod();
			retry = !requestType.equals("POST");
		}

		if (retry)
		{
			SystemClock.sleep(RETRY_SLEEP_TIME_MILLIS);
		}
		else
		{
			exception.printStackTrace();
		}

		return retry;
	}

	protected boolean isInList(HashSet<Class<?>> list, Throwable error)
	{
		Iterator<Class<?>> itr = list.iterator();
		while (itr.hasNext())
		{
			if (itr.next().isInstance(error)) { return true; }
		}
		return false;
	}

}
