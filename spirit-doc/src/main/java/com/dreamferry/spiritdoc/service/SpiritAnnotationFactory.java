package com.dreamferry.spiritdoc.service;

import com.dreamferry.spiritdoc.enums.AnnotationType;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午1:02:58
*/
public interface SpiritAnnotationFactory {
	public SpiritAnnotationService getInstance(AnnotationType type, SpiritAnnotationService extServiceInstance);
}
