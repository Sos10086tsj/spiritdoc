package com.dreamferry.spiritdoc.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamferry.spiritdoc.annotation.SpiritDoc;
import com.dreamferry.spiritdoc.service.SpiritAnnotationFactory;

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
        
		return System.currentTimeMillis();
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
