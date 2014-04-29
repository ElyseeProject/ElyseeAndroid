package com.cn.elysee.util.extend.app;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author hzx 
 * 2014��4��21��
 * @Description �û����ؿͻ���SIM����һЩ��Ϣ ��Ҫ���� <uses-permission
 *              android:name="android.permission.READ_PHONE_STATE"/>
 * @version V1.0
 */
public class SIMCardInfoUtil
{
	/**
	 * ���ر����ֻ����룬������벻һ���ܻ�ȡ��
	 * 
	 * @param context
	 * @return
	 */
	public static String getNativePhoneNumber(Context context)
	{
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	/**
	 * �����ֻ�����������
	 * 
	 * @param context
	 * @return
	 */
	public static String getProvidersName(Context context)
	{
		String ProvidersName = null;
		// ����Ψһ���û�ID;�������ſ��ı�������
		String IMSI = getIMSI(context);
		// IMSI��ǰ��3λ460�ǹ��ң������ź���2λ00 02���й��ƶ���01���й���ͨ��03���й����š�
		System.out.println(IMSI);
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002"))
		{
			ProvidersName = "�й��ƶ�";
		}
		else if (IMSI.startsWith("46001"))
		{
			ProvidersName = "�й���ͨ";
		}
		else if (IMSI.startsWith("46003"))
		{
			ProvidersName = "�й�����";
		}
		else
		{
			ProvidersName = "����������";
		}
		return ProvidersName;
	}

	/**
	 * �����ֻ�IMSI����
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context)
	{
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// ����Ψһ���û�ID;�������ſ���IMSI���
		return telephonyManager.getSubscriberId();
	}
}
