package com.cn.elysee.util.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.cn.elysee.exception.FileAlreadyExistException;
import com.cn.elysee.util.Logger;

import android.os.Message;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class FileHttpResponseHandler extends AsyncHttpResponseHandler
{
	public final static int TIME_OUT = 30000;
	private final static int BUFFER_SIZE = 1024 * 8;

	private static final String TAG = "FileHttpResponseHandler";
	private static final String TEMP_SUFFIX = ".download";
	private File file;
	private File tempFile;
	private File baseDirFile;
	private RandomAccessFile outputStream;
	private long downloadSize;
	private long previousFileSize;
	private long totalSize;
	private long networkSpeed;
	private long previousTime;
	private long totalTime;
	private boolean interrupt = false;
	private boolean timerInterrupt = false;
	private String url;
	private Timer timer = new Timer();
	private static final int TIMERSLEEPTIME = 100;

	public FileHttpResponseHandler(String url, String rootFile, String fileName)
	{

		super();
		this.url = url;
		this.baseDirFile = new File(rootFile);
		this.file = new File(rootFile, fileName);
		this.tempFile = new File(rootFile, fileName + TEMP_SUFFIX);
		init();
	}

	public FileHttpResponseHandler(String rootFile, String fileName)
	{
		super();
		this.baseDirFile = new File(rootFile);
		this.file = new File(rootFile, fileName);
		this.tempFile = new File(rootFile, fileName + TEMP_SUFFIX);
		init();
	}

	public FileHttpResponseHandler(String filePath)
	{
		super();
		this.file = new File(filePath);
		this.baseDirFile = new File(this.file.getParent());
		this.tempFile = new File(filePath + TEMP_SUFFIX);
		init();
	}

	private void init()
	{
		if (!this.baseDirFile.exists())
		{
			this.baseDirFile.mkdirs();
		}
	}

	private void startTimer()
	{
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				while (!timerInterrupt)
				{
					sendProgressMessage(totalSize, getDownloadSize(),
							networkSpeed);
					try
					{
						Thread.sleep(TIMERSLEEPTIME);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

			}
		}, 0, 1000);
	}

	private void stopTimer()
	{
		timerInterrupt = true;
	}

	public File getFile()
	{
		return file;
	}

	public String getUrl()
	{
		return url;
	}

	private class ProgressReportingRandomAccessFile extends RandomAccessFile
	{
		private int progress = 0;

		public ProgressReportingRandomAccessFile(File file, String mode)
				throws FileNotFoundException
		{
			super(file, mode);
		}

		@Override
		public void write(byte[] buffer, int offset, int count)
				throws IOException
		{

			super.write(buffer, offset, count);
			progress += count;
			totalTime = System.currentTimeMillis() - previousTime;
			downloadSize = progress + previousFileSize;
			if (totalTime > 0)
			{
				networkSpeed = (long) ((progress / totalTime) / 1.024);
			}

		}
	}

	public boolean isInterrupt()
	{

		return interrupt;
	}

	public void setInterrupt(boolean interrupt)
	{
		this.interrupt = interrupt;
	}

	public long getDownloadSize()
	{

		return downloadSize;
	}

	public long getTotalSize()
	{

		return totalSize;
	}

	public double getDownloadSpeed()
	{

		return this.networkSpeed;
	}

	public void setPreviousFileSize(long previousFileSize)
	{
		this.previousFileSize = previousFileSize;
	}

	public long getTotalTime()
	{

		return this.totalTime;
	}

	public void onSuccess(byte[] binaryData)
	{
		onSuccess(new String(binaryData));
	}

	public void onSuccess(int statusCode, byte[] binaryData)
	{
		onSuccess(binaryData);
	}

	public void onFailure(Throwable error, byte[] binaryData)
	{
		onFailure(error);
	}

	protected void sendSuccessMessage(int statusCode, byte[] responseBody)
	{
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]
		{ statusCode, responseBody }));
	}

	@Override
	protected void sendFailureMessage(Throwable e, byte[] responseBody)
	{
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]
		{ e, responseBody }));
	}

	protected void sendProgressMessage(long totalSize, long currentSize,
			long speed)
	{
		sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]
		{ totalSize, currentSize, speed }));
	}

	protected void handleSuccessMessage(int statusCode, byte[] responseBody)
	{
		onSuccess(statusCode, responseBody);
	}

	protected void handleFailureMessage(Throwable e, byte[] responseBody)
	{
		onFailure(e, responseBody);
	}

	@Override
	protected void handleMessage(Message msg)
	{
		Object[] response;
		switch (msg.what)
		{
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(byte[]) response[1]);
			break;
		default:
			super.handleMessage(msg);
			break;
		}
	}

	@Override
	protected void sendResponseMessage(HttpResponse response)
	{

		Throwable error = null;
		byte[] responseBody = null;
		long result = -1;
		int statusCode = 0;
		try
		{
			statusCode = response.getStatusLine().getStatusCode();
			long contentLenght = response.getEntity().getContentLength();
			// -1�Ľ����ʽContentLength ���ֻ����ʵ�ʱ����������⣬����Ϊ-1
			if (contentLenght == -1)
			{
				contentLenght = response.getEntity().getContent().available();
			}
			totalSize = contentLenght + previousFileSize;

			Logger.v(TAG, "totalSize: " + totalSize);

			if (file.exists() && totalSize == file.length())
			{
				Logger.v(TAG,
						"Output file already exists. Skipping download.");
				throw new FileAlreadyExistException(
						"Output file already exists. Skipping download.");
			}
			else if (tempFile.exists())
			{
				Logger.v(TAG, "yahooo: "
						+ response.getEntity().getContentLength());
				previousFileSize = tempFile.length();

				Logger.v(TAG, "File is not complete, download now.");
				Logger.v(TAG, "File length:" + tempFile.length()
						+ " totalSize:" + totalSize);

			}
			outputStream = new ProgressReportingRandomAccessFile(tempFile, "rw");
			InputStream input = response.getEntity().getContent();
			startTimer();
			int bytesCopied = copy(input, outputStream);
			if ((previousFileSize + bytesCopied) != totalSize
					&& totalSize != -1 && !interrupt) { throw new IOException(
					"Download incomplete: " + bytesCopied + " != " + totalSize); }
			Logger.v(TAG, "Download completed successfully.");
			result = bytesCopied;
		}
		catch (FileNotFoundException e)
		{
			sendFailureMessage(e, responseBody);
			error = e;
		}
		catch (FileAlreadyExistException e)
		{
			error = e;
		}
		catch (IllegalStateException e)
		{
			error = e;
		}
		catch (IOException e)
		{
			error = e;
		}
		// ֹͣ��ӡ
		stopTimer();
		// ��֤timer���ر�
		try
		{
			Thread.sleep(TIMERSLEEPTIME);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result == -1 || interrupt || error != null)
		{
			if (error != null)
			{
				Logger.v(TAG, "Download failed." + error.getMessage());
				if (error instanceof FileAlreadyExistException)
				{
					sendSuccessMessage(statusCode, "���سɹ���".getBytes());

				}
				else
				{
					sendFailureMessage(error, responseBody);
				}
			}
			return;
		}
		tempFile.renameTo(file);
		sendSuccessMessage(statusCode, "���سɹ���".getBytes());
	}

	public int copy(InputStream input, RandomAccessFile out) throws IOException
	{

		if (input == null || out == null) { return -1; }

		byte[] buffer = new byte[BUFFER_SIZE];

		BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
		Logger.v(TAG, "length" + out.length());
		int count = 0, n = 0;
		long errorBlockTimePreviousTime = -1, expireTime = 0;
		try
		{
			out.seek(out.length());
			previousTime = System.currentTimeMillis();
			while (!interrupt)
			{
				n = in.read(buffer, 0, BUFFER_SIZE);
				if (n == -1)
				{
					break;
				}
				out.write(buffer, 0, n);
				count += n;
				if (networkSpeed == 0)
				{
					if (errorBlockTimePreviousTime > 0)
					{
						expireTime = System.currentTimeMillis()
								- errorBlockTimePreviousTime;
						if (expireTime > TIME_OUT) { throw new ConnectTimeoutException(
								"connection time out."); }
					}
					else
					{
						errorBlockTimePreviousTime = System.currentTimeMillis();
					}
				}
				else
				{
					expireTime = 0;
					errorBlockTimePreviousTime = -1;
				}
			}
		}
		finally
		{

			try
			{
				out.close();
			}
			catch (IOException e)
			{
			}
		}
		return count;

	}

	public File getTempFile()
	{
		return tempFile;
	}
}
