package com.dreamferry.spiritdoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午12:57:30
*/
@Data
@ConfigurationProperties
public class SpiritProperties {

	/**
	 * 	扫描包路径
	 */
	@Value("${spiritDoc.scanPackage}")
	private String scanPackage;
	
	/**
	 * 	showDoc 地址
	 */
	@Value("${spiritDoc.showDoc.host}")
	private String showDocHost;
	
	/**
	 * 	showDoc api_key
	 */
	@Value("${spiritDoc.showDoc.apiKey}")
	private String showDocApiKey;
	
	/**
	 * 	showDoc api_token
	 */
	@Value("${spiritDoc.showDoc.apiToken}")
	private String showDocApiToken;
	
	/**
	 * 	showDoc 应用前缀地址
	 */
	@Value("${spiritDoc.showDoc.apiHost}")
	private String showDocApiHost;
	
	/**
	 * 	showDoc 应用前缀地址
	 */
	@Value("${spiritDoc.showDoc.javaApiTemplate}")
	private String showDocJavaApiTemplate;
}
