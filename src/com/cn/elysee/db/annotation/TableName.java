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
@Target(ElementType.TYPE)
public @interface TableName
{
	/**
	 * 设置表名字
	 * 
	 * @return
	 */
	String name() default "";
}
