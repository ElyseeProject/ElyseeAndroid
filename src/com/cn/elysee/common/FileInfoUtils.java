package com.cn.elysee.common;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class FileInfoUtils
{
	/**
	 * ��ȡ�ļ���С 2014��4��21��
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "resource", "unused" })
	public static long getFileSizes(File f) throws Exception
	{
		long s = 0;
		if (f.exists())
		{
			FileInputStream fileInputStream = new FileInputStream(f);
			s = fileInputStream.available();
		}
		else
		{
			f.createNewFile();
		}
		return 0;
	}

	/**
	 * �ݹ�ȡ���ļ��д�С 2014��4��21��
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++)
		{
			if (flist[i].isDirectory())
			{
				size = size + getFileSize(flist[i]);
			}
			else
			{
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * ת���ļ���С 2014��4��21��
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS)
	{
		DecimalFormat df = new DecimalFormat("#0.00");
		String fileSizeString = "";
		if (fileS < 1024)
		{
			fileSizeString = df.format((double) fileS) + "B";
		}
		else if (fileS < 1048576)
		{
			fileSizeString = df.format((double) fileS / 1024) + "K";
		}
		else if (fileS < 1073741824)
		{
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		}
		else
		{
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * �ݹ���ȡĿ¼�ļ�����
	 *  2014��4��21��
	 * @param f
	 * @return
	 */
	public static long getlist(File f)
	{
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++)
		{
			if (flist[i].isDirectory())
			{
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

}
