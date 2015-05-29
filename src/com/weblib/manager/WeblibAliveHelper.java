package com.weblib.manager;

import java.net.MalformedURLException;

import com.helper.http.HttpRequest;

public class WeblibAliveHelper implements Runnable{
	private String m_cookie;
	private String m_url;
	private AliveErrorInterface m_owner;
	
	public WeblibAliveHelper(String url, String cookie, AliveErrorInterface owner){
		m_url = url;
		m_cookie = cookie;
		m_owner = owner;
	}
	
	@Override
	public void run() {
		try {
			while(true){
				HttpRequest request = new HttpRequest(m_url + "/user/alive.action");
				request.addHeader("Cookie", m_cookie);
				request.setHttpGetMethod();
				if(!request.SendRequest()){
					System.out.println("Alive Error :" + request.getResponse());
					break;
				}
				
				System.out.println("Send Alive Request");
				Thread.sleep(120000);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			m_owner.aliveErrorNotify();
		}
	}
}
