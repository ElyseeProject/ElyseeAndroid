package com.cn.elysee.util.http;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 * @author hzx 
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class AsyncHttpRequest implements Runnable
{
	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest request;
	private final AsyncHttpResponseHandler responseHandler;
	private boolean isBinaryRequest;
	private int executionCount;

	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, AsyncHttpResponseHandler responseHandler)
	{
		this.client = client;
		this.context = context;
		this.request = request;
		this.responseHandler = responseHandler;
		if (responseHandler instanceof BinaryHttpResponseHandler)
		{
			this.isBinaryRequest = true;
		}
		else if (responseHandler instanceof FileHttpResponseHandler)
		{
			FileHttpResponseHandler fileHttpResponseHandler = (FileHttpResponseHandler) responseHandler;
			File tempFile = fileHttpResponseHandler.getTempFile();

			if (tempFile.exists())
			{

				long previousFileSize = tempFile.length();
				fileHttpResponseHandler.setPreviousFileSize(previousFileSize);
				this.request.setHeader("RANGE", "bytes=" + previousFileSize
						+ "-");
			}

		}
	}

	@Override
	public void run()
	{
		try
		{
			if (responseHandler != null)
			{
				responseHandler.sendStartMessage();
			}

			makeRequestWithRetries();
			if (responseHandler != null)
			{
				responseHandler.sendFinishMessage();
			}

		}
		catch (IOException e)
		{
			if (responseHandler != null)
			{
				responseHandler.sendFinishMessage();
				if (this.isBinaryRequest)
				{
					responseHandler.sendFailureMessage(e, (byte[]) null);
				}
				else
				{
					responseHandler.sendFailureMessage(e, (String) null);
				}
			}
		}
	}

	private void makeRequest() throws IOException
	{
		if (!Thread.currentThread().isInterrupted())
		{
			try
			{
				HttpResponse response = client.execute(request, context);
				if (!Thread.currentThread().isInterrupted())
				{
					if (responseHandler != null)
					{
						responseHandler.sendResponseMessage(response);
					}
				}
				else
				{
					
				}
			}
			catch (IOException e)
			{
				if (!Thread.currentThread().isInterrupted()) { throw e; }
			}
		}
	}

	private void makeRequestWithRetries() throws ConnectException
	{
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client
				.getHttpRequestRetryHandler();
		while (retry)
		{
			try
			{
				makeRequest();
				return;
			}
			catch (UnknownHostException e)
			{
				if (responseHandler != null)
				{
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			}
			catch (SocketException e)
			{
				if (responseHandler != null)
				{
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			}
			catch (SocketTimeoutException e)
			{
				if (responseHandler != null)
				{
					responseHandler.sendFailureMessage(e, "socket time out");
				}
				return;
			}
			catch (IOException e)
			{
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
			}
			catch (NullPointerException e)
			{
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
			}
		}

		ConnectException ex = new ConnectException();
		ex.initCause(cause);
		throw ex;
	}

}
