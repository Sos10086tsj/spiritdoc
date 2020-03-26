package com.dreamferry.spiritdoc.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamferry.spiritdoc.annotation.SpiritDoc;
import com.dreamferry.spiritdoc.config.SpiritProperties;
import com.dreamferry.spiritdoc.model.ShowDocItem;
import com.dreamferry.spiritdoc.service.SpiritAnnotationService;
import com.dreamferry.spiritdoc.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午1:07:28
*/
@Service("defaultSpiritAnnotationService")
@Slf4j
public class DefaultSpiritAnnotationService implements SpiritAnnotationService{

	@Autowired
	private SpiritProperties spiritProperties;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void parseDocumention() {
		//1. 获取注解的类
		Reflections reflections = new Reflections(spiritProperties.getScanPackage());
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(SpiritDoc.class);
		Class requestAnnotationClass = null;
		if (!StringUtils.isEmpty(spiritProperties.getRequestAnnotationName())) {
			try {
				requestAnnotationClass = Class.forName(spiritProperties.getRequestAnnotationName());
			} catch (ClassNotFoundException e) {
				log.error("{}", e);
			}
		}
		List<ShowDocItem> items = new ArrayList<ShowDocItem>();
		for (Class<?> targetClass : classSet) {
			//获取方法
			Method[] methods = targetClass.getMethods();
			for (Method method : methods) {
				boolean isReqeustMethod = false;
				if (null != method.getAnnotation(RequestMapping.class) 
						|| null != method.getAnnotation(ResponseBody.class)
						|| null != method.getAnnotation(GetMapping.class)
						|| null != method.getAnnotation(PostMapping.class)
						) {
					isReqeustMethod = true;
				}
				if (null != requestAnnotationClass && null != method.getAnnotation(requestAnnotationClass)) {
					isReqeustMethod = true;
				}
				if (isReqeustMethod) {
					ShowDocItem item = this.parseAnnotation(targetClass.getAnnotation(SpiritDoc.class), method);
					items.add(item);
				}
			}
		}
		items.forEach(i -> {
			send2ShowDoc(i);
		});
	}

	/**
	 * 	解析注解
	 * @param classAnnotation
	 * @param method
	 * @return
	 */
	private ShowDocItem parseAnnotation(SpiritDoc classAnnotation, Method method) {
//		AnnotationType type = classAnnotation.type();
		String categoryName = classAnnotation.categoryName() + "/";
		String pageTitle = classAnnotation.pageTitle() + "-";
		String pageContent = classAnnotation.pageContent() + "<br/>";
		
		SpiritDoc methodAnnotation = method.getAnnotation(SpiritDoc.class);
		if (null != methodAnnotation) {
			if (StringUtils.isEmpty(methodAnnotation.categoryName())) {
				categoryName += method.getName();
			}else {
				categoryName += methodAnnotation.categoryName();
			}
			if (StringUtils.isEmpty(methodAnnotation.pageTitle())) {
				pageTitle += method.getName();
			}else {
				pageTitle += methodAnnotation.pageTitle();
			}
			if (StringUtils.isEmpty(methodAnnotation.pageContent())) {
				pageContent += method.getName();
			}else {
				pageContent += methodAnnotation.pageContent();
			}
		}else {
			categoryName += method.getName();
			pageTitle += method.getName();
			pageContent += method.getName();
		}
		return new ShowDocItem(categoryName, pageTitle, pageContent);
	}
	
	/**
	 * 	https://www.showdoc.cc/page/102098
	 * @param item
	 */
	private void send2ShowDoc(ShowDocItem item) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("api_key", spiritProperties.getShowDocApiKey());
		parameters.put("api_token", spiritProperties.getShowDocApiToken());
		parameters.put("cat_name", item.getCategoryName());
		parameters.put("page_title", item.getPageTitle());
		parameters.put("page_content", item.getPageContent());
		System.out.println(JSON.toJSONString(parameters));
		String responseText = HttpUtil.post(spiritProperties.getShowDocHost(), parameters, "application/x-www-form-urlencoded");
		JSONObject jo = JSON.parseObject(responseText);
		if (jo.getInteger("error_code").intValue() != 0) {
			log.error("{}", responseText);
		}
	}
}
