package com.helper.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

enum HTTPMETHOD{
	GET,
	POST
}

public class HttpRequest extends BaseHttpClass implements Runnable{
	private Map<String, List<String>> m_cookies;
	private HttpCallbackInterface m_callback;
	
	public HttpRequest(String url) throws MalformedURLException{
		super(url);
	}
	
	public void setHttpGetMethod(){
		m_httpmethod = "GET";
	}
	
	public List<String> getCookies(String key){
		if(m_cookies != null && m_cookies.containsKey(key))
			return m_cookies.get(key);
		else
			return null;
	}
	
	public void setCallbackMethod(HttpCallbackInterface obj){
		m_callback = obj;
	}
	
	public boolean SendRequest(){
		try{
			this.createConnection();
			this.getResponseContent(m_connection.getInputStream(), true);
			m_cookies = m_connection.getHeaderFields();
			return true;
		} catch (IOException e) {
			try {
				this.getResponseContent(m_connection.getErrorStream(), false);
				return false;
			} catch (IOException e1) {
				m_response = e1.getMessage();
				return false;
			}
		}
		finally{
			this.close();
		}
	}
	
	public void close(){
		if(m_connection != null){
			m_connection.disconnect();
		}
	}
	
	public void run(){
		boolean result = SendRequest();
		if(m_callback != null)
			m_callback.httpCallback(result, m_response, m_url, m_params);
	}
}
