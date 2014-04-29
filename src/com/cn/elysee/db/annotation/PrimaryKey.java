package com.cn.elysee.db.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author hzx 
 * 2014��4��18��
 * @Description �����õ�ʱ��Ĭ�������id��_id�ֶ���Ϊ������
 * column�����õ���Ĭ��Ϊ�ֶ���������������������Զ�����ID
 * @version V1.0
 */
@Target(
{ METHOD, FIELD })
@Retention(RUNTIME)
public @interface PrimaryKey
{
	/**
	 * ����������
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * �ֶ�Ĭ��ֵ
	 * 
	 * @return
	 */
	public String defaultValue() default "";

	/**
	 * �Ƿ��Զ�����
	 * 
	 * @return
	 */
	boolean autoIncrement() default false;
}
