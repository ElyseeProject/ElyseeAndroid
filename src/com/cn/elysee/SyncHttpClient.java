package com.cn.elysee;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.cn.elysee.util.http.AsyncHttpClient;
import com.cn.elysee.util.http.AsyncHttpRequest;
import com.cn.elysee.util.http.AsyncHttpResponseHandler;
import com.cn.elysee.util.http.RequestParams;

import android.content.Context;
import android.os.Message;

/**
 * @author hzx 
 * @version V1.0
 */
public class SyncHttpClient extends AsyncHttpClient
{
	private int responseCode;
	protected String result;
	protected AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
	{

		@Override
		protected void sendResponseMessage(org.apache.http.HttpResponse response)
		{
			responseCode = response.getStatusLine().getStatusCode();
			super.sendResponseMessage(response);
		}

		@Override
		protected void sendMessage(Message msg)
		{
			handleMessage(msg);
		}

		@Override
		public void onSuccess(String content)
		{
			result = content;
		}

		@Override
		public void onFailure(Throwable error, String content)
		{
			result = onRequestFailed(error, content);
		}
	};

	public int getResponseCode()
	{
		return responseCode;
	}

	@Override
	protected void sendRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, AsyncHttpResponseHandler responseHandler,
			Context context)
	{
		if (contentType != null)
		{
			uriRequest.addHeader("Content-Type", contentType);
		}
		new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler)
				.run();
	}

	public String onRequestFailed(Throwable error, String content)
	{
		return "";
	}

	public void delete(String url, RequestParams queryParams,
			AsyncHttpResponseHandler responseHandler)
	{
		delete(url, responseHandler);
	}

	public String get(String url, RequestParams params)
	{
		this.get(url, params, responseHandler);
		return result;
	}

	public String get(String url)
	{
		this.get(url, null, responseHandler);
		return result;
	}

	public String put(String url, RequestParams params)
	{
		this.put(url, params, responseHandler);
		return result;
	}

	public String put(String url)
	{
		this.put(url, null, responseHandler);
		return result;
	}
	
	

	public String post(String url, RequestParams params)
	{
		this.post(url, params, responseHandler);
		return result;
	}

	public String post(String url)
	{
		this.post(url, null, responseHandler);
		return result;
	}

	public String delete(String url, RequestParams params)
	{
		this.delete(url, params, responseHandler);
		return result;
	}

	public String delete(String url)
	{
		this.delete(url, null, responseHandler);
		return result;
	}
}
