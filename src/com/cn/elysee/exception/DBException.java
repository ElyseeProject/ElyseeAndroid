
package com.cn.elysee.exception;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public class DBException extends Exception
{		
	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 4392812818254502082L;

	public DBException()
	{
		super();
	}
	
	public DBException(String detailMessage)
	{
		super(detailMessage);
	}
}
