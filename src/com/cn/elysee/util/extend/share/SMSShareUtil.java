package com.cn.elysee.util.extend.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class SMSShareUtil
{
	/**
	 * ���ŷ���
	 * 
	 * @param mContext
	 * @param smstext
	 *            ���ŷ�������
	 * @return
	 */
	public static Boolean sendSms(Context mContext, String smstext)
	{
		Uri smsToUri = Uri.parse("smsto:");
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
				smsToUri);
		mIntent.putExtra("sms_body", smstext);
		mContext.startActivity(mIntent);
		return null;
	}
}
