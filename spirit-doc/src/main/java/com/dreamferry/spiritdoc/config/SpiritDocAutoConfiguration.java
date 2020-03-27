package com.dreamferry.spiritdoc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dreamferry.spiritdoc.service.SpiritAnnotationFactory;
import com.dreamferry.spiritdoc.service.impl.SpiritAnnotationFactoryImpl;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月27日 下午12:46:22
*/

@Configuration
@ConditionalOnClass({SpiritAnnotationFactory.class})
@EnableConfigurationProperties(SpiritProperties.class)
public class SpiritDocAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(SpiritAnnotationFactory.class)
	public SpiritAnnotationFactory init(SpiritProperties properties) {
		SpiritAnnotationFactory factory = new SpiritAnnotationFactoryImpl();
		factory.autoScan(properties);
		return factory;
	}
}
