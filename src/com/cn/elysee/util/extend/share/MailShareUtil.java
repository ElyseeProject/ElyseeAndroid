package com.cn.elysee.util.extend.share;

import android.content.Context;
import android.content.Intent;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class MailShareUtil
{
	/**
	 * 邮件分享
	 * 
	 * @param mContext
	 * @param title
	 *            邮件的标题
	 * @param text
	 *            邮件的内容
	 * @return
	 */
	public static Boolean sendMail(Context mContext, String title, String text)
	{
		// 调用系统发邮件
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// 设置文本格式
		emailIntent.setType("text/plain");
		// 设置对方邮件地址
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// 设置标题内容
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		// 设置邮件文本内容
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		mContext.startActivity(Intent.createChooser(emailIntent,
				"Choose Email Client"));
		return null;
	}
}
