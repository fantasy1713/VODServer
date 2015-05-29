package com.weblib.manager;

import java.net.MalformedURLException;
import java.util.List;

import net.sf.json.JSONObject;
import com.format.json.SelectMemeberJson;
import com.helper.http.HttpRequest;

public class WeblibServerInfo implements AliveErrorInterface {
	private String m_host;
	private String m_account;
	private String m_password;
	private boolean m_islogin;
	private String m_sessionId;
	private WeblibAliveHelper m_alivehelper;
	private Thread helperthread;
	
	public WeblibServerInfo(String host, String account, String password) throws MalformedURLException{
		m_host = host;
		m_account = account;
		m_password = password;
		m_islogin = false;
	}
	
	public String getHost(){
		return m_host;
	}
	
	public String getCookie(){
		return m_sessionId;
	}
	
	public boolean isLogin(){
		return m_islogin;
	}
	
	public boolean login(){
		HttpRequest request;
		try {
			request = new HttpRequest("http://" + m_host + "/login/authenticate.action");
			request.addParam("account", m_account);
			request.addParam("password", m_password);
			if(!request.SendRequest())
				return false;
			m_sessionId = this.getSessionId(request.getCookies("Set-Cookie"));
			String response = request.getResponse();
			JSONObject obj = JSONObject.fromObject(response);
			SelectMemeberJson memberinfo = (SelectMemeberJson)JSONObject.toBean(obj, SelectMemeberJson.class);
			
			HttpRequest request2 = new HttpRequest("http://" + m_host + "/login/selectMember.action");
			request2.addParam("memberId", String.valueOf(memberinfo.getMembers()[0].getId()));
			request2.addHeader("Cookie", m_sessionId);
			if(!request2.SendRequest())
				return false;

			System.out.println(request2.getResponse());
			m_islogin = true;
			
			if(helperthread != null)
				helperthread.interrupt();
			m_alivehelper = new WeblibAliveHelper("http://" + m_host, m_sessionId, this);
			helperthread = new Thread(m_alivehelper);
			helperthread.start();

			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String getSessionId(List<String> cookies){
		if(cookies == null)
			return null;
		
		for(String value : cookies){
			if(value.startsWith("JSESSIONID=")){
				int end = value.indexOf(";");
				if(end > 0)
					return value.substring(0, end);
				else
					return value;
			}
		}
		return null;
	}

	@Override
	public void aliveErrorNotify() {
		System.out.println("Weblib Alive Error");
		m_islogin = false;
	}
}
