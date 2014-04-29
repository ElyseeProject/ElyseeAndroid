
package com.cn.elysee.mvc.common;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
public interface IResponseListener
{
	/**
	 * ��ʼ��������
	 * 2013��12��12��
	 */
	void onStart();

	/**
	 * ���ݼ��سɹ�
	 * 2013��12��12��
	 * @param response
	 */
	void onSuccess(Response response);

	/**
	 * ���ڼ�����
	 * 2013��12��12��
	 * @param response
	 */
	void onRuning(Response response);

	/**
	 * ���ݼ���ʧ��
	 * 2013��12��12��
	 * @param response
	 */
	void onFailure(Response response);

	/**
	 * �����������
	 * 2013��12��12��
	 */
	void onFinish();
}
