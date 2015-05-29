package com.helper.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseHttpClass {
	protected String m_httpmethod;
	protected String m_url;
	protected HttpURLConnection m_connection;
	protected int m_timeout;
	protected Map<String, String> m_params;
	protected Map<String, String> m_headers;
	protected int m_contentlen;
	protected int m_bytesdownloaded;
	protected String m_response;
	
	public BaseHttpClass(String url) throws MalformedURLException{
		m_url = url;
		m_timeout = 30000;
		m_httpmethod = "POST";
	}
	
	public void setTimeout(int timeout){
		m_timeout = timeout;
	}
	
	public void addParam(String key, String value){
		if(m_params == null)
			m_params = new HashMap<String, String>();
		m_params.put(key, value);
	}
	
	public void addHeader(String key, String value){
		if(m_headers == null)
			m_headers = new HashMap<String, String>();
		m_headers.put(key, value);
	}
	
	public String getResponse(){
		return m_response;
	}
	
	protected void createConnection() throws IOException{
		StringBuffer param = null;
		if(m_params != null){
			for(String key : m_params.keySet()){
				if(param == null)
					param = new StringBuffer();
				else
					param.append("&");
				param.append(key + "=" + m_params.get(key));
			}
		}

		String requrl = m_url;
		if(m_httpmethod.equalsIgnoreCase("GET") && param != null){
			requrl += "?" + param;
		}

		URL url = new URL(requrl);
		m_connection = (HttpURLConnection) url.openConnection();
		m_connection.setRequestMethod(m_httpmethod);
		m_connection.setConnectTimeout(m_timeout);
		m_connection.setReadTimeout(m_timeout);
		m_connection.setDoInput(true);
		m_connection.setDoOutput(true);
		m_connection.setUseCaches(false);
		if(m_headers != null && m_headers.size() > 0){
			for(String key : m_headers.keySet()){
				m_connection.addRequestProperty(key, m_headers.get(key));
			}
		}
		
		if(m_httpmethod.equalsIgnoreCase("POST") && param != null){
			m_connection.getOutputStream().write(param.toString().getBytes());
			m_connection.getOutputStream().flush();
			m_connection.getOutputStream().close();
		}
		
		m_contentlen = m_connection.getContentLength();
	}
	
	public abstract void close();
	
	protected void getResponseContent(InputStream input, boolean encoded) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String line;
		StringBuffer content = new StringBuffer();
		while((line = br.readLine()) != null){
			content.append(line);
		}
		br.close();
		if(encoded == true)
			m_response = new String(content.toString().getBytes("GBK"), "UTF-8");
		else
			m_response = content.toString();
	}
}