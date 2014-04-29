package com.cn.elysee.util.bitmap;

import java.io.FileDescriptor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class ResizerBitmapHandler extends ProcessBitmapHandler
{

	protected int mImageWidth;
	protected int mImageHeight;
	protected Resources mResources;

	/**
	 * 初始化一个目标提供图像的宽度和高度的来处理图像
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
	 * 初始化提供单一目标图像大小(用于宽度和高度);
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
	 * 设置目标图片的宽度高度
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
	 * 设置目标图片的宽度高度
	 * 
	 * @param size
	 */
	public void setImageSize(int size)
	{
		setImageSize(size, size);
	}

	/**
	 * 一个主要的处理方法，当发生在后台时, 在这种情况下我们只是抽取出来一个位图，并返回资源.
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
	 * 按照一定的宽度来从资源解码和抽取一个位图。
	 * 
	 * @param res
	 *            资源对象,其中包含了图像数据
	 * @param 资源id的图像数据
	 * @param reqWidth
	 *            请求的宽度产生的位图
	 * @param reqHeight
	 *            请求的高度产生的位图
	 * @return
	 * 
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight)
	{

		// 第一解码和inJustDecodeBounds = true来检查维度
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// 计算inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 根据inSampleSize解码位图
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 按照一定的宽度来从文件解码和抽取一个位图。
	 * 
	 * @param filename
	 *            文件名的完整路径文件来解码
	 * @param reqWidth
	 *            目标图片宽度
	 * @param reqHeight
	 *            目标图片的高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight)
	{

		// 第一解码和inJustDecodeBounds = true来检查维度
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// 计算inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 根据inSampleSize解码位图
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * 按照一定的宽度来从输入流解码和抽取一个位图。
	 * 
	 * @param fileDescriptor
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
	{

		// 第一解码和inJustDecodeBounds = true来检查维度
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// 计算inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 根据inSampleSize解码位图
		options.inJustDecodeBounds = false;
		return BitmapFactory
				.decodeFileDescriptor(fileDescriptor, null, options);
	}

	/**
	 * 
	 * 2014年4月21日
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
