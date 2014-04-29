package com.cn.elysee;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.cn.elysee.db.SQLiteDatabasePool;
import com.cn.elysee.exception.AppException;
import com.cn.elysee.exception.NoSuchCommandException;
import com.cn.elysee.mvc.command.CommandExecutor;
import com.cn.elysee.mvc.command.ICommand;
import com.cn.elysee.mvc.command.IdentityCommand;
import com.cn.elysee.mvc.common.IResponseListener;
import com.cn.elysee.mvc.common.Request;
import com.cn.elysee.mvc.common.Response;
import com.cn.elysee.mvc.controller.ActivityStackInfo;
import com.cn.elysee.mvc.controller.NavigationDirection;
import com.cn.elysee.util.Injector;
import com.cn.elysee.util.Logger;
import com.cn.elysee.util.cache.FileCache;
import com.cn.elysee.util.cache.FileCache.CacheParams;
import com.cn.elysee.util.config.IConfig;
import com.cn.elysee.util.config.PreferenceConfig;
import com.cn.elysee.util.config.PropertiesConfig;
import com.cn.elysee.util.layoutloader.ILayoutLoader;
import com.cn.elysee.util.layoutloader.LayoutLoader;
import com.cn.elysee.util.netstate.NetChangeObserver;
import com.cn.elysee.util.netstate.NetWorkUtil.netType;
import com.cn.elysee.util.netstate.NetworkStateReceiver;

/**
 * @author hzx 2014年4月24日
 * @version V1.0
 */
public class BaseApplication extends Application implements IResponseListener
{

	/** 配置器 为Preference */
	public final static int PREFERENCECONFIG = 0;
	/** 配置器 为PROPERTIESCONFIG */
	public final static int PROPERTIESCONFIG = 1;

	private final static String TAIDENTITYCOMMAND = "IdentityCommand";
	/** 配置器 */
	private IConfig mCurrentConfig;
	/** 获取布局文件ID加载器 */
	private ILayoutLoader mLayoutLoader;
	/** 加载类注入器 */
	private Injector mInjector;
	/** App异常崩溃处理器 */
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	private static BaseApplication application;
	private CommandExecutor mCommandExecutor;
	private BaseActivity currentActivity;
	private final HashMap<String, Class<? extends BaseActivity>> registeredActivities = new HashMap<String, Class<? extends BaseActivity>>();
	private Stack<ActivityStackInfo> activityStack = new Stack<ActivityStackInfo>();
	private NavigationDirection currentNavigationDirection;
	/** ElyseeAndroid 文件缓存 */
	private FileCache mFileCache;
	/** ElyseeAndroid数据库链接池 */
	private SQLiteDatabasePool mSQLiteDatabasePool;
	/** ElyseeAndroid 应用程序运行Activity管理器 */
	private AppManager mAppManager;
	private Boolean networkAvailable = false;
	private static final String SYSTEMCACHE = "elyseeandroid";
	private NetChangeObserver taNetChangeObserver;

	@Override
	public void onCreate()
	{
		onPreCreateApplication();
		super.onCreate();
		doOncreate();
		onAfterCreateApplication();
		getAppManager();
	}

	private void doOncreate()
	{
		BaseApplication.application = this;
		// 注册App异常崩溃处理器
		Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
		mCommandExecutor = CommandExecutor.getInstance();
		taNetChangeObserver = new NetChangeObserver()
		{
			@Override
			public void onConnect(netType type)
			{
				super.onConnect(type);
				BaseApplication.this.onConnect(type);
			}

			@Override
			public void onDisConnect()
			{
				super.onDisConnect();
				BaseApplication.this.onDisConnect();

			}
		};
		NetworkStateReceiver.registerObserver(taNetChangeObserver);
		// 注册activity启动控制控制器
		registerCommand(TAIDENTITYCOMMAND, IdentityCommand.class);
	}

	/**
	 * 当前没有网络连接
	 */
	public void onDisConnect()
	{
		networkAvailable = false;
		if (currentActivity != null)
		{
			currentActivity.onDisConnect();
		}
	}

	/**
	 * 网络连接连接时调用
	 */
	protected void onConnect(netType type)
	{
		networkAvailable = true;
		if (currentActivity != null)
		{
			currentActivity.onConnect(type);
		}
	}

	/**
	 * 获取Application
	 * 
	 * @return
	 */
	public static BaseApplication getApplication()
	{
		return application;
	}

	protected void onAfterCreateApplication()
	{

	}

	protected void onPreCreateApplication()
	{

	}

