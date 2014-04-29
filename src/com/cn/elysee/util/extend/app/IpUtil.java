package com.cn.elysee.util.extend.app;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.cn.elysee.util.Logger;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class IpUtil
{
	/**
	 * 使用Wifi时获取IP 设置用户权限
	 * 
	 * <uses-permission
	 * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
	 * 
	 * <uses-permission
	 * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
	 * 
	 * <uses-permission
	 * android:name="android.permission.WAKE_LOCK"></uses-permission>
	 * 
	 * @return
	 */
	public static String getWifiIp(Context context)
	{
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return intToIp(ipAddress);
	}

	private static String intToIp(int i)
	{
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);

	}

	/**
	 * 使用GPRS上网，时获取ip地址，设置用户上网权限
	 * 
	 * <uses-permission
	 * android:name="android.permission.INTERNET"></uses-permission>
	 * 
	 * @return
	 */
	public static String getGPRSIp()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) { return inetAddress
							.getHostAddress().toString(); }
				}
			}
		}
		catch (SocketException ex)
		{
			Logger.d("IpUtil", ex.getMessage());
		}
		return "";
	}

}
