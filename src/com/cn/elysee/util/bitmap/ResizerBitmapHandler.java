package com.cn.elysee.util.bitmap;

import java.io.FileDescriptor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class ResizerBitmapHandler extends ProcessBitmapHandler
{

	protected int mImageWidth;
	protected int mImageHeight;
	protected Resources mResources;

	/**
	 * ��ʼ��һ��Ŀ���ṩͼ��Ŀ�Ⱥ͸߶ȵ�������ͼ��
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ResizerBitmapHandler(Context context, int imageWidth,
			int imageHeight)
	{
		mResources = context.getResources();
		setImageSize(imageWidth, imageHeight);
	}

	/**
	 * ��ʼ���ṩ��һĿ��ͼ���С(���ڿ�Ⱥ͸߶�);
	 * 
	 * @param context
	 * @param imageSize
	 */
	public ResizerBitmapHandler(Context context, int imageSize)
	{
		mResources = context.getResources();
		setImageSize(imageSize);
	}

	/**
	 * ����Ŀ��ͼƬ�Ŀ�ȸ߶�
	 * 
	 * @param width
	 * @param height
	 */
	public void setImageSize(int width, int height)
	{
		mImageWidth = width;
		mImageHeight = height;
	}

	/**
	 * ����Ŀ��ͼƬ�Ŀ�ȸ߶�
	 * 
	 * @param size
	 */
	public void setImageSize(int size)
	{
		setImageSize(size, size);
	}

	/**
	 * һ����Ҫ�Ĵ��������������ں�̨ʱ, ���������������ֻ�ǳ�ȡ����һ��λͼ����������Դ.
	 * 
	 * @param resId
	 * @return
	 */
	private Bitmap processBitmap(int resId)
	{
		return decodeSampledBitmapFromResource(mResources, resId, mImageWidth,
				mImageHeight);
	}

	@Override
	protected Bitmap processBitmap(Object data)
	{
		return processBitmap(Integer.parseInt(String.valueOf(data)));
	}

	/**
	 * ����һ���Ŀ��������Դ����ͳ�ȡһ��λͼ��
	 * 
	 * @param res
	 *            ��Դ����,���а�����ͼ������
	 * @param ��Դid��ͼ������
	 * @param reqWidth
	 *            ����Ŀ�Ȳ�����λͼ
	 * @param reqHeight
	 *            ����ĸ߶Ȳ�����λͼ
	 * @return
	 * 
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight)
	{

		// ��һ�����inJustDecodeBounds = true�����ά��
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ����inSampleSize����λͼ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * ����һ���Ŀ�������ļ�����ͳ�ȡһ��λͼ��
	 * 
	 * @param filename
	 *            �ļ���������·���ļ�������
	 * @param reqWidth
	 *            Ŀ��ͼƬ���
	 * @param reqHeight
	 *            Ŀ��ͼƬ�ĸ߶�
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight)
	{

		// ��һ�����inJustDecodeBounds = true�����ά��
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ����inSampleSize����λͼ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * ����һ���Ŀ����������������ͳ�ȡһ��λͼ��
	 * 
	 * @param fileDescriptor
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
	{

		// ��һ�����inJustDecodeBounds = true�����ά��
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// ����inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// ����inSampleSize����λͼ
		options.inJustDecodeBounds = false;
		return BitmapFactory
				.decodeFileDescriptor(fileDescriptor, null, options);
	}

	/**
	 * 
	 * 2014��4��21��
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight)
	{
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth)
		{
			if (width > height)
			{
				inSampleSize = Math.round((float) height / (float) reqHeight);
			}
			else
			{
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;

			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
			{
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}
