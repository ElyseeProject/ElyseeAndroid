package com.cn.elysee.util.bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.cn.elysee.common.AndroidVersionCheckUtils;
import com.cn.elysee.util.Logger;
import com.cn.elysee.util.cache.DiskLruCache;
import com.cn.elysee.util.cache.ExternalOverFroyoUtils;
import com.cn.elysee.util.cache.ExternalUnderFroyoUtils;

/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class DownloadBitmapHandler extends ResizerBitmapHandler
{
	private static final String TAG = "TABitmapFetcher";
	private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final String HTTP_CACHE_DIR = "http";
	private static final int IO_BUFFER_SIZE = 8 * 1024;
	private DiskLruCache mHttpDiskCache;
	private File mHttpCacheDir;
	private boolean mHttpDiskCacheStarting = true;
	private final Object mHttpDiskCacheLock = new Object();
	private static final int DISK_CACHE_INDEX = 0;

	/**
	 * 初始化一个目标提供图像的宽度和高度的来处理图像
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public DownloadBitmapHandler(Context context, int imageWidth,
			int imageHeight)
	{
		super(context, imageWidth, imageHeight);
		init(context);
	}

	/**
	 * Initialize providing a single target image size (used for both width and
	 * height);
	 * 
	 * @param context
	 * @param imageSize
	 */
	public DownloadBitmapHandler(Context context, int imageSize)
	{
		super(context, imageSize);
		init(context);
	}

	private void init(Context context)
	{
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			mHttpCacheDir = ExternalOverFroyoUtils.getDiskCacheDir(context,
					HTTP_CACHE_DIR);
		}
		else
		{
			mHttpCacheDir = ExternalUnderFroyoUtils.getDiskCacheDir(context,
					HTTP_CACHE_DIR);
		}
		initDiskCacheInternal();
	}

	protected void initDiskCacheInternal()
	{
		initHttpDiskCache();
	}

	private void initHttpDiskCache()
	{
		if (!mHttpCacheDir.exists())
		{
			mHttpCacheDir.mkdirs();
		}
		synchronized (mHttpDiskCacheLock)
		{
			long usableSpace = 0;
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				usableSpace = ExternalOverFroyoUtils
						.getUsableSpace(mHttpCacheDir);
			}
			else
			{
				usableSpace = ExternalUnderFroyoUtils
						.getUsableSpace(mHttpCacheDir);
			}
			if (usableSpace > HTTP_CACHE_SIZE)
			{
				try
				{
					mHttpDiskCache = DiskLruCache.open(mHttpCacheDir, 1, 1,
							HTTP_CACHE_SIZE);
				}
				catch (IOException e)
				{
					mHttpDiskCache = null;
				}
			}
			mHttpDiskCacheStarting = false;
			mHttpDiskCacheLock.notifyAll();
		}
	}

	protected void clearCacheInternal()
	{
		synchronized (mHttpDiskCacheLock)
		{
			if (mHttpDiskCache != null && !mHttpDiskCache.isClosed())
			{
				try
				{
					mHttpDiskCache.delete();

				}
				catch (IOException e)
				{
					Logger.e(TAG, "clearCacheInternal - " + e);
				}
				mHttpDiskCache = null;
				mHttpDiskCacheStarting = true;
				initHttpDiskCache();
			}
		}
	}

	protected void flushCacheInternal()
	{
		synchronized (mHttpDiskCacheLock)
		{
			if (mHttpDiskCache != null)
			{
				try
				{
					mHttpDiskCache.flush();

				}
				catch (IOException e)
				{
					Logger.e(TAG, "flush - " + e);
				}
			}
		}
	}

	protected void closeCacheInternal()
	{
		synchronized (mHttpDiskCacheLock)
		{
			if (mHttpDiskCache != null)
			{
				try
				{
					if (!mHttpDiskCache.isClosed())
					{
						mHttpDiskCache.close();
						mHttpDiskCache = null;
					}
				}
				catch (IOException e)
				{
					Logger.e(TAG, "closeCacheInternal - " + e);
				}
			}
		}
	}

	private Bitmap processBitmap(String data)
	{
		String key = "";
		if (AndroidVersionCheckUtils.hasGingerbread())
		{
			key = ExternalOverFroyoUtils.hashKeyForDisk(data);
		}
		else
		{
			key = ExternalUnderFroyoUtils.hashKeyForDisk(data);
		}
		FileDescriptor fileDescriptor = null;
		FileInputStream fileInputStream = null;
		DiskLruCache.Snapshot snapshot;
		synchronized (mHttpDiskCacheLock)
		{
			while (mHttpDiskCacheStarting)
			{
				try
				{
					mHttpDiskCacheLock.wait();
				}
				catch (InterruptedException e)
				{
				}
			}

			if (mHttpDiskCache != null)
			{
				try
				{
					snapshot = mHttpDiskCache.get(key);
					if (snapshot == null)
					{

						DiskLruCache.Editor editor = mHttpDiskCache.edit(key);
						if (editor != null)
						{
							if (downloadUrlToStream(data,
									editor.newOutputStream(DISK_CACHE_INDEX)))
							{
								editor.commit();
							}
							else
							{
								editor.abort();
							}
						}
						snapshot = mHttpDiskCache.get(key);
					}
					if (snapshot != null)
					{
						fileInputStream = (FileInputStream) snapshot
								.getInputStream(DISK_CACHE_INDEX);
						fileDescriptor = fileInputStream.getFD();
					}
				}
				catch (IOException e)
				{
					Logger.e(TAG, "processBitmap - " + e);
				}
				catch (IllegalStateException e)
				{
					Logger.e(TAG, "processBitmap - " + e);
				}
				finally
				{
					if (fileDescriptor == null && fileInputStream != null)
					{
						try
						{
							fileInputStream.close();
						}
						catch (IOException e)
						{
						}
					}
				}
			}
		}

		Bitmap bitmap = null;
		if (fileDescriptor != null)
		{
			bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor,
					mImageWidth, mImageHeight);
		}
		if (fileInputStream != null)
		{
			try
			{
				fileInputStream.close();
			}
			catch (IOException e)
			{
			}
		}
		return bitmap;
	}

	@Override
	protected Bitmap processBitmap(Object data)
	{
		return processBitmap(String.valueOf(data));
	}

	/**
	 * 
	 * 2014年4月21日
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public boolean downloadUrlToStream(String urlString,
			OutputStream outputStream)
	{
		disableConnectionReuseIfNecessary();
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;

		try
		{
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(),
					IO_BUFFER_SIZE);
			out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

			int b;
			while ((b = in.read()) != -1)
			{
				out.write(b);
			}
			return true;
		}
		catch (final IOException e)
		{
			Logger.e(TAG, "Error in downloadBitmap - " + e);
		}
		finally
		{
			if (urlConnection != null)
			{
				urlConnection.disconnect();
			}
			try
			{
				if (out != null)
				{
					out.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (final IOException e)
			{
			}
		}
		return false;
	}

	/**
	 * 
	 * 2014年4月21日
	 */
	public static void disableConnectionReuseIfNecessary()
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
		{
			System.setProperty("http.keepAlive", "false");
		}
	}
}
