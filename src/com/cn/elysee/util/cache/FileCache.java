package com.cn.elysee.util.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

import com.cn.elysee.common.AndroidVersionCheckUtils;
import com.cn.elysee.util.Logger;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class FileCache
{
	// Ĭ���ڴ洢�洢��С
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
	// Ĭ�ϴ��̴洢��С
	public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	// ��ѹ��ͼƬ�����̵���Ĭ�ϸ�ʽ
	private static final int DEFAULT_COMPRESS_QUALITY = 70;
	private static final int DISK_CACHE_INDEX = 0;
	// ��Щ�������������л���ͬ�Ļ���
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true; // Ĭ���ڴ滺������
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true; // Ĭ�ϴ��̻�������
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;// Ĭ�ϵ������Ĵ��̻��濪ʼ
	private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false; // Ĭ�ϵĳ�ʼ���Ĵ��̸��ٻ��濪ʼ
	private CacheParams mCacheParams;
	private boolean mDiskCacheStarting = true;
	private LruCache<String, byte[]> mMemoryCache;
	private DiskLruCache mDiskLruCache;
	private final Object mDiskCacheLock = new Object();

	/**
	 * ����һ���µ�TAFileCache����ʹ��ָ���Ĳ�����
	 * 
	 * @param cacheParams
	 *            �������������ʼ������
	 */
	public FileCache(CacheParams cacheParams)
	{
		init(cacheParams);
	}

	/**
	 * ����һ������
	 * 
	 * @param context
	 *            ��������Ϣ
	 * @param uniqueName
	 *            �����ʾ���ֻᱻ��ӵ����ɾ����ļ���
	 * 
	 */
	public FileCache(Context context, String uniqueName)
	{
		init(new CacheParams(context, uniqueName));
	}

	private void init(CacheParams cacheParams)
	{
		mCacheParams = cacheParams;
		if (mCacheParams.memoryCacheEnabled)
		{
			mMemoryCache = new LruCache<String, byte[]>(
					mCacheParams.memCacheSize)
			{
				@Override
				protected int sizeOf(String key, byte[] buffer)
				{
					return getSize(key, buffer);
				}

			};
		}

		// ���Ĭ�ϵĴ��̻���û�г�ʼ�����Ǿ���Ҫ��ʼ������һ���������߳����ڴ��̷��ʡ�
		if (!mCacheParams.initDiskCacheOnCreate)
		{
			initDiskCache();
		}
	}

	/**
	 * ��ʼ�����̻���
	 */
	public void initDiskCache()
	{
		synchronized (mDiskCacheLock)
		{
			if (mDiskLruCache == null || mDiskLruCache.isClosed())
			{
				File diskCacheDir = mCacheParams.diskCacheDir;
				if (mCacheParams.diskCacheEnabled && diskCacheDir != null)
				{
					if (!diskCacheDir.exists())
					{
						diskCacheDir.mkdirs();
					}
					long usableSpace = 0;
					if (AndroidVersionCheckUtils.hasGingerbread())
					{
						usableSpace = ExternalOverFroyoUtils
								.getUsableSpace(diskCacheDir);
					}
					else
					{
						usableSpace = ExternalUnderFroyoUtils
								.getUsableSpace(diskCacheDir);
					}
					if (usableSpace > mCacheParams.diskCacheSize)
					{
						try
						{

							mDiskLruCache = DiskLruCache.open(diskCacheDir, 1,
									1, DEFAULT_DISK_CACHE_SIZE);
						}
						catch (final IOException e)
						{
							mCacheParams.diskCacheDir = null;
							Logger.e(FileCache.this, "initDiskCache - " + e);
						}
					}
				}
			}
			mDiskCacheStarting = false;
			mDiskCacheLock.notifyAll();
		}
	}

	/**
	 * ��� byte[]�������ݵ��ڴ滺��ʹ��̻���
	 * 
	 * @param data
	 *            byte[]��Ωһ��ʶ�����洢,һ����URL
	 * @param buffer
	 *            ��Ҫ��ӵ����������
	 */
	public void addBufferToCache(String data, byte[] buffer)
	{
		if (data == null || buffer == null) { return; }

		// ��ӵ��ڴ滺��
		if (mMemoryCache != null && mMemoryCache.get(data) == null)
		{
			mMemoryCache.put(data, buffer);
		}

		synchronized (mDiskCacheLock)
		{
			// ��ӵ����̻���
			if (mDiskLruCache != null)
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
				OutputStream out = null;
				try
				{
					DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
					if (snapshot == null)
					{
						final DiskLruCache.Editor editor = mDiskLruCache
								.edit(key);
						if (editor != null)
						{
							out = editor.newOutputStream(DISK_CACHE_INDEX);
							out.write(buffer, 0, buffer.length);
							editor.commit();
							out.close();
						}
					}
					else
					{
						snapshot.getInputStream(DISK_CACHE_INDEX).close();
					}
				}
				catch (final IOException e)
				{
					Logger.e(FileCache.this, "addBufferToCache - " + e);
				}
				catch (Exception e)
				{
					Logger.e(FileCache.this, "addBufferToCache - " + e);
				}
				finally
				{
					try
					{
						if (out != null)
						{
							out.close();
						}
					}
					catch (IOException e)
					{
					}
				}
			}
		}
	}

	/**
	 * ���ڴ滺���ȡ���ݣ�����ڴ滺��û�У�����Ӵ��̻����ȡ��䵽�ڴ滺��
	 * 
	 * @param data
	 *            byte[]��Ωһ��ʶ�����洢,һ����URL
	 * @return ����byte[]���͵�һ������
	 */
	public byte[] getBufferFromMemCache(String data)
	{
		byte[] memValue = null;
		try
		{
			if (mMemoryCache != null)
			{
				memValue = mMemoryCache.get(data);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Logger.e(FileCache.this, "��ȡ��������ʧ�ܣ�");
		}
		return memValue;
	}

	/**
	 * �Ӵ��̻����л�ȡ����
	 * 
	 * @param data
	 *            ���صı�ʶ����Ŀ�õ�
	 * @return ����ڻ������ҵ���Ӧ�����ݣ��򷵻�����,����Ϊnull
	 */
	public byte[] getBufferFromDiskCache(String data)
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

		synchronized (mDiskCacheLock)
		{
			while (mDiskCacheStarting)
			{
				try
				{
					mDiskCacheLock.wait();
				}
				catch (InterruptedException e)
				{
				}
			}
			if (mDiskLruCache != null)
			{
				byte[] buffer = null;
				try
				{
					final DiskLruCache.Snapshot snapshot = mDiskLruCache
							.get(key);
					if (snapshot != null)
					{
						InputStream fileInputStream = snapshot
								.getInputStream(DISK_CACHE_INDEX);

						buffer = readStream(fileInputStream);
						// ������ݵ��ڴ滺��
						if (buffer != null && mMemoryCache != null
								&& mMemoryCache.get(data) == null)
						{
							mMemoryCache.put(data, buffer);
						}
						return buffer;
					}
				}
				catch (final IOException e)
				{

					Logger.e(FileCache.this, "getBufferFromDiskCache - "
							+ e);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					Logger.e(FileCache.this, "getBufferFromDiskCache - "
							+ e);
				}
				finally
				{

				}
			}
			return null;
		}
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

	/**
	 * λͼ��Ωһ��ʶ�����洢����ڴ�ʹ��̻��涼TAFileCache������ ���� ע��,������Դ��̷���,�������ǲ�Ӧ�������̻߳�
	 * UI�߳���ִ�еġ�
	 */
	public void clearCache()
	{
		if (mMemoryCache != null)
		{
			mMemoryCache.evictAll();
		}

		synchronized (mDiskCacheLock)
		{
			mDiskCacheStarting = true;
			if (mDiskLruCache != null && !mDiskLruCache.isClosed())
			{
				try
				{
					mDiskLruCache.delete();

				}
				catch (IOException e)
				{
					Logger.e(FileCache.this, "clearCache - " + e);
				}
				mDiskLruCache = null;
				initDiskCache();
			}
		}
	}

	/**
	 * ���̻���ˢ��TAFileCache�����صĶ���ע��,������Դ��̷���,�������ǲ�Ӧ�������̻߳� UI�߳���ִ�еġ�
	 */
	public void flush()
	{
		synchronized (mDiskCacheLock)
		{
			if (mDiskLruCache != null)
			{
				try
				{
					mDiskLruCache.flush();
				}
				catch (IOException e)
				{
					Logger.e(FileCache.this, "flush - " + e);
				}
			}
		}
	}

	/**
	 * �رմ��̻���������TAFileCache����ע��,������Դ��̷���,�������ǲ�Ӧ�������̻߳� UI�߳���ִ�еġ�
	 */
	public void close()
	{
		synchronized (mDiskCacheLock)
		{
			if (mDiskLruCache != null)
			{
				try
				{
					if (!mDiskLruCache.isClosed())
					{
						mDiskLruCache.close();
						mDiskLruCache = null;
					}
				}
				catch (IOException e)
				{
					Logger.e(FileCache.this, "close" + e);
				}
			}
		}
	}

	/**
	 * �ӻ��byte[]�ĳ���
	 * 
	 * @param key
	 * @param value
	 * @return bytes���͵ĳ���
	 */
	private int getSize(String key, byte[] value)
	{
		return value.length;
	}

	/**
	 * @Title TACacheParams
	 * @Package com.ta.util.cache
	 * @Description ����Ĳ�����
	 * @author ��è
	 * @date 2013-1-20
	 * @version V1.0
	 */
	public static class CacheParams
	{
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public File diskCacheDir;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		public boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;

		/**
		 * ��ʼ�����̲���
		 * 
		 * @param context
		 *            ������
		 * @param uniqueName
		 *            �����ļ�����
		 */
		public CacheParams(Context context, String uniqueName)
		{
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				diskCacheDir = ExternalOverFroyoUtils.getDiskCacheDir(
						context, uniqueName);
			}
			else
			{
				diskCacheDir = ExternalUnderFroyoUtils.getDiskCacheDir(
						context, uniqueName);
			}
		}

		/**
		 * ��ʼ�����̲���
		 * 
		 * @param diskCacheDir
		 *            �����ļ���
		 */
		public CacheParams(File diskCacheDir)
		{
			this.diskCacheDir = diskCacheDir;
		}

		/**
		 * ���û���Ĵ�С
		 * 
		 * @param context
		 *            ������
		 * @param percent
		 *            ���÷��仺��Ϊ���豸�İٶȱȣ���0.01f����
		 * 
		 */
		public void setMemCacheSizePercent(Context context, float percent)
		{
			if (percent < 0.05f || percent > 0.8f) { throw new IllegalArgumentException(
					"setMemCacheSizePercent - percent must be "
							+ "between 0.05 and 0.8 (inclusive)"); }
			memCacheSize = Math.round(percent * getMemoryClass(context) * 1024
					* 1024);
		}

		private static int getMemoryClass(Context context)
		{
			if (AndroidVersionCheckUtils.hasGingerbread())
			{
				return ExternalOverFroyoUtils.getMemoryClass(context);
			}
			else
			{
				return ExternalUnderFroyoUtils.getMemoryClass(context);
			}

		}
	}
}
