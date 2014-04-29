package com.cn.elysee.util;

import java.lang.reflect.Field;

import com.cn.elysee.annotation.Inject;
import com.cn.elysee.annotation.InjectResource;
import com.cn.elysee.annotation.InjectView;

import android.app.Activity;
import android.content.res.Resources;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class Injector
{
	private static Injector instance;

	private Injector()
	{

	}

	public static Injector getInstance()
	{
		if (instance == null)
		{
			instance = new Injector();
		}
		return instance;
	}

	public void inJectAll(Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(InjectView.class))
				{
					injectView(activity, field);
				}
				else if (field.isAnnotationPresent(InjectResource.class))
				{
					injectResource(activity, field);
				}
				else if (field.isAnnotationPresent(Inject.class))
				{
					inject(activity, field);
				}
			}
		}
	}

	private void inject(Activity activity, Field field)
	{
		try
		{
			field.setAccessible(true);
			field.set(activity, field.getType().newInstance());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void injectView(Activity activity, Field field)
	{
		if (field.isAnnotationPresent(InjectView.class))
		{
			InjectView viewInject = field.getAnnotation(InjectView.class);
			int viewId = viewInject.id();
			try
			{
				field.setAccessible(true);
				field.set(activity, activity.findViewById(viewId));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void injectResource(Activity activity, Field field)
	{
		if (field.isAnnotationPresent(InjectResource.class))
		{
			InjectResource resourceJect = field
					.getAnnotation(InjectResource.class);
			int resourceID = resourceJect.id();
			try
			{
				field.setAccessible(true);
				Resources resources = activity.getResources();
				String type = resources.getResourceTypeName(resourceID);
				if (type.equalsIgnoreCase("string"))
				{
					field.set(activity,
							activity.getResources().getString(resourceID));
				}
				else if (type.equalsIgnoreCase("drawable"))
				{
					field.set(activity,
							activity.getResources().getDrawable(resourceID));
				}
				else if (type.equalsIgnoreCase("layout"))
				{
					field.set(activity,
							activity.getResources().getLayout(resourceID));
				}
				else if (type.equalsIgnoreCase("array"))
				{
					if (field.getType().equals(int[].class))
					{
						field.set(activity, activity.getResources()
								.getIntArray(resourceID));
					}
					else if (field.getType().equals(String[].class))
					{
						field.set(activity, activity.getResources()
								.getStringArray(resourceID));
					}
					else
					{
						field.set(activity, activity.getResources()
								.getStringArray(resourceID));
					}

				}
				else if (type.equalsIgnoreCase("color"))
				{
					if (field.getType().equals(Integer.TYPE))
					{
						field.set(activity,
								activity.getResources().getColor(resourceID));
					}
					else
					{
						field.set(activity, activity.getResources()
								.getColorStateList(resourceID));
					}

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void inject(Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(Inject.class))
				{
					inject(activity, field);
				}
			}
		}
	}

	public void injectView(Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(InjectView.class))
				{
					injectView(activity, field);
				}
			}
		}
	}

	public void injectResource(Activity activity)
	{
		Field[] fields = activity.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(InjectResource.class))
				{
					injectResource(activity, field);
				}
			}
		}
	}

}
