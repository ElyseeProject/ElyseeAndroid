package com.cn.elysee.util.netstate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class NetWorkUtil
{
	public static enum netType
	{
		wifi, CMNET, CMWAP, noneNet
	}

	/**
	 * �����Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null)
		{
			for (int i = 0; i < info.length; i++)
			{
				if (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; }
			}
		}
		return false;
	}

	/**
	 * �ж��Ƿ�����������
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) { return mNetworkInfo.isAvailable(); }
		}
		return false;
	}

	/**
	 * �ж�WIFI�����Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) { return mWiFiNetworkInfo
					.isAvailable(); }
		}
		return false;
	}

	/**
	 * �ж�MOBILE�����Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) { return mMobileNetworkInfo
					.isAvailable(); }
		}
		return false;
	}

	/**
	 * ��ȡ��ǰ�������ӵ�������Ϣ
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) { return mNetworkInfo
					.getType(); }
		}
		return -1;
	}

	/**
	 * 
	 * @author ��è
	 * 
	 *         ��ȡ��ǰ������״̬ -1��û������ 1��WIFI����2��wap ����3��net����
	 * 
	 * @param context
	 * 
	 * @return
	 */
	public static netType getAPNType(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) { return netType.noneNet; }
		int nType = networkInfo.getType();

		if (nType == ConnectivityManager.TYPE_MOBILE)
		{
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
			{
				return netType.CMNET;
			}

			else
			{
				return netType.CMWAP;
			}
		}
		else if (nType == ConnectivityManager.TYPE_WIFI) { return netType.wifi; }
		return netType.noneNet;

	}
}
