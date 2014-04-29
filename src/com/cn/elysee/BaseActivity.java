package com.cn.elysee;

import com.cn.elysee.mvc.command.IdentityCommand;
import com.cn.elysee.mvc.common.IResponseListener;
import com.cn.elysee.mvc.common.Request;
import com.cn.elysee.mvc.common.Response;
import com.cn.elysee.util.Logger;
import com.cn.elysee.util.netstate.NetWorkUtil.netType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
@SuppressLint("DefaultLocale")
public class BaseActivity extends Activity
{
	/** ģ������� */
	private String moduleName = "";
	/** �����ļ������� */
	private String layouName = "";
	private static final int DIALOG_ID_PROGRESS_DEFAULT = 0x174980;
	private static final String TAIDENTITYCOMMAND = "taidentitycommand";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		notifiyApplicationActivityCreating();
		onPreOnCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		getBaseApplication().getAppManager().addActivity(this);
		initActivity();
		onAfterOnCreate(savedInstanceState);
		notifiyApplicationActivityCreated();
	}

	public BaseApplication getBaseApplication()
	{
		return (BaseApplication) getApplication();
	}

	private void notifiyApplicationActivityCreating()
	{
		getBaseApplication().onActivityCreating(this);
	}

	private void notifiyApplicationActivityCreated()
	{
		getBaseApplication().onActivityCreated(this);
	}

	protected void onPreOnCreate(Bundle savedInstanceState)
	{
		getBaseApplication().registerCommand(TAIDENTITYCOMMAND,
				IdentityCommand.class);
	}

	private void initActivity()
	{
		// ��ʼ��ģ����
		getModuleName();
		// ��ʼ��������
		getLayouName();
		// ������ע����
		initInjector();
		// �Զ�����Ĭ�ϲ���
		loadDefautLayout();
	}

	protected void onAfterOnCreate(Bundle savedInstanceState)
	{
	}

	/**
	 * ��ʼ��ע����
	 */
	private void initInjector()
	{
		getBaseApplication().getInjector().injectResource(this);
		getBaseApplication().getInjector().inject(this);
	}

	/**
	 * �Զ�����Ĭ�ϲ���
	 */
	private void loadDefautLayout()
	{
		try
		{
			int layoutResID = getBaseApplication().getLayoutLoader()
					.getLayoutID(layouName);
			setContentView(layoutResID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		// ����view��������ͼ����֮�����ע��
		getBaseApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	@Override
	public void setContentView(View view, LayoutParams params)
	{
		super.setContentView(view, params);
		// ����view��������ͼ����֮�����ע��
		getBaseApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		// ����view��������ͼ����֮�����ע��
		getBaseApplication().getInjector().injectView(this);
		onAfterSetContentView();
	}

	protected void onAfterSetContentView()
	{

	}

	/**
	 * ��ȡģ�������
	 */
	public String getModuleName()
	{
		String moduleName = this.moduleName;
		if (moduleName == null || moduleName.equalsIgnoreCase(""))
		{
			moduleName = getClass().getName().substring(0,
					getClass().getName().length() - 8);
			String arrays[] = moduleName.split("\\.");
			this.moduleName = moduleName = "activity_"
					+ arrays[arrays.length - 1].toLowerCase();
		}
		return moduleName;
	}

	/**
	 * ����ģ�������
	 */
	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	/**
	 * ��ȡ�����ļ���
	 * 
	 * @return�����ļ���
	 */
	public String getLayouName()
	{
		String layouName = this.layouName;
		if (layouName == null || layouName.equalsIgnoreCase(""))
		{
			this.layouName = this.moduleName;
		}
		return layouName;
	}

	/**
	 * ���ò����ļ���
	 */
	protected void setLayouName(String layouName)
	{
		this.layouName = layouName;
	}

	public void preProcessData(Response response)
	{

	}

	public void processData(Response response)
	{

	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case DIALOG_ID_PROGRESS_DEFAULT:
			ProgressDialog dlg = new ProgressDialog(this);
			dlg.setMessage("���ڼ���...");
			dlg.setCancelable(true);
			return dlg;
		default:
			return super.onCreateDialog(id);

		}
	}

	public final void doCommand(int resId, Request request)
	{
		String commandKey = getString(resId);
		doCommand(commandKey, request, null, true);
	}

	public final void doCommand(String commandKey, Request request)
	{
		doCommand(commandKey, request, null, true);
	}

	public final void doCommand(int resId, Request request,
			IResponseListener listener)
	{
		String commandKey = getString(resId);
		doCommand(commandKey, request, listener, true);
	}

	public final void doCommand(String commandKey, Request request,
			IResponseListener listener)
	{
		doCommand(commandKey, request, listener, true);
	}

	public final void doCommand(int resId, Request request,
			IResponseListener listener, boolean showProgress)
	{
		String commandKey = getString(resId);
		Logger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, true);
	}

	public final void doCommand(String commandKey, Request request,
			IResponseListener listener, boolean showProgress)
	{
		Logger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, true);
	}

	public final void doCommand(int resId, Request request,
			IResponseListener listener, boolean showProgress, boolean record)
	{
		String commandKey = getString(resId);
		Logger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, record, false);
	}

	public final void doCommand(String commandKey, Request request,
			IResponseListener listener, boolean showProgress, boolean record)
	{
		Logger.i(BaseActivity.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ", request: " + request);
		doCommand(commandKey, request, listener, showProgress, record, false);
	}

	public final void doCommand(int resId, Request request,
			IResponseListener listener, boolean showProgress, boolean record,
			boolean resetStack)
	{
		String commandKey = getString(resId);
		doCommand(commandKey, request, listener, showProgress, record,
				resetStack);
	}

	public final void doCommand(String commandKey, Request request,
			IResponseListener listener, boolean showProgress, boolean record,
			boolean resetStack)
	{
		if (showProgress)
		{
			showProgress();
		}
		getBaseApplication().doCommand(commandKey, request, listener, record,
				resetStack);
	}

	/**
	 * ����
	 */
	public final void back()
	{
		getBaseApplication().back();
	}

	/**
	 * ��Ҫ�Զ��������������д
	 */
	protected void showProgress()
	{
		showDialog(DIALOG_ID_PROGRESS_DEFAULT);
	}

	/**
	 * ���ؽ���������Ҫ��д������д
	 */
	protected void hideProgress()
	{
		try
		{
			removeDialog(DIALOG_ID_PROGRESS_DEFAULT);
		}
		catch (IllegalArgumentException iae)
		{
		}
	}

	/**
	 * ������������ʱ����
	 */
	public void onConnect(netType type)
	{

	}

	/**
	 * ��ǰû����������
	 */
	public void onDisConnect()
	{

	}

	@Override
	public void finish()
	{
		getBaseApplication().getAppManager().removeActivity(this);
		super.finish();
	}

	/**
	 * �˳�Ӧ�ó���
	 * 
	 * @param isBackground
	 *            �Ƿ񿪿�����̨����,���Ϊtrue��Ϊ��̨����
	 */
	public void exitApp(Boolean isBackground)
	{
		getBaseApplication().exitApp(isBackground);
	}

	/**
	 * �˳�Ӧ�ó���
	 * 
	 */
	public void exitApp()
	{
		getBaseApplication().exitApp(false);
	}

	/**
	 * �˳�Ӧ�ó������ں�̨����
	 * 
	 */
	public void exitAppToBackground()
	{
		getBaseApplication().exitApp(true);
	}

	/**
	 * ����activity
	 * 
	 * @param activityResID
	 */
	public final void doActivity(int activityResID)
	{
		doActivity(activityResID, null);
	}

	public final void doActivity(int activityResID, Bundle bundle)
	{
		Request request = new Request();
		request.setData(bundle);
		request.setActivityKeyResID(activityResID);
		// ����activity
		doCommand(TAIDENTITYCOMMAND, request);
	}

	@Override
	public void onBackPressed()
	{
		back();
	}
}
