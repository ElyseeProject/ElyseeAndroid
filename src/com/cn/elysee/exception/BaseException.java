
package com.cn.elysee.exception;


/**
 * @author hzx
 * 2014Äê4ÔÂ21ÈÕ
 * @version V1.0
 */
public class BaseException extends Exception
{

	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 1L;
	
	public BaseException()
	{
		super();
	}

	public BaseException(String detailMessage)
	{
		super(detailMessage);
	}
}
