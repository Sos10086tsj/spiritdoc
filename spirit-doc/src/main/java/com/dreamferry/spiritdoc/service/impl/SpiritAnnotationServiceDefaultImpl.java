package com.dreamferry.spiritdoc.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.dreamferry.spiritdoc.annotation.SpiritDoc;
import com.dreamferry.spiritdoc.annotation.SpiritField;
import com.dreamferry.spiritdoc.model.ShowDocProcessInfo;
import com.dreamferry.spiritdoc.service.SpiritAnnotationService;

/**
 * Description:
 * 
 * @author paris tao
 * @version 1.0.0 2020年3月26日 下午1:07:28
 */
public class SpiritAnnotationServiceDefaultImpl extends SpiritAnnotationServiceAbstractImpl implements SpiritAnnotationService {

	@Override
	protected Class<? extends Annotation> getAnnotationClass() {
		return SpiritDoc.class;
	}

	@Override
	protected Class<? extends Annotation> getMethodAnnotationClass() {
		return SpiritDoc.class;
	}

	@Override
	protected ShowDocProcessInfo getDocProcessInfo(Annotation classAnnotation, Method method) {
		ShowDocProcessInfo processInfo = new ShowDocProcessInfo();
		
		SpiritDoc spiritDoc = (SpiritDoc)classAnnotation;
		String categoryName = spiritDoc.categoryName() + "/";
		String pageTitle = "";

		SpiritDoc methodAnnotation = method.getAnnotation(SpiritDoc.class);
		if (null != methodAnnotation) {
			if (StringUtils.isEmpty(methodAnnotation.categoryName())) {
				categoryName += method.getName();
			} else {
				categoryName += methodAnnotation.categoryName();
			}
			if (StringUtils.isEmpty(methodAnnotation.pageTitle())) {
				pageTitle += method.getName();
			} else {
				pageTitle += methodAnnotation.pageTitle();
			}
		} else {
			categoryName += method.getName();
			pageTitle += method.getName();
		}
		processInfo.setCategoryName(categoryName);
		processInfo.setPageTitle(pageTitle);
		if (null != methodAnnotation
				&& org.apache.commons.lang3.StringUtils.isNotEmpty(methodAnnotation.description())) {
			processInfo.setDescirption(methodAnnotation.description());
		} else {
			processInfo.setDescirption(method.getName());
		}
		return processInfo;
	}

	@Override
	protected void parseRequestFieldModelMap(Field field, Map<String, String> map) {
		SpiritField spiritField = field.getAnnotation(SpiritField.class);
		if (null == spiritField) {
			map.put("mandatory", "否");
			map.put("fieldDesc", "-");
		}else {
			map.put("mandatory", spiritField.mandatory() ? "是" : "否");
			map.put("fieldDesc", spiritField.desc());
		}
	}

	@Override
	protected void parseResponseFieldModelMap(Field field, Map<String, String> map) {
		SpiritField spiritField = field.getAnnotation(SpiritField.class);
		if (null == spiritField) {
			map.put("fieldDesc", "-");
		}else {
			map.put("fieldDesc", spiritField.desc());
		}
	}
}
