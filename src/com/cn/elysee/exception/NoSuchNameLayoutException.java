
package com.cn.elysee.exception;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public class NoSuchNameLayoutException extends Exception
{

	/**
	 * @author hzx
	 */
	private static final long serialVersionUID = -426716386468423444L;
	
	public NoSuchNameLayoutException()
	{
		super();
	}
	
	public NoSuchNameLayoutException(String detailMessage)
	{
		super(detailMessage);
	}

}
