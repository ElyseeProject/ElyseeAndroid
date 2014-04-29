package com.cn.elysee.util.download;

import java.util.ArrayList;
import java.util.List;

import com.cn.elysee.BaseApplication;
import com.cn.elysee.common.StringUtils;

/**
 * @author hzx 
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class DownLoadConfigUtil
{
	public static final String PREFERENCE_NAME = "com.yyxu.download";
	public static final int URL_COUNT = 3;
	public static final String KEY_URL = "url";

	public static void storeURL(int index, String url)
	{
		BaseApplication.getApplication().getCurrentConfig()
				.setString(KEY_URL + index, url);
	}

	public static void clearURL(int index)
	{
		BaseApplication.getApplication().getCurrentConfig()
				.remove(KEY_URL + index);
	}

	public static String getURL(int index)
	{
		return BaseApplication.getApplication().getCurrentConfig()
				.getString(KEY_URL + index, "");
	}

	public static List<String> getURLArray()
	{
		List<String> urlList = new ArrayList<String>();
		for (int i = 0; i < URL_COUNT; i++)
		{
			if (!StringUtils.isEmpty(getURL(i)))
			{
				urlList.add(getString(KEY_URL + i));
			}
		}
		return urlList;
	}

	private static String getString(String key)
	{
		return BaseApplication.getApplication().getCurrentConfig()
				.getString(key, "");
	}

}
