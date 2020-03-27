package com.dreamferry.spiritdoc.service.impl;

import com.dreamferry.spiritdoc.config.SpiritProperties;
import com.dreamferry.spiritdoc.enums.AnnotationType;
import com.dreamferry.spiritdoc.service.SpiritAnnotationFactory;
import com.dreamferry.spiritdoc.service.SpiritAnnotationService;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午1:05:47
*/
public class SpiritAnnotationFactoryImpl implements SpiritAnnotationFactory{


	private SpiritAnnotationService defaultSpiritAnnotationService;
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
					instance = defaultSpiritAnnotationService;
					break;
				}
			}
		}
		return instance;
	}

	@Override
	public void autoScan(SpiritProperties properties) {
		this.init();
		this.getInstance(AnnotationType.SHOW_DOC, null).parseDocumention(properties);
		this.getInstance(AnnotationType.SWAGGER, null).parseDocumention(properties);
	}

	@Override
	public void init() {
		if (null == defaultSpiritAnnotationService) {
			defaultSpiritAnnotationService = new SpiritAnnotationServiceDefaultImpl();
		}
		if (null == swaggerSpiritAnnotationService) {
			swaggerSpiritAnnotationService = new SpiritAnnotationServiceSwaggerImpl();
		}
	}

}