	public IConfig getPreferenceConfig()
	{
		return getConfig(PREFERENCECONFIG);
	}

	public IConfig getPropertiesConfig()
	{
		return getConfig(PROPERTIESCONFIG);
	}

	public IConfig getConfig(int confingType)
	{
		if (confingType == PREFERENCECONFIG)
		{
			mCurrentConfig = PreferenceConfig.getPreferenceConfig(this);

		}
		else if (confingType == PROPERTIESCONFIG)
		{
			mCurrentConfig = PropertiesConfig.getPropertiesConfig(this);
		}
		else
		{
			mCurrentConfig = PropertiesConfig.getPropertiesConfig(this);
		}
		if (!mCurrentConfig.isLoadConfig())
		{
			mCurrentConfig.loadConfig();
		}
		return mCurrentConfig;
	}

	public IConfig getCurrentConfig()
	{
		if (mCurrentConfig == null)
		{
			getPreferenceConfig();
		}
		return mCurrentConfig;
	}

	public ILayoutLoader getLayoutLoader()
	{
		if (mLayoutLoader == null)
		{
			mLayoutLoader = LayoutLoader.getInstance(this);
		}
		return mLayoutLoader;
	}

	public void setLayoutLoader(ILayoutLoader layoutLoader)
	{
		this.mLayoutLoader = layoutLoader;
	}

