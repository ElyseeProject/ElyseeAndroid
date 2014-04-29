package com.cn.elysee.util.download;

import com.cn.elysee.common.StringUtils;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;


/**
 * @author hzx 
 * 2014年4月21日
 * @version V1.0
 */
public class DownloadService extends Service
{

	// 这样能在后台运行么？
	private DownloadManager mDownloadManager;

	@Override
	public IBinder onBind(Intent intent)
	{

		return new DownloadServiceImpl();
	}

	@Override
	public void onCreate()
	{

		super.onCreate();
		// 这样能在后台运行么？
		mDownloadManager = DownloadManager.getDownloadManager();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
	}

	private class DownloadServiceImpl extends IDownloadService.Stub
	{

		public void startManage() throws RemoteException
		{

			mDownloadManager.startManage();
		}

		public void addTask(String url) throws RemoteException
		{
			if (!StringUtils.isEmpty(url))
			{
				mDownloadManager.addHandler(url);
			}

		}

		public void pauseTask(String url) throws RemoteException
		{
			if (!StringUtils.isEmpty(url))
			{
				mDownloadManager.pauseHandler(url);
			}
		}

		public void deleteTask(String url) throws RemoteException
		{
			if (!StringUtils.isEmpty(url))
			{
				mDownloadManager.deleteHandler(url);
			}
		}

		public void continueTask(String url) throws RemoteException
		{
			if (!StringUtils.isEmpty(url))
			{
				mDownloadManager.continueHandler(url);
			}
		}

		public void pauseAll(String url) throws RemoteException
		{
			if (!StringUtils.isEmpty(url))
			{
				mDownloadManager.pauseAllHandler();
			}
		}

		public void stopManage() throws RemoteException
		{
			mDownloadManager.close();
		}

	}

}
