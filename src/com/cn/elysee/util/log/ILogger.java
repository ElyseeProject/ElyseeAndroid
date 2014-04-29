package com.cn.elysee.util.log;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public interface ILogger
{
	void v(String tag, String message);

	void d(String tag, String message);

	void i(String tag, String message);

	void w(String tag, String message);

	void e(String tag, String message);

	void open();

	void close();

	void println(int priority, String tag, String message);
}
