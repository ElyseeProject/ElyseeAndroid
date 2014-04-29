
package com.cn.elysee.exception;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public class NoSuchCommandException extends Exception
{

	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = 6775989272729970537L;
	
	public NoSuchCommandException()
	{
		super();
	}
	
	public NoSuchCommandException(String detailMessage)
	{
		super(detailMessage);
	}

}
