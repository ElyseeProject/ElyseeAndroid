
package com.cn.elysee.exception;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public class DBFieldException extends Exception
{

	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 5098312819917252668L;
	
	public DBFieldException()
	{
		super();
	}
	
	public DBFieldException(String detailMessage)
	{
		super(detailMessage);
	}

}
