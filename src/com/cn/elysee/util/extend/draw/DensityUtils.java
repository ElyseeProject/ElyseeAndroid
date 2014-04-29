package com.cn.elysee.util.extend.draw;

import android.content.Context;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class DensityUtils
{
	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 * 
	 * @param context
	 * @param dpValue
	 *            dpֵ
	 * @return ��������ֵ
	 */
	public static int dipTopx(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 * 
	 * @param context
	 * @param pxValue
	 *            ����ֵ
	 * @return ����dpֵ
	 */
	public static int pxTodip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
