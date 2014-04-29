package com.cn.elysee.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author hzx 
 * 2014��4��21��
 * @version V1.0
 */

@Retention(RUNTIME)
@Target(
{ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
public @interface CompareAnnotation
{
	/**
	 * ѡ������������Ա�ʶ 
	 * 2014��4��21��
	 * @return ���ر�ʶ��
	 */
	public int sortFlag() default 0;
}
