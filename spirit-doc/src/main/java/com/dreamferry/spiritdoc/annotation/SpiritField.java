package com.dreamferry.spiritdoc.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(SOURCE)
@Target(FIELD)
/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午12:51:23
*/
public @interface SpiritField {

	String desc();
	
	boolean mandatory() default true;
}
