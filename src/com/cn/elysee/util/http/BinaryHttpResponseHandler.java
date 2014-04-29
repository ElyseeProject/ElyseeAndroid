package com.cn.elysee.util.http;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import android.os.Message;

/**
 * @author hzx 2014年4月21日
 * @version V1.0
 */
public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler
{
	private static String[] mAllowedContentTypes = new String[]
	{ "image/jpeg", "image/png" };

	/**
	 * 
	 */
	public BinaryHttpResponseHandler()
	{
		super();
	}

	/**
	 * 
	 * @param allowedContentTypes
	 */
	public BinaryHttpResponseHandler(String[] allowedContentTypes)
	{
		this();
		mAllowedContentTypes = allowedContentTypes;
	}

	/**
	 * 
	 * 2014年4月21日
	 * @param binaryData
	 */
	public void onSuccess(byte[] binaryData)
	{
	}

	/**
	 * 
	 * 2014年4月21日
	 * @param statusCode
	 * @param binaryData
	 */
	public void onSuccess(int statusCode, byte[] binaryData)
	{
		onSuccess(binaryData);
	}

	/**
	 * 
	 * 2014年4月21日
	 * @param error
	 * @param binaryData
	 */
	@Deprecated
	public void onFailure(Throwable error, byte[] binaryData)
	{
		onFailure(error);
	}

	/**
	 * 
	 * 2014年4月21日
	 * @param statusCode
	 * @param responseBody
	 */
	protected void sendSuccessMessage(int statusCode, byte[] responseBody)
	{
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]
		{ statusCode, responseBody }));
	}

	@Override
	protected void sendFailureMessage(Throwable e, byte[] responseBody)
	{
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]
		{ e, responseBody }));
	}

	/**
	 * 
	 * 2014年4月21日
	 * @param statusCode
	 * @param responseBody
	 */
	protected void handleSuccessMessage(int statusCode, byte[] responseBody)
	{
		onSuccess(statusCode, responseBody);
	}

	protected void handleFailureMessage(Throwable e, byte[] responseBody)
	{
		onFailure(e, responseBody);
	}

	/**
	 * 
	 */
	@Override
	protected void handleMessage(Message msg)
	{
		Object[] response;
		switch (msg.what)
		{
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(byte[]) response[1]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			handleFailureMessage((Throwable) response[0],
					response[1].toString());
			break;
		default:
			super.handleMessage(msg);
			break;
		}
	}

	/**
	 * 
	 */
	@Override
	protected void sendResponseMessage(HttpResponse response)
	{
		StatusLine status = response.getStatusLine();
		Header[] contentTypeHeaders = response.getHeaders("Content-Type");
		byte[] responseBody = null;
		if (contentTypeHeaders.length != 1)
		{
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(),
					"None, or more than one, Content-Type Header found!"),
					responseBody);
			return;
		}
		Header contentTypeHeader = contentTypeHeaders[0];
		boolean foundAllowedContentType = false;
		for (String anAllowedContentType : mAllowedContentTypes)
		{
			if (Pattern.matches(anAllowedContentType,
					contentTypeHeader.getValue()))
			{
				foundAllowedContentType = true;
			}
		}
		if (!foundAllowedContentType)
		{
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), "Content-Type not allowed!"),
					responseBody);
			return;
		}
		try
		{
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null)
			{
				entity = new BufferedHttpEntity(temp);
			}
			responseBody = EntityUtils.toByteArray(entity);
		}
		catch (IOException e)
		{
			sendFailureMessage(e, (byte[]) null);
		}

		if (status.getStatusCode() >= 300)
		{
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), status.getReasonPhrase()),
					responseBody);
		}
		else
		{
			sendSuccessMessage(status.getStatusCode(), responseBody);
		}
	}
}
