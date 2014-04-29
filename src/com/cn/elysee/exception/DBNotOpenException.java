
package com.cn.elysee.exception;


/**
 * @author hzx
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class DBNotOpenException extends Exception
{

	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 3496656035004866608L;
	
	public DBNotOpenException()
	{
		super();
	}
	
	public DBNotOpenException(String detailMessage)
	{
		super(detailMessage);
	}

}
