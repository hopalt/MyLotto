package com.saltlux.api.base.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

import lombok.extern.java.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;


import com.google.gson.Gson;

/**
 * http 전송 관련 유틸 클래스
 * @author saltlux
 *
 */
@Log
public class HttpSendUtil {

	/**
	 * get 방식 전송 타입
	 * @param paramMap
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String doGet(String url, Map<String, Object> paramMap) throws ClientProtocolException, IOException {

		String tempUrl = url;
		//파라미타 설정 부분

		if(paramMap != null) {
			tempUrl += "?";

			Iterator<String> keyIter = paramMap.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				tempUrl +=  key + "=" + URLEncoder.encode(paramMap.get(key).toString(), "UTF-8");
				if (keyIter.hasNext()) {
					tempUrl += "&";
				}
			}

		}

		//통신 관련 변수들 생성
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(tempUrl);
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");
		
		//타임아웃 설정
		RequestConfig reqConfig = RequestConfig.copy(RequestConfig.DEFAULT)
				.setConnectionRequestTimeout(15000)
				.setConnectTimeout(15000)
				.build();
		
		request.setConfig(reqConfig);
		
		//데이터 전송
		HttpResponse response = client.execute(request);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		StringBuffer sb = new StringBuffer();
		
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String result = sb.toString();

		log.info(result);

		return result;
	}
	
	/**
	 * post 방식 전송 타입
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
//	public static String doPost(String url, Map<String, Object> paramMap) throws ClientProtocolException, IOException {
	public static String doPost(String url, Object paramVo) throws ClientProtocolException, IOException {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(url);
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json; charset=utf-8 ");
		
		Gson gson = new Gson();
		String gsonStr = gson.toJson(paramVo);
		StringEntity entity = new StringEntity(gsonStr, StandardCharsets.UTF_8);
		request.setEntity(entity);
		
		
		//타임아웃 설정
		RequestConfig reqConfig = RequestConfig.copy(RequestConfig.DEFAULT)
			.setConnectionRequestTimeout(4000)
			.setConnectTimeout(4000)
			.build();
		
		request.setConfig(reqConfig);
		
		//데이터 전송
		HttpResponse response = client.execute(request);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		StringBuffer sb = new StringBuffer();
		
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String result = sb.toString();

		log.info(result);

		return result;
	}

}
