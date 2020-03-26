package com.dreamferry.spiritdoc.demo;

import java.util.Date;

import com.dreamferry.spiritdoc.annotation.SpiritField;

import lombok.Data;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午12:48:24
*/
@Data
public class Demo {

	/**
	 * 	主键ID
	 */
	@SpiritField(desc = "主键ID")
	Long id;
	
	/**
	 * 	姓名
	 */
	@SpiritField(desc = "姓名")
	String name;
	
	/**
	 * 	生日
	 */
	@SpiritField(desc = "生日")
	Date birthday;
}
