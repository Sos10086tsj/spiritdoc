package com.dreamferry.spiritdoc.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

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
	
	@Override
	public SpiritAnnotationService getInstance(SpiritAnnotationService extServiceInstance) {
		return null == extServiceInstance ? this.defaultSpiritAnnotationService : extServiceInstance;
	}

}
