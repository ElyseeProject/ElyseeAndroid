package com.cn.elysee.util.cache;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
public class CallBackHandler<T>
{
	/**
	 * �������п�ʼ
	 * 
	 * @param t
	 *            ��Ӧ�Ķ���
	 * @param data
	 *            ����Ψһ��ʶ
	 */
	public void onStart(T t, Object data)
	{
	}

	/**
	 * �������п�ʼ
	 * 
	 * @param t
	 *            ��Ӧ�Ķ���
	 * @param data
	 *            ����Ψһ��ʶ
	 * @param inputStream
	 *            ��ʶ��Ӧ����Ӧ����
	 */
	public void onSuccess(T t, Object data, byte[] buffer)
	{
	}

	/**
	 * ��������ʧ��
	 * 
	 * @param t
	 *            ��Ӧ�Ķ���
	 * @param data
	 *            ����Ψһ��ʶ
	 */
	public void onFailure(T t, Object data)
	{

	}
}
