package com.cn.elysee.util.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class PersistentCookieStore implements CookieStore
{

	private static final String COOKIE_PREFS = "CookiePrefsFile";
	private static final String COOKIE_NAME_STORE = "names";
	private static final String COOKIE_NAME_PREFIX = "cookie_";

	private final ConcurrentHashMap<String, Cookie> cookies;
	private final SharedPreferences cookiePrefs;

	/**
	 * 
	 * @param context
	 */
	public PersistentCookieStore(Context context)
	{
		cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
		cookies = new ConcurrentHashMap<String, Cookie>();

		String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE,
				null);
		if (storedCookieNames != null)
		{
			String[] cookieNames = TextUtils.split(storedCookieNames, ",");
			for (String name : cookieNames)
			{
				String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX
						+ name, null);
				if (encodedCookie != null)
				{
					Cookie decodedCookie = decodeCookie(encodedCookie);
					if (decodedCookie != null)
					{
						cookies.put(name, decodedCookie);
					}
				}
			}

			clearExpired(new Date());
		}
	}

	@Override
	public void addCookie(Cookie cookie)
	{
		String name = cookie.getName() + cookie.getDomain();

		if (!cookie.isExpired(new Date()))
		{
			cookies.put(name, cookie);
		}
		else
		{
			cookies.remove(name);
		}

		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		prefsWriter.putString(COOKIE_NAME_STORE,
				TextUtils.join(",", cookies.keySet()));
		prefsWriter.putString(COOKIE_NAME_PREFIX + name,
				encodeCookie(new SerializableCookie(cookie)));
		prefsWriter.commit();
	}

	@Override
	public void clear()
	{
		cookies.clear();

		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		for (String name : cookies.keySet())
		{
			prefsWriter.remove(COOKIE_NAME_PREFIX + name);
		}
		prefsWriter.remove(COOKIE_NAME_STORE);
		prefsWriter.commit();
	}

	@Override
	public boolean clearExpired(Date date)
	{
		boolean clearedAny = false;
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

		for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet())
		{
			String name = entry.getKey();
			Cookie cookie = entry.getValue();
			if (cookie.isExpired(date))
			{
				cookies.remove(name);

				prefsWriter.remove(COOKIE_NAME_PREFIX + name);

				clearedAny = true;
			}
		}

		if (clearedAny)
		{
			prefsWriter.putString(COOKIE_NAME_STORE,
					TextUtils.join(",", cookies.keySet()));
		}
		prefsWriter.commit();

		return clearedAny;
	}

	@Override
	public List<Cookie> getCookies()
	{
		return new ArrayList<Cookie>(cookies.values());
	}


	protected String encodeCookie(SerializableCookie cookie)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			ObjectOutputStream outputStream = new ObjectOutputStream(os);
			outputStream.writeObject(cookie);
		}
		catch (Exception e)
		{
			return null;
		}

		return byteArrayToHexString(os.toByteArray());
	}

	protected Cookie decodeCookie(String cookieStr)
	{
		byte[] bytes = hexStringToByteArray(cookieStr);
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		Cookie cookie = null;
		try
		{
			ObjectInputStream ois = new ObjectInputStream(is);
			cookie = ((SerializableCookie) ois.readObject()).getCookie();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return cookie;
	}

	protected String byteArrayToHexString(byte[] b)
	{
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (byte element : b)
		{
			int v = element & 0xff;
			if (v < 16)
			{
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	protected byte[] hexStringToByteArray(String s)
	{
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}
