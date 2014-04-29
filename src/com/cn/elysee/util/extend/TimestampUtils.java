package com.cn.elysee.util.extend;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class TimestampUtils
{
	private static long day = 7;

	/**
	 * ��õ�ǰʱ���
	 * 
	 * @return
	 */
	public static String getTimestamp()
	{
		String unixTimeGMT;
		try
		{
			long unixTime = System.currentTimeMillis();
			unixTimeGMT = unixTime + "";
		}
		catch (Exception e)
		{
			unixTimeGMT = "";
		}
		return unixTimeGMT;

	}

	/**
	 * ��õ�ǰʱ���
	 * 
	 * @return
	 */
	public static long getIntTimestamp()
	{
		long unixTimeGMT = 0;
		try
		{
			unixTimeGMT = System.currentTimeMillis();
		}
		catch (Exception e)
		{
		}
		return unixTimeGMT;

	}

	/**
	 * ����ʱ������
	 * 
	 * @return
	 */
	public static boolean compareTimestamp(long currentTimestap,
			long oldTimestap)
	{
		Boolean isExceed = false;
		if (gapTimestamp(currentTimestap, oldTimestap) > 86400 * day)
		{
			isExceed = true;
		}
		return isExceed;
	}

	public static long gapTimestamp(long currentTimestap, long oldTimestap)
	{
		return (currentTimestap - oldTimestap);
	}

	/**
	 * ��ʱ�����ʽ���и�ʽ������֤ʱ�������Ϊ13λ
	 * 
	 * @param timestamp
	 *            ʱ���
	 * @return ����Ϊ13λ��ʱ���
	 */
	public static String formatTimestamp(String timestamp)
	{
		if (timestamp == null || "".equals(timestamp)) { return ""; }
		String tempTimeStamp = timestamp + "00000000000000";
		StringBuffer stringBuffer = new StringBuffer(tempTimeStamp);
		return tempTimeStamp = stringBuffer.substring(0, 13);
	}

	/**
	 * ���� timestamp ���ɸ���ʱ��״̬��
	 * 
	 * @param timestamp
	 *            ��1970 00:00:00 GMT������
	 * @param format
	 *            ��ʽ
	 * @return ʱ��״̬��(�磺�ո�5����ǰ)
	 */
	public static String getTimeState(String timestamp, String format)
	{
		if (timestamp == null || "".equals(timestamp)) { return ""; }

		try
		{
			timestamp = formatTimestamp(timestamp);
			long _timestamp = Long.parseLong(timestamp);
			if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000)
			{
				return "�ո�";
			}
			else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000)
			{
				return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
						+ "����ǰ";
			}
			else
			{
				Calendar now = Calendar.getInstance();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(_timestamp);
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("���� HH:mm");
					return sdf.format(c.getTime());
				}
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("���� HH:mm");
					return sdf.format(c.getTime());
				}
				else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR))
				{
					SimpleDateFormat sdf = null;
					if (format != null && !format.equalsIgnoreCase(""))
					{
						sdf = new SimpleDateFormat(format);

					}
					else
					{
						sdf = new SimpleDateFormat("M��d�� HH:mm:ss");
					}

					return sdf.format(c.getTime());
				}
				else
				{
					SimpleDateFormat sdf = null;
					if (format != null && !format.equalsIgnoreCase(""))
					{
						sdf = new SimpleDateFormat(format);

					}
					else
					{
						sdf = new SimpleDateFormat("yyyy��M��d�� HH:mm:ss");
					}
					return sdf.format(c.getTime());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}
}
