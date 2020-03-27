package com.dreamferry.spiritdoc.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dreamferry.spiritdoc.enums.AnnotationType;
import com.dreamferry.spiritdoc.service.SpiritAnnotationFactory;
import com.dreamferry.spiritdoc.service.SpiritAnnotationService;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午1:05:47
*/
@Service
public class SpiritAnnotationFactoryImpl implements SpiritAnnotationFactory{


	@Resource(name = "defaultSpiritAnnotationService")
	private SpiritAnnotationService defaultSpiritAnnotationService;
	@Resource(name = "swaggerSpiritAnnotationService")
	private SpiritAnnotationService swaggerSpiritAnnotationService;
	
	@Override
	public SpiritAnnotationService getInstance(AnnotationType type, SpiritAnnotationService extServiceInstance) {
		SpiritAnnotationService instance = null;
		if (null != extServiceInstance) {
			instance = extServiceInstance;
		}else {
			if (null == type) {
				instance = defaultSpiritAnnotationService;
			}else {
				switch (type) {
				case SWAGGER:
					instance = swaggerSpiritAnnotationService;
					break;

				default:
					break;
				}
			}
		}
		return instance;
	}

}
