
package com.cn.elysee.util.layoutloader;

import android.content.pm.PackageManager.NameNotFoundException;

import com.cn.elysee.exception.NoSuchNameLayoutException;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public interface ILayoutLoader
{
	public int getLayoutID(String resIDName) throws ClassNotFoundException,
	IllegalArgumentException, IllegalAccessException,
	NameNotFoundException, NoSuchNameLayoutException;
}
