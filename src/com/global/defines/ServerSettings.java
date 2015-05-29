package com.global.defines;

import java.io.File;

import org.apache.struts2.ServletActionContext;

public class ServerSettings {
	private static ServerSettings g_instance = new ServerSettings();
	
	private String m_downloadfiledir;
	private String m_videodir;
	private int m_downloadconnects;
	private long m_downloadspeedlimit;
	
	ServerSettings(){
		String rootdir;
		try{
			rootdir = ServletActionContext.getServletContext().getRealPath("");
		}
		catch(Exception ex){
			rootdir = "/usr/local/glassfish3_confman/glassfish/domains/confman/applications/VodServer";
			//rootdir = "D:/apache-tomcat-7.0.53/webapps/VodServer";
		}
		m_downloadfiledir = rootdir + "/video/weblib";
		m_videodir = rootdir + "/video/encoded";

		m_downloadconnects = 5;
		m_downloadspeedlimit = Long.MAX_VALUE;
		
		this.checkAndCreateDir();
	}
	
	private void checkAndCreateDir(){
		File dfile = new File(m_downloadfiledir);
		if(!dfile.exists())
			dfile.mkdirs();
	}
	
	public static String getVideoDirPath(){
		return g_instance.m_videodir;
	}
	
	public static String getDownloadPath(){
		return g_instance.m_downloadfiledir;
	}
	
	public static int getDownloadConnects(){
		return g_instance.m_downloadconnects;
	}
	
	public static long getDownloadSpeedLimit(){
		return g_instance.m_downloadspeedlimit;
	}
}
