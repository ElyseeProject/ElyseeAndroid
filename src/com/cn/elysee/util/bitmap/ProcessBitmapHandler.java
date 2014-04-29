package com.cn.elysee.util.bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.cn.elysee.util.Logger;
import com.cn.elysee.util.cache.ProcessDataHandler;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public abstract class ProcessBitmapHandler extends ProcessDataHandler
{
	// ��ѹ��ͼƬ�����̵���Ĭ�ϸ�ʽ
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;

	@Override
	public byte[] processData(Object data)
	{
		// TODO Auto-generated method stub
		byte[] buffer = null;
		Bitmap bitmap = processBitmap(data);
		InputStream is = null;
		if (bitmap != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY,
					baos);
			is = new ByteArrayInputStream(baos.toByteArray());
			try
			{
				buffer = readStream(is);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Logger.d(ProcessBitmapHandler.this, "processData" + "ʧ��");
			}
		}
		return buffer;
	}

	/*
	 * �õ�ͼƬ�ֽ��� �����С
	 */
	public static byte[] readStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		return outStream.toByteArray();
	}

	protected abstract Bitmap processBitmap(Object data);
}
