package com.cn.elysee.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * @author hzx 
 * 2014年4月18日
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column
{
	/**
	 * 设置字段名
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 字段默认值
	 * 
	 * @return
	 */
	public String defaultValue() default "";
}
