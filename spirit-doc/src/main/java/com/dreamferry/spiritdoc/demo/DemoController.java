package com.dreamferry.spiritdoc.demo;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamferry.spiritdoc.annotation.SpiritDoc;
import com.dreamferry.spiritdoc.service.SpiritAnnotationFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午12:03:52
*/
@RestController
@SpiritDoc(categoryName = "示例")
public class DemoController {
	
	@Autowired
	private SpiritAnnotationFactory factory;


	@RequestMapping(value = "demo")
	public Long demo(Demo demo) {
		print();
		Map<String, Object> model = new HashMap<String, Object>();
        
        Configuration configuration = new Configuration(Configuration.getVersion());

		StringWriter result = new StringWriter();
		Template t;
		try {
			String templateStr = FileUtils.readFileToString(new File("D:\\dev\\sourcecode\\github\\spiritdoc\\spirit-doc\\src\\main\\resources\\template\\showdoc\\Java_API_template.tmpl"));
			t = new Template("demo", new StringReader(templateStr), configuration);
			model.put("descirption", "用户注册接口");
			model.put("host", "http://xx.com/");
			model.put("apiPath", "user/register");
			model.put("methodList", Arrays.asList("POST", "GET"));
			
			Map<String, String> map1 = paramItem("username", "是", "string", "用户名");
			Map<String, String> map2 = paramItem("password", "是", "string", "密码");
			Map<String, String> map3 = paramItem("name", "否 ", "string", "昵称");
			model.put("paramList", Arrays.asList(map1, map2, map3));
			model.put("responeStruct", "{\"uid\": \"1\",\"username\": \"12154545\",\"name\": \"吴系挂\",\"groupid\": 2 ,\"reg_time\": \"1436864169\",\"last_login_time\": \"0\",}");
			
			
			Map<String, String> map4 = new HashMap<String, String>();
			map4.put("fieldName", "groupid");
			map4.put("fieldType", "int");
			map4.put("fieldDesc", "用户组id，1：超级管理员；2：普通用户");
			model.put("responseParamList", Arrays.asList(map4));
			
			t.process(model, result);
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return System.currentTimeMillis();
	}
	
	private Map<String, String> paramItem(String fieldName, String mandatory, String fieldType, String fieldDesc) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("fieldName", fieldName);
		map.put("mandatory", mandatory);
		map.put("fieldType", fieldType);
		map.put("fieldDesc", fieldDesc);
		return map;
	}
	
	@RequestMapping(value = "demo/test")
	@SpiritDoc(categoryName = "测试打印", pageTitle = "DemoTest")
	public void demoTest(Demo demo, Long id) {
		this.factory.getInstance(null).parseDocumention();
	}
	
	@RequestMapping(value = "demo/test2")
	@SpiritDoc(categoryName = "测试打印2", pageTitle = "DemoTest2")
	public Demo demoTest2(Demo demo) {
		this.factory.getInstance(null).parseDocumention();
		return null;
	}
	
	private void print() {
		System.out.println(System.currentTimeMillis());
	}
}
