package com.video.manager;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;

import com.global.defines.ServerSettings;
import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Mediafileinfo;

public class DownloadMediaFileHandler {
	private boolean m_ok;
	private String m_filepath;
	private String m_errorinfo;
	Logger logger = Logger.getLogger("pay-log");
	
	public DownloadMediaFileHandler(){
		m_ok = false;
	}
	
	public String getFilePath(){
		return m_filepath;
	}
	
	public boolean isSuccess(){
		return m_ok;
	}
	
	public String getError(){
		return m_errorinfo;
	}
	
	//根据文件id以及文件名定位文件位置
	public boolean checkFileRealPath(int fileid, String fileseg){
		
		logger.info("func checkFileRealPath");
		
		Session sen = HibernateSessionFactory.getSession();
		Query query = sen.createQuery("from Mediafileinfo where id=:id");
		query.setInteger("id", fileid);
		Mediafileinfo fileinfo = (Mediafileinfo) query.uniqueResult();
		if(fileinfo == null){
			logger.error("无id为" + fileid + "的记录");
			setError("无id为" + fileid + "的记录");
			return false;
		}
		
		String filename;
		if(fileseg == null || fileseg.length() == 0 || fileseg.equalsIgnoreCase("init.mpd")){
			logger.info("seg=init.mpd");
			filename = fileinfo.getFilename();
			
		}
		else{
			logger.info("seg !=init.mpd");
			filename = fileseg;
		}
		logger.info("filename = "+filename);
		
		File targfile = new File(fileinfo.getFilepath() + "/" + filename);
		if(!targfile.exists()){
			logger.error("服务器文件不存在");
			setError("服务器文件不存在");
			return false;
		}
		
		String rootpath =formatPathString(ServerSettings.getRootDirPath());// formatPathString(ServletActionContext.getServletContext().getRealPath(""));
		m_filepath = formatPathString(fileinfo.getFilepath());
		if(!m_filepath.startsWith(rootpath)){
			logger.error("服务器文件存储路径错误");
			setError("服务器文件存储路径错误");
			return false;
		}
		
		//m_filepath = m_filepath.substring(rootpath.length()) + "/" + filename;
		m_filepath = m_filepath + "/" + filename;

		logger.info("m_filepath = "+m_filepath);
		
		m_ok = true;
		return m_ok;
	}
	
	private String formatPathString(String str){
		return str.replace('\\', '/');
	}
	
	private void setError(String info){
		m_ok = false;
		m_errorinfo = info;
	}
}
