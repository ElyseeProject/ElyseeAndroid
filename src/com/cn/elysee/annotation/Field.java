package com.cn.elysee.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ��������
 * �����õ�ʱ��Ĭ�������id��_id�ֶ���Ϊ������column�����õ���Ĭ��Ϊ�ֶ���������������������Զ�����ID
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Field
{
	/**
	 * �������õ�����
	 * 2014��4��21��
	 * @return
	 */
	public String name() default "";
	
	/**
	 * �������õ�Ĭ��ֵ
	 * 2014��4��21��
	 * @return
	 */
	public String defaultValue() default "";
}
