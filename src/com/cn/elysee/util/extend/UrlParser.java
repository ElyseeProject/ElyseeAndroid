package com.cn.elysee.util.extend;

import java.net.URL;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class UrlParser
{
	/**
	 * url转换工具,确保url为绝对路径
	 * 
	 * @param baseUrl
	 *            url的根域名
	 * @param url
	 *            需要转换的url(绝对路径，或相对路径)
	 * @return 返回绝对路径url
	 */
	public static String urlParse(String baseUrl, String url)
	{
		String returnUrl = "";
		try
		{
			URL absoluteUrl = new URL(baseUrl);
			URL parseUrl = new URL(absoluteUrl, url);
			returnUrl = parseUrl.toString();
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		return returnUrl;

	}
}
