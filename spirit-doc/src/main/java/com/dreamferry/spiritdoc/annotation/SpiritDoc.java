package com.dreamferry.spiritdoc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.dreamferry.spiritdoc.enums.AnnotationType;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, PACKAGE })
/** 
* Description: 文档注解，可以用于类、接口、方法
* @author paris tao
* @version 1.0.0 2020年3月26日 下午12:42:08
*/
public @interface SpiritDoc {

	/**
	 * 	使用的第三方文档类型
	 * @return
	 */
	AnnotationType type() default AnnotationType.SHOW_DOC;
	
	/**
	 * 	目录，使用"/"分隔
	 * @return
	 */
	String categoryName() default "";
	
	/**
	 * 	页面标题，唯一。
	 * @return
	 */
	String pageTitle() default "";
	
	/**
	 * 	描述
	 * @return
	 */
	String description() default "";
}
