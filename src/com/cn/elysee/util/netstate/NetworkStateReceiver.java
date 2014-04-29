package com.cn.elysee.util.netstate;

import java.util.ArrayList;

import com.cn.elysee.util.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cn.elysee.util.netstate.NetWorkUtil.netType;
/**
 * @author hzx
 * @Description ��һ���������״̬�ı�ģ���Ҫ���� <receiver
 *              android:name="com.ta.util.netstate.NetworkStateReceiver" >
 *              <intent-filter> <action
 *              android:name="android.net.conn.CONNECTIVITY_CHANGE" /> <action
 *              android:name="android.gzcpc.conn.CONNECTIVITY_CHANGE" />
 *              </intent-filter> </receiver>
 * 
 *              ��Ҫ����Ȩ�� <uses-permission
 *              android:name="android.permission.CHANGE_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.CHANGE_WIFI_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_NETWORK_STATE" />
 *              <uses-permission
 *              android:name="android.permission.ACCESS_WIFI_STATE" />
 *              2014��4��21��
 * @version V1.0
 */
public class NetworkStateReceiver extends BroadcastReceiver
{

	private static Boolean networkAvailable = false;
	private static netType netType;
	private static ArrayList<NetChangeObserver> netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public final static String TA_ANDROID_NET_CHANGE_ACTION = "elysee.android.net.conn.CONNECTIVITY_CHANGE";
	private static BroadcastReceiver receiver;

	private static BroadcastReceiver getReceiver()
	{
		if (receiver == null)
		{
			receiver = new NetworkStateReceiver();
		}
		return receiver;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		receiver = NetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
				|| intent.getAction().equalsIgnoreCase(
						TA_ANDROID_NET_CHANGE_ACTION))
		{
			Logger.i(NetworkStateReceiver.this, "����״̬�ı�.");
			if (!NetWorkUtil.isNetworkAvailable(context))
			{
				Logger.i(NetworkStateReceiver.this, "û����������.");
				networkAvailable = false;
			}
			else
			{
				Logger.i(NetworkStateReceiver.this, "�������ӳɹ�.");
				netType = NetWorkUtil.getAPNType(context);
				networkAvailable = true;
			}
			notifyObserver();
		}
	}

	/**
	 * ע������״̬�㲥
	 * 
	 * @param mContext
	 */
	public static void registerNetworkStateReceiver(Context mContext)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(TA_ANDROID_NET_CHANGE_ACTION);
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		mContext.getApplicationContext()
				.registerReceiver(getReceiver(), filter);
	}

	/**
	 * �������״̬
	 * 
	 * @param mContext
	 */
	public static void checkNetworkState(Context mContext)
	{
		Intent intent = new Intent();
		intent.setAction(TA_ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}

	/**
	 * ע������״̬�㲥
	 * 
	 * @param mContext
	 */
	public static void unRegisterNetworkStateReceiver(Context mContext)
	{
		if (receiver != null)
		{
			try
			{
				mContext.getApplicationContext().unregisterReceiver(receiver);
			}
			catch (Exception e)
			{
				Logger.d("TANetworkStateReceiver", e.getMessage());
			}
		}

	}

	/**
	 * ��ȡ��ǰ����״̬��trueΪ�������ӳɹ���������������ʧ��
	 * 
	 * @return
	 */
	public static Boolean isNetworkAvailable()
	{
		return networkAvailable;
	}

	public static netType getAPNType()
	{
		return netType;
	}

	private void notifyObserver()
	{

		for (int i = 0; i < netChangeObserverArrayList.size(); i++)
		{
			NetChangeObserver observer = netChangeObserverArrayList.get(i);
			if (observer != null)
			{
				if (isNetworkAvailable())
				{
					observer.onConnect(netType);
				}
				else
				{
					observer.onDisConnect();
				}
			}
		}

	}

	/**
	 * ע���������ӹ۲���
	 * 
	 * @param observerKey
	 *            observerKey
	 */
	public static void registerObserver(NetChangeObserver observer)
	{
		if (netChangeObserverArrayList == null)
		{
			netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
		}
		netChangeObserverArrayList.add(observer);
	}

	/**
	 * ע���������ӹ۲���
	 * 
	 * @param resID
	 *            observerKey
	 */
	public static void removeRegisterObserver(NetChangeObserver observer)
	{
		if (netChangeObserverArrayList != null)
		{
			netChangeObserverArrayList.remove(observer);
		}
	}

}
