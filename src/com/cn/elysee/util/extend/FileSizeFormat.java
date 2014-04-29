package com.cn.elysee.util.extend;

import java.text.DecimalFormat;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class FileSizeFormat
{
	private static String kB_UNIT_NAME = "KB";
	private static String B_UNIT_NAME = "B";
	private static String MB_UNIT_NAME = "MB";

	public static String getSizeString(long size)
	{
		if (size < 1024)
		{
			return String.valueOf(size) + B_UNIT_NAME;
		}
		else
		{
			size = size / 1024;
		}
		if (size < 1024)
		{
			return String.valueOf(size) + kB_UNIT_NAME;
		}
		else
		{
			size = size * 100 / 1024;
		}

		return String.valueOf((size / 100)) + "."
				+ ((size % 100) < 10 ? "0" : "") + String.valueOf((size % 100))
				+ MB_UNIT_NAME;
	}

	/**
	 * ��MbΪ��λ������λС��
	 * 
	 * @param dirSize
	 * @return
	 */
	public static String getMbSize(long dirSize)
	{
		double size = 0;
		size = (dirSize + 0.0) / (1024 * 1024);
		DecimalFormat df = new DecimalFormat("0.00");// ��MbΪ��λ������λС��
		String filesize = df.format(size);
		return filesize;
	}

	/**
	 * ��MbΪ��λ������λС��
	 * 
	 * @param dirSize
	 * @return
	 */
	public static String getKBSize(long dirSize)
	{
		double size = 0;
		size = (dirSize + 0.0) / 1024;
		DecimalFormat df = new DecimalFormat("0.00");// ��KBΪ��λ������λС��
		String filesize = df.format(size);
		return filesize;
	}
}
