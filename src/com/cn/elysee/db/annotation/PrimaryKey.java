package com.cn.elysee.db.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author hzx 
 * 2014年4月18日
 * @Description 不配置的时候默认找类的id或_id字段作为主键，
 * column不配置的是默认为字段名，如果不设置主键，自动生成ID
 * @version V1.0
 */
@Target(
{ METHOD, FIELD })
@Retention(RUNTIME)
public @interface PrimaryKey
{
	/**
	 * 设置主键名
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * 字段默认值
	 * 
	 * @return
	 */
	public String defaultValue() default "";

	/**
	 * 是否自动自增
	 * 
	 * @return
	 */
	boolean autoIncrement() default false;
}
