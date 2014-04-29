package com.cn.elysee.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * @author hzx 
 * 2014��4��18��
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column
{
	/**
	 * �����ֶ���
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * �ֶ�Ĭ��ֵ
	 * 
	 * @return
	 */
	public String defaultValue() default "";
}
