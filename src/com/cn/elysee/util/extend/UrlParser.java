package com.cn.elysee.util.extend;

import java.net.URL;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class UrlParser
{
	/**
	 * urlת������,ȷ��urlΪ����·��
	 * 
	 * @param baseUrl
	 *            url�ĸ�����
	 * @param url
	 *            ��Ҫת����url(����·���������·��)
	 * @return ���ؾ���·��url
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
