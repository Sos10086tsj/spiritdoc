package com.dreamferry.spiritdoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午12:57:30
*/
@Configuration
@Data
public class SpiritProperties {

	/**
	 * 	扫描包路径
	 */
	@Value("${spiritDoc.scanPackage}")
	private String scanPackage;
	
	/**
	 * 	自定义请求注解
	 */
	@Value("${spiritDoc.requestAnnotationName}")
	private String requestAnnotationName;
	
	@Value("${spiritDoc.showDoc.host}")
	private String showDocHost;
	
	@Value("${spiritDoc.showDoc.apiKey}")
	private String showDocApiKey;
	
	@Value("${spiritDoc.showDoc.apiToken}")
	private String showDocApiToken;
}
