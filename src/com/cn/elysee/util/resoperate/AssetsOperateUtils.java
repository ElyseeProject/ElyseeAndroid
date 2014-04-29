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
 * 2014年4月21日
 * @version V1.0
 */
public class AssetsOperateUtils
{
	private static final String TAG = "TAAssetsOperateUtils";

	/**
	 * 通过文件名从Assets中获得资源,以输入流的形式返回
	 * 
	 * @param context
	 * @param fileName
	 *            文件名应为assets文件下载绝对路径
	 * @return 以InputStream的形式返回
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
	 * 通过文件名从Assets中获得资源，以字符串的形式返回
	 * 
	 * @param context
	 * @param fileName
	 *            文件名应为assets文件下载绝对路径
	 * @return 以字符串的形式返回
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
	 * 通过文件名从Assets中获得资源，以位图的形式返回
	 * 
	 * @param context
	 * @param fileName
	 *            文件名应为assets文件下载绝对路径
	 * @return 以位图的形式返回
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
