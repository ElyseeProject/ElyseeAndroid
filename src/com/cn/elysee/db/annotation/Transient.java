
package com.cn.elysee.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;


/**
 * @author hzx
 * 2014年4月18日
 * @Description 设置实体属性不被识别为表字段
 * @version V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient
{

}