	/**
	 * 设置 App异常崩溃处理器
	 * 
	 * @param uncaughtExceptionHandler
	 */
	public void setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler)
	{
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	private UncaughtExceptionHandler getUncaughtExceptionHandler()
	{
		if (uncaughtExceptionHandler == null)
		{
			uncaughtExceptionHandler = AppException.getInstance(this);
		}
		return uncaughtExceptionHandler;
	}

	public Injector getInjector()
	{
		if (mInjector == null)
		{
			mInjector = Injector.getInstance();
		}
		return mInjector;
	}

	public void setInjector(Injector injector)
	{
		this.mInjector = injector;
	}

	public void onActivityCreating(BaseActivity activity)
	{
		if (activityStack.size() > 0)
		{
			ActivityStackInfo info = activityStack.peek();
			if (info != null)
			{
				Response response = info.getResponse();
				activity.preProcessData(response);
			}
		}
	}

	public void onActivityCreated(BaseActivity activity)
	{
		if (currentActivity != null)
		{
			currentActivity.finish();
		}
		currentActivity = activity;

		int size = activityStack.size();

		if (size > 0)
		{
			ActivityStackInfo info = activityStack.peek();
			if (info != null)
			{
				Response response = info.getResponse();
				activity.processData(response);

				if (size >= 2 && !info.isRecord())
				{
					activityStack.pop();
				}
			}
		}
	}

	public void doCommand(String commandKey, Request request,
			IResponseListener listener, boolean record, boolean resetStack)
	{
		if (listener != null)
		{
			try
			{
				CommandExecutor.getInstance().enqueueCommand(commandKey,
						request, listener);

			}
			catch (NoSuchCommandException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Logger.i(BaseApplication.this, "go with cmdid=" + commandKey
					+ ", record: " + record + ",rs: " + resetStack
					+ ", request: " + request);
			if (resetStack)
			{
				activityStack.clear();
			}

			currentNavigationDirection = NavigationDirection.Forward;

			ActivityStackInfo info = new ActivityStackInfo(commandKey, request,
					record, resetStack);
			activityStack.add(info);

			Object[] newTag =
			{ request.getTag(), listener };
			request.setTag(newTag);

			Logger.i(BaseApplication.this, "Enqueue-ing command");
			try
			{
				CommandExecutor.getInstance().enqueueCommand(commandKey,
						request, this);
			}
			catch (NoSuchCommandException e)
			{
				e.printStackTrace();
			}
			Logger.i(BaseApplication.this, "Enqueued command");

		}

	}

	public void back()
	{
		Logger.i(BaseApplication.this,
				"ActivityStack Size: " + activityStack.size());
		if (activityStack != null && activityStack.size() != 0)
		{
			if (activityStack.size() >= 2)
			{
				activityStack.pop();
			}

			currentNavigationDirection = NavigationDirection.Backward;
			ActivityStackInfo info = activityStack.peek();
			try
			{
				CommandExecutor.getInstance().enqueueCommand(
						info.getCommandKey(), info.getRequest(), this);
			}
			catch (NoSuchCommandException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void processResponse(Message msg)
	{
		Response response = (Response) msg.obj;
		ActivityStackInfo top = activityStack.peek();
		top.setResponse(response);
		if (response != null)
		{
			int targetActivityKeyResID = response.getActivityKeyResID();
			String targetActivityKey = "";
			if (targetActivityKeyResID != 0)
			{
				targetActivityKey = getString(targetActivityKeyResID);
			}
			if (targetActivityKey != null
					&& targetActivityKey.equalsIgnoreCase(""))
			{
				targetActivityKey = response.getActivityKey();
			}
			Object[] newTag = (Object[]) response.getTag();
			Object tag = newTag[0];
			response.setTag(tag);
			Class<? extends BaseActivity> cls = registeredActivities
					.get(targetActivityKey);
			Logger.i(BaseApplication.this,
					"Launching new activity // else, current Direction: "
							+ currentNavigationDirection);

			int asize = activityStack.size();
			Logger.i(BaseApplication.this,
					"Current Stack Size (before processing): " + asize);

			switch (currentNavigationDirection)
			{
			case Forward:
				if (asize >= 2)
				{
					if (!top.isRecord())
					{
						activityStack.pop();
					}
				}
				break;
			case Backward:
				currentNavigationDirection = NavigationDirection.Forward;
				break;
			}
			Logger.i(
					BaseApplication.this,
					"Current Stack Size (after processing): "
							+ activityStack.size());

			if (cls != null)
			{
				Intent launcherIntent = new Intent(currentActivity, cls);
				currentActivity.startActivity(launcherIntent);
				currentActivity.finish();
				top.setActivityClass(cls);
			}

		}

	}

	public void registerActivity(int resID, Class<? extends BaseActivity> clz)
	{
		String activityKey = getString(resID);
		registeredActivities.put(activityKey, clz);
	}

	public void registerActivity(String activityKey,
			Class<? extends BaseActivity> clz)
	{
		registeredActivities.put(activityKey, clz);
	}

	public void unregisterActivity(int resID)
	{
		String activityKey = getString(resID);
		unregisterActivity(activityKey);
	}

	public void unregisterActivity(String activityKey)
	{
		registeredActivities.remove(activityKey);
	}

	public void registerCommand(int resID, Class<? extends ICommand> command)
	{

		String commandKey = getString(resID);
		registerCommand(commandKey, command);

	}

	public void registerCommand(String commandKey,
			Class<? extends ICommand> command)
	{
		if (command != null)
		{
			mCommandExecutor.registerCommand(commandKey, command);
		}
	}

	public void unregisterCommand(int resID)
	{
		String commandKey = getString(resID);
		unregisterCommand(commandKey);
	}

	public void unregisterCommand(String commandKey)
	{

		mCommandExecutor.unregisterCommand(commandKey);
	}

	public FileCache getFileCache()
	{
		if (mFileCache == null)
		{
			CacheParams cacheParams = new CacheParams(this, SYSTEMCACHE);
			FileCache fileCache = new FileCache(cacheParams);
			application.setFileCache(fileCache);

		}
		return mFileCache;
	}

	public void setFileCache(FileCache fileCache)
	{
		this.mFileCache = fileCache;
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			processResponse(msg);
		}
	};

	private void handleResponse(Response response)
	{
		Message msg = new Message();
		msg.what = 0;
		msg.obj = response;
		handler.sendMessage(msg);
	}

	@Override
	public void onStart()
	{

	}

	@Override
	public void onSuccess(Response response)
	{
		handleResponse(response);
	}

	@Override
	public void onRuning(Response response)
	{

	}

	@Override
	public void onFailure(Response response)
	{
		handleResponse(response);
	}

	public SQLiteDatabasePool getSQLiteDatabasePool()
	{
		if (mSQLiteDatabasePool == null)
		{
			mSQLiteDatabasePool = SQLiteDatabasePool.getInstance(this);
			mSQLiteDatabasePool.createPool();
		}
		return mSQLiteDatabasePool;
	}

	public void setSQLiteDatabasePool(SQLiteDatabasePool sqliteDatabasePool)
	{
		this.mSQLiteDatabasePool = sqliteDatabasePool;
	}

	public AppManager getAppManager()
	{
		if (mAppManager == null)
		{
			mAppManager = AppManager.getAppManager();
		}
		return mAppManager;
	}

	/**
	 * 退出应用程序
	 * 
	 * @param isBackground
	 *            是否开开启后台运行,如果为true则为后台运行
	 */
	public void exitApp(Boolean isBackground)
	{
		mAppManager.AppExit(this, isBackground);
	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public Boolean isNetworkAvailable()
	{
		return networkAvailable;
	}

	@Override
	public void onFinish()
	{

	}

}
