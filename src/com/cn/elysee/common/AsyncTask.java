package com.cn.elysee.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.annotation.TargetApi;

/**
 * @author hzx 2014年4月21日
 * @version V1.0
 */
public abstract class AsyncTask<Params, Progress, Result>
{
	private static final String LOG_TAG = "AsyncTask";

	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 128;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory()
	{
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r)
		{
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
			10);

	/**
	 * 
	 */
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sPoolWorkQueue, sThreadFactory,
			new ThreadPoolExecutor.DiscardOldestPolicy());

	/**
	 * 
	 */
	public static final Executor SERIAL_EXECUTOR = AndroidVersionCheckUtils
			.hasHoneycomb() ? new SerialExecutor() : Executors
			.newSingleThreadExecutor(sThreadFactory);

	public static final Executor DUAL_THREAD_EXECUTOR = Executors
			.newFixedThreadPool(2, sThreadFactory);

	private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_PROGRESS = 0x2;

	private static final InternalHandler sHandler = new InternalHandler();

	private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
	private final WorkerRunnable<Params, Result> mWorker;
	private final FutureTask<Result> mFuture;

	private volatile Status mStatus = Status.PENDING;

	private final AtomicBoolean mCancelled = new AtomicBoolean();
	private final AtomicBoolean mTaskInvoked = new AtomicBoolean();

	@TargetApi(11)
	private static class SerialExecutor implements Executor
	{
		final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
		Runnable mActive;

		@Override
		public synchronized void execute(final Runnable r)
		{
			mTasks.offer(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						r.run();
					}
					finally
					{
						scheduleNext();
					}
				}
			});
			if (mActive == null)
			{
				scheduleNext();
			}
		}

		protected synchronized void scheduleNext()
		{
			if ((mActive = mTasks.poll()) != null)
			{
				THREAD_POOL_EXECUTOR.execute(mActive);
			}
		}
	}

	/**
	 * 
	 * @author hzx
	 * 
	 */
	public enum Status
	{
		PENDING,

		RUNNING,

		FINISHED,
	}

	/**
	 * 
	 */
	public static void init()
	{
		sHandler.getLooper();
	}

	/** @hide */
	public static void setDefaultExecutor(Executor exec)
	{
		sDefaultExecutor = exec;
	}

	/**
	 * 
	 */
	public AsyncTask()
	{
		mWorker = new WorkerRunnable<Params, Result>()
		{
			@Override
			public Result call() throws Exception
			{
				mTaskInvoked.set(true);

				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				return postResult(doInBackground(mParams));
			}
		};

		mFuture = new FutureTask<Result>(mWorker)
		{
			@Override
			protected void done()
			{
				try
				{
					postResultIfNotInvoked(get());
				}
				catch (InterruptedException e)
				{
					android.util.Log.w(LOG_TAG, e);
				}
				catch (ExecutionException e)
				{
					throw new RuntimeException(
							"An error occured while executing doInBackground()",
							e.getCause());
				}
				catch (CancellationException e)
				{
					postResultIfNotInvoked(null);
				}
			}
		};
	}

	private void postResultIfNotInvoked(Result result)
	{
		final boolean wasTaskInvoked = mTaskInvoked.get();
		if (!wasTaskInvoked)
		{
			postResult(result);
		}
	}

	private Result postResult(Result result)
	{
		@SuppressWarnings("unchecked")
		Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT,
				new AsyncTaskResult<Result>(this, result));
		message.sendToTarget();
		return result;
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @return
	 */
	public final Status getStatus()
	{
		return mStatus;
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param params
	 * @return
	 */
	protected abstract Result doInBackground(Params... params);

	/**
	 * 
	 * 2014年4月21日
	 */
	protected void onPreExecute()
	{
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param result
	 */
	protected void onPostExecute(Result result)
	{
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param values
	 */
	protected void onProgressUpdate(Progress... values)
	{
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param result
	 */
	protected void onCancelled(Result result)
	{
		onCancelled();
	}

	/**
	 * 
	 * 2014年4月21日
	 */
	protected void onCancelled()
	{
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @return
	 */
	public final boolean isCancelled()
	{
		return mCancelled.get();
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param mayInterruptIfRunning
	 * @return
	 */
	public final boolean cancel(boolean mayInterruptIfRunning)
	{
		mCancelled.set(true);
		return mFuture.cancel(mayInterruptIfRunning);
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public final Result get() throws InterruptedException, ExecutionException
	{
		return mFuture.get();
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public final Result get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException
	{
		return mFuture.get(timeout, unit);
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param params
	 * @return
	 */
	public final AsyncTask<Params, Progress, Result> execute(Params... params)
	{
		return executeOnExecutor(sDefaultExecutor, params);
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param exec
	 * @param params
	 * @return
	 */
	@SuppressWarnings("incomplete-switch")
	public final AsyncTask<Params, Progress, Result> executeOnExecutor(
			Executor exec, Params... params)
	{
		if (mStatus != Status.PENDING)
		{
			switch (mStatus)
			{
			case RUNNING:
				throw new IllegalStateException("Cannot execute task:"
						+ " the task is already running.");
			case FINISHED:
				throw new IllegalStateException("Cannot execute task:"
						+ " the task has already been executed "
						+ "(a task can be executed only once)");
			}
		}

		mStatus = Status.RUNNING;

		onPreExecute();

		mWorker.mParams = params;
		exec.execute(mFuture);

		return this;
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param runnable
	 */
	public static void execute(Runnable runnable)
	{
		sDefaultExecutor.execute(runnable);
	}

	/**
	 * 
	 * 2014年4月21日
	 * 
	 * @param values
	 */
	protected final void publishProgress(Progress... values)
	{
		if (!isCancelled())
		{
			sHandler.obtainMessage(MESSAGE_POST_PROGRESS,
					new AsyncTaskResult<Progress>(this, values)).sendToTarget();
		}
	}

	private void finish(Result result)
	{
		if (isCancelled())
		{
			onCancelled(result);
		}
		else
		{
			onPostExecute(result);
		}
		mStatus = Status.FINISHED;
	}

	private static class InternalHandler extends Handler
	{
		@SuppressWarnings(
		{ "unchecked", "rawtypes" })
		@Override
		public void handleMessage(Message msg)
		{
			AsyncTaskResult result = (AsyncTaskResult) msg.obj;
			switch (msg.what)
			{
			case MESSAGE_POST_RESULT:
				result.mTask.finish(result.mData[0]);
				break;
			case MESSAGE_POST_PROGRESS:
				result.mTask.onProgressUpdate(result.mData);
				break;
			}
		}
	}

	private static abstract class WorkerRunnable<Params, Result> implements
			Callable<Result>
	{
		Params[] mParams;
	}

	private static class AsyncTaskResult<Data>
	{
		@SuppressWarnings("rawtypes")
		final AsyncTask mTask;
		final Data[] mData;

		@SuppressWarnings("rawtypes")
		AsyncTaskResult(AsyncTask task, Data... data)
		{
			mTask = task;
			mData = data;
		}
	}
}
