package com.cn.elysee.util.extend.share;

import android.content.Context;
import android.content.Intent;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class MailShareUtil
{
	/**
	 * �ʼ�����
	 * 
	 * @param mContext
	 * @param title
	 *            �ʼ��ı���
	 * @param text
	 *            �ʼ�������
	 * @return
	 */
	public static Boolean sendMail(Context mContext, String title, String text)
	{
		// ����ϵͳ���ʼ�
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// �����ı���ʽ
		emailIntent.setType("text/plain");
		// ���öԷ��ʼ���ַ
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// ���ñ�������
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		// �����ʼ��ı�����
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		mContext.startActivity(Intent.createChooser(emailIntent,
				"Choose Email Client"));
		return null;
	}
}
