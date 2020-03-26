package com.dreamferry.spiritdoc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	@SuppressWarnings("deprecation")
	public static String get(String url, Map<String, String> parameters) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String context = StringUtils.EMPTY;
		HttpGet httpGet = null;
		try {
			if (null != parameters) {
				int i = 0;
				for (String key : parameters.keySet()) {
					if (i == 0) {
						url += "?";
					} else {
						url += "&";
					}
					url += key + "=" + parameters.get(key);
					i++;
				}
			}
			logger.debug("*********** post url: {}", url);
			httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(20000)
					.setConnectionRequestTimeout(10000).build();
			httpGet.setConfig(requestConfig);

			// 设置回调接口接收的消息头
			httpGet.addHeader("Content-Type", "application/json");

			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			context = EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			try {
				if (null != response) {
					response.close();
				}

				if (null != httpGet) {
					httpGet.abort();
				}
				if (null != httpClient) {
					httpClient.close();
				}

			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return context;
	}

	@SuppressWarnings("deprecation")
	public static String post(String url, Map<String, String> parameters, String contentType) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(20000)
				.setConnectionRequestTimeout(10000).build();
		httpPost.setConfig(requestConfig);
		String context = StringUtils.EMPTY;
		CloseableHttpResponse response = null;
		try {
			if (null != parameters) {
				List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
				for (String key : parameters.keySet()) {
					list.add(new BasicNameValuePair(key, parameters.get(key)));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
				httpPost.setEntity(entity);
			}
			// 设置回调接口接收的消息头
			httpPost.addHeader("Content-Type", null == contentType ? "application/json" : contentType);

			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			context = EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			try {
				
				if (null != response) {
					response.close();
				}

				if (null != httpPost) {
					httpPost.abort();
				}
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return context;
	}

	/**
	 * 解析出url参数中的键值对
	 * 
	 * @param url
	 *            url参数
	 * @return 键值对
	 */
	public static Map<String, String> getRequestParam(String url) {

		Map<String, String> map = new HashMap<String, String>();
		String[] arrSplit = null;

		// 每个键值为一组
		arrSplit = url.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				map.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (!arrSplitEqual[0].equals("")) {
					map.put(arrSplitEqual[0], "");
				}
			}
		}
		return map;
	}
}
