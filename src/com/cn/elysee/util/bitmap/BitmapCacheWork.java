package com.cn.elysee.util.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.cn.elysee.util.cache.FileCacheWork;
import com.cn.elysee.util.cache.FileCache;
import com.cn.elysee.util.cache.FileCache.CacheParams;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
public class BitmapCacheWork extends FileCacheWork<ImageView>
{
	protected Resources mResources;
	@SuppressWarnings("unused")
	private CacheParams mCacheParams;
	private Context mContext;

	public BitmapCacheWork(Context context)
	{
		mResources = context.getResources();
		this.mContext = context;
	}

	@Override
	public void loadFormCache(Object data, ImageView responseObject)
	{
		if (getCallBackHandler() == null)
		{
			BitmapCallBackHanlder callBackHanlder = new BitmapCallBackHanlder();
			setCallBackHandler(callBackHanlder);
		}
		if (getProcessDataHandler() == null)
		{
			DownloadBitmapHandler downloadBitmapFetcher = new DownloadBitmapHandler(
					mContext, 100);
			setProcessDataHandler(downloadBitmapFetcher);
		}
		super.loadFormCache(data, responseObject);
	}

	/**
	 * 设置图片缓存
	 * 
	 * @param cacheParams
	 *            响应参数
	 */
	public void setBitmapCache(CacheParams cacheParams)
	{
		mCacheParams = cacheParams;
		setFileCache(new FileCache(cacheParams));
	}

	@Override
	protected void initDiskCacheInternal()
	{
		// TODO Auto-generated method stub
		DownloadBitmapHandler downloadBitmapFetcher = (DownloadBitmapHandler) getProcessDataHandler();
		super.initDiskCacheInternal();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.initDiskCacheInternal();
		}
	}

	@Override
	protected void clearCacheInternal()
	{
		super.clearCacheInternal();
		DownloadBitmapHandler downloadBitmapFetcher = (DownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.clearCacheInternal();
		}
	}

	@Override
	protected void flushCacheInternal()
	{
		super.flushCacheInternal();
		DownloadBitmapHandler downloadBitmapFetcher = (DownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.flushCacheInternal();
		}
	}

	@Override
	protected void closeCacheInternal()
	{
		super.closeCacheInternal();
		DownloadBitmapHandler downloadBitmapFetcher = (DownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.closeCacheInternal();
		}
	}
}
