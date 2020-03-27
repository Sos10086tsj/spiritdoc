package com.dreamferry.spiritdoc.service.impl;
/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月27日 下午1:40:36
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dreamferry.spiritdoc.config.SpiritProperties;
import com.dreamferry.spiritdoc.model.ShowDocItem;
import com.dreamferry.spiritdoc.model.ShowDocProcessInfo;
import com.dreamferry.spiritdoc.service.SpiritAnnotationService;
import com.dreamferry.spiritdoc.util.HttpUtil;
import com.github.jsonzou.jmockdata.JMockData;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SpiritAnnotationServiceAbstractImpl implements SpiritAnnotationService{

	@Override
	public void parseDocumention(SpiritProperties spiritProperties) {
		Reflections reflections = new Reflections(spiritProperties.getScanPackage());
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(getAnnotationClass());
		List<ShowDocItem> items = new ArrayList<ShowDocItem>();
		for (Class<?> targetClass : classSet) {
			String classApiPath = "";
			if (null != targetClass.getAnnotation(RequestMapping.class) && targetClass.getAnnotation(RequestMapping.class).value().length > 0) {
				classApiPath += targetClass.getAnnotation(RequestMapping.class).value()[0];
				if (!classApiPath.endsWith("/")) {
					classApiPath += "/";
				}
			}
			
			Method[] methods = targetClass.getMethods();
			for (Method method : methods) {
				Annotation annotation = method.getAnnotation(getMethodAnnotationClass());
				if (null == annotation) {
					continue;
				}
				boolean isReqeustMethod = false;
				String apiPath = null;
				String[] requestMethods = null;
				if (null != method.getAnnotation(RequestMapping.class)) {
					isReqeustMethod = true;
					if (method.getAnnotation(RequestMapping.class).value().length > 0) {
						apiPath = classApiPath + method.getAnnotation(RequestMapping.class).value()[0];
					}
					if (method.getAnnotation(RequestMapping.class).method().length == 0) {
						requestMethods = new String[] { "GET", "POST" };
					} else {
						RequestMethod[] requestMappingMethods = method.getAnnotation(RequestMapping.class).method();
						requestMethods = new String[requestMappingMethods.length];
						for (int j = 0; j < requestMappingMethods.length; j++) {
							requestMethods[j] = requestMappingMethods[j].name();
						}
					}
				}
				if (null != method.getAnnotation(GetMapping.class)) {
					isReqeustMethod = true;
					if (method.getAnnotation(GetMapping.class).value().length > 0) {
						apiPath = classApiPath + method.getAnnotation(GetMapping.class).value()[0];
					}
					requestMethods = new String[] { "GET" };
				}
				if (null != method.getAnnotation(PostMapping.class)) {
					isReqeustMethod = true;
					if (method.getAnnotation(PostMapping.class).value().length > 0) {
						apiPath = classApiPath + method.getAnnotation(PostMapping.class).value()[0];
					}
					requestMethods = new String[] { "POST" };
				}
				if (isReqeustMethod) {
					ShowDocItem item = this.parseAnnotation(spiritProperties, apiPath, requestMethods,
							targetClass.getAnnotation(getAnnotationClass()), method);
					items.add(item);
				}
			}
		}
		items.forEach(i -> {
			send2ShowDoc(i, spiritProperties);
		});
	}

	/**
	 * 	获取类注解
	 * @return
	 */
	protected abstract Class<? extends Annotation> getAnnotationClass();
	
	/**
	 * 	获取方法注解
	 * @return
	 */
	protected abstract Class<? extends Annotation> getMethodAnnotationClass();
	
	/**
	 * 	获取转换为 showDoc的参数
	 * @param classAnnotation
	 * @param method
	 * @return
	 */
	protected abstract ShowDocProcessInfo getDocProcessInfo(Annotation classAnnotation, Method method);
	
	private ShowDocItem parseAnnotation(SpiritProperties spiritProperties, String apiPath, String[] requestMethods, Annotation classAnnotation,
			Method method) {

		ShowDocProcessInfo processInfo = getDocProcessInfo(classAnnotation, method);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("descirption", processInfo.getDescirption());
		model.put("host", spiritProperties.getShowDocApiHost());
		model.put("apiPath", apiPath);
		model.put("methodList", requestMethods);

		Class<?>[] parameterTypes = method.getParameterTypes();
		List<Map<String, String>> paramList = new ArrayList<Map<String,String>>();
		if (null != parameterTypes) {
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> parameterType = parameterTypes[i];
				if (parameterType.equals(Integer.class) || parameterType.getName().equals("int")
						|| parameterType.equals(Long.class) || parameterType.getName().equals("long")
						) {
					paramList.add(this.parseRequestConstParameters(parameterType, method.getParameters()[i]));
				}else {
					paramList.addAll(this.parseReqeustParameters(parameterType, null));
				}
			}
		}
		model.put("paramList", paramList);
		model.put("hasRequestDesc", paramList.size() == 0 ? 0 : 1);

		Class<?> returnType = method.getReturnType();
		String returnStructure = null;
		List<Map<String, String>> responseParamList = new ArrayList<Map<String,String>>();
		model.put("hasResponseDesc", 0);
		if (returnType.equals(Void.class) || returnType.getName().equals("void")) {
			returnStructure = "Void";
		} else if (returnType.equals(Integer.class) || returnType.getName().equals("int")) {
			returnStructure = "Integer";
		} else if (returnType.equals(Long.class) || returnType.getName().equals("long")) {
			returnStructure = "Long";
		} else if (returnType.equals(BigDecimal.class)) {
			returnStructure = "BigDecimal";
		} else if (returnType.equals(Date.class)) {
			returnStructure = "Date";
		}else {
			try {
				returnStructure = JSON.toJSONString(JMockData.mock(returnType), SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
				responseParamList.addAll(this.parseResponseParameters(returnType, null));
				model.put("hasResponseDesc", 1);
			} catch (Exception e) {
				log.error("{}", e);
			}
		}
		model.put("responeStruct", null == returnStructure ? "" : returnStructure);
		model.put("responseParamList", responseParamList);

		String pageContent = this.fillupTempalte(model, spiritProperties);
		return new ShowDocItem(processInfo.getCategoryName(), processInfo.getPageTitle(), pageContent);
	}
	
	/**
	 * 	解析参数
	 * @param parameterType
	 * @param parameter
	 * @return
	 */
	private Map<String, String> parseRequestConstParameters(Class<?> parameterType, Parameter parameter) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("fieldName", parameter.getName());
		map.put("mandatory", "否");
		map.put("fieldDesc", "-");
		map.put("fieldType", parameterType.getSimpleName());
		return map;
	}
	
	/**
	 * 	解析参数
	 * @param parameterType
	 * @param prefix
	 * @return
	 */
	private List<Map<String, String>> parseReqeustParameters(Class<?> parameterType, String prefix) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Field[] fields = parameterType.getDeclaredFields();
		for (Field field : fields) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("fieldName", (null == prefix ? "" : prefix + ".") + field.getName());
			Class<?> fieldType = field.getType();
			map.put("fieldType", fieldType.getSimpleName());
			if (fieldType.equals(Integer.class) || fieldType.getName().equals("int")
					|| fieldType.equals(Long.class) || fieldType.getName().equals("long")
					|| fieldType.equals(String.class)
					|| fieldType.equals(BigDecimal.class)
					|| fieldType.equals(Date.class)) {
				parseRequestFieldModelMap(field, map);
				list.add(map);
			}else {
				list.addAll(this.parseReqeustParameters(fieldType, map.get("fieldName")));
			}
		}
		return list;
	}
	
	/**
	 * 	解析字段注解
	 * @param field
	 * @param map
	 */
	protected abstract void parseRequestFieldModelMap(Field field, Map<String, String> map);
	
	private List<Map<String, String>> parseResponseParameters(Class<?> parameterType, String prefix) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Field[] fields = parameterType.getDeclaredFields();
		for (Field field : fields) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("fieldName", (null == prefix ? "" : prefix + ".") + field.getName());
			Class<?> fieldType = field.getType();
			map.put("fieldType", fieldType.getSimpleName());
			
			if (fieldType.equals(Integer.class) || fieldType.getName().equals("int")
					|| fieldType.equals(Long.class) || fieldType.getName().equals("long")
					|| fieldType.equals(String.class)
					|| fieldType.equals(BigDecimal.class)
					|| fieldType.equals(Date.class)) {
				parseResponseFieldModelMap(field, map);
				list.add(map);
			}else {
				list.addAll(this.parseResponseParameters(fieldType, map.get("fieldName")));
			}
		}
		return list;
	}
	
	protected abstract void parseResponseFieldModelMap(Field field, Map<String, String> map);
	
	private String fillupTempalte(Map<String, Object> model, SpiritProperties spiritProperties) {
		Configuration configuration = new Configuration(Configuration.getVersion());
		StringWriter result = new StringWriter();
		try {
			ClassPathResource resource = new ClassPathResource(spiritProperties.getShowDocJavaApiTemplate());
			String templateStr = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8")).lines().collect(Collectors.joining(System.lineSeparator()));
			Template t = new Template("Java_API_template", new StringReader(templateStr), configuration);
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			log.error("{}", e);
		} finally {
			if (null != result) {
				try {
					result.close();
				} catch (IOException e) {
					log.error("{}", e);
				}
			}

		}
		return null;
	}
	
	/**
	 * API请求showdoc https://www.showdoc.cc/page/102098
	 * 
	 * @param item
	 */
	private void send2ShowDoc(ShowDocItem item, SpiritProperties spiritProperties) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("api_key", spiritProperties.getShowDocApiKey());
		parameters.put("api_token", spiritProperties.getShowDocApiToken());
		parameters.put("cat_name", item.getCategoryName());
		parameters.put("page_title", item.getPageTitle());
		parameters.put("page_content", item.getPageContent());

		String responseText = HttpUtil.post(spiritProperties.getShowDocHost(), parameters,
				"application/x-www-form-urlencoded");
		JSONObject jo = JSON.parseObject(responseText);
		if (jo.getInteger("error_code").intValue() != 0) {
			log.error("{}", responseText);
		}
	}
}
