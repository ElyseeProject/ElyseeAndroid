package com.cn.elysee.util.resoperate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cn.elysee.util.Logger;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class AssetsOperateUtils
{
	private static final String TAG = "TAAssetsOperateUtils";

	/**
	 * ͨ���ļ�����Assets�л����Դ,������������ʽ����
	 * 
	 * @param context
	 * @param fileName
	 *            �ļ���ӦΪassets�ļ����ؾ���·��
	 * @return ��InputStream����ʽ����
	 */
	public static InputStream getInputStreamForName(Context context,
			String fileName)
	{
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		try
		{
			inputStream = assetManager.open(fileName);
		}
		catch (IOException e)
		{
			Logger.d(TAG, e.getMessage());
		}
		return inputStream;
	}

	/**
	 * ͨ���ļ�����Assets�л����Դ�����ַ�������ʽ����
	 * 
	 * @param context
	 * @param fileName
	 *            �ļ���ӦΪassets�ļ����ؾ���·��
	 * @return ���ַ�������ʽ����
	 */
	public static String getStringForName(Context context, String fileName)
	{
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try
		{
			inputStream = getInputStreamForName(context, fileName);
			while ((len = inputStream.read(buf)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		}
		catch (IOException e)
		{
			Logger.d(TAG, e.getMessage());
		}
		return outputStream.toString();
	}

	/**
	 * ͨ���ļ�����Assets�л����Դ����λͼ����ʽ����
	 * 
	 * @param context
	 * @param fileName
	 *            �ļ���ӦΪassets�ļ����ؾ���·��
	 * @return ��λͼ����ʽ����
	 */
	public static Bitmap getBitmapForName(Context context, String fileName)
	{
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try
		{
			inputStream = getInputStreamForName(context, fileName);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		}
		catch (IOException e)
		{
			Logger.d(TAG, e.getMessage());
		}
		return bitmap;
	}

}
