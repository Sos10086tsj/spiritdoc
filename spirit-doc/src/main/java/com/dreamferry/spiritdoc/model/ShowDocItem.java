package com.dreamferry.spiritdoc.model;

import lombok.Data;

/** 
* Description: 
* @author paris tao
* @version 1.0.0 2020年3月26日 下午1:08:45
*/
@Data
public class ShowDocItem {

	private String categoryName;
	private String pageTitle;
	private String pageContent;
	
	public ShowDocItem() {}
	public ShowDocItem(String categoryName, String pageTitle, String pageContent) {
		this.categoryName = categoryName;
		this.pageTitle = pageTitle;
		this.pageContent = pageContent;
	}
}
