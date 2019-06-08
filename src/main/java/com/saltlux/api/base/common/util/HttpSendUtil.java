package com.saltlux.api.base.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * http 전송 관련 유틸 클래스
 * @author saltlux
 *
 */
public class HttpSendUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpSendUtil.class);

	public static final String NLU_ANALYSIS_URL = "http://211.109.9.10:3010/nlu/get";
	
//	http://211.109.9.10:3010/nlu/get?question=%EC%A0%84%EC%A7%80%ED%98%84%EC%9D%B4%20%EB%88%84%EA%B5%AC%EC%95%BC?	
	/**
	 * get 방식 전송 타입
	 * @param paramMap
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static String doGet(String url, Map<String, Object> paramMap) throws ClientProtocolException, IOException {
		
		//파라미타 설정 부분
		String tempUrl = url +"?";
		
		Iterator<String> keyIter = paramMap.keySet().iterator();
		logger.info("=============================   doGet    ===============================");
		while(keyIter.hasNext()) {
			String key = keyIter.next();
			logger.info(key);
			tempUrl = tempUrl + key + "=" + URLEncoder.encode(paramMap.get(key).toString(), "UTF-8");
			if(keyIter.hasNext()) {
				tempUrl = tempUrl + "&";
			}
		}
		
		logger.info("=============================   doGet  final  ===============================");
		logger.info(tempUrl);
		//통신 관련 변수들 생성
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(tempUrl);
		request.addHeader("Accept", "application/json");
		request.addHeader("Content-Type", "application/json");
		
		//타임아웃 설정
		RequestConfig reqConfig = RequestConfig.copy(RequestConfig.DEFAULT)
				.setConnectionRequestTimeout(2000)
				.setConnectTimeout(2000)
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
		
		logger.info("=============================   doGet  get final  ===============================");
		logger.info(sb.toString());

		return sb.toString();
	}
	
	/**
	 * post 방식 전송 타입
	 * @param paramMap
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
				.setConnectionRequestTimeout(2000)
				.setConnectTimeout(2000)
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
		
		return sb.toString();
	}
	
}
