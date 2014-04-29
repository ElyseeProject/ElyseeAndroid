
package com.cn.elysee.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author hzx
 * 2014��4��21��
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface InjectView
{
	/**
	 * View��ID
	 * 2014��4��21��
	 * @return
	 */
	public int id() default -1;
	
	/**
	 * View�ĵ���¼�
	 * 2014��4��21��
	 * @return
	 */
	public String click() default "";
	/**
	 * View�ĳ������¼�
	 * 2014��4��21��
	 * @return
	 */
	public String longClick() default "";
	/**
	 * View�Ľ���ı��¼�
	 * 2014��4��21��
	 * @return
	 */
	public String focusChange() default "";
	/**
	 * View�ļ����¼�
	 * 2014��4��21��
	 * @return
	 */
	public String key() default "";
	/**
	 * View�Ĵ����¼�
	 * 2014��4��21��
	 * @return
	 */
	public String touch() default "";
	
	
}
