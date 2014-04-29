
package com.cn.elysee.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public class AppException implements UncaughtExceptionHandler
{

	public static final String TAG = "CrashHandler";
	private static AppException instance;
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private AppException(Context context)
	{
		init(context);
	}

	public static AppException getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new AppException(context);
		}
		return instance;
	}

	private void init(Context context)
	{
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			mDefaultHandler.uncaughtException(thread, ex);
		} else
		{
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼�
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false
	 */
	private boolean handleException(Throwable ex)
	{
		if (ex == null)
		{
			return true;
		}
		new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				new AlertDialog.Builder(mContext).setTitle("��ʾ")
						.setCancelable(false).setMessage("���������...")
						.setNeutralButton("��֪����", new OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								android.os.Process
										.killProcess(android.os.Process.myPid());
								System.exit(10);
							}
						}).create().show();
				Looper.loop();
			}
		}.start();
		return true;
	}
}
