package com.video.manager;

import java.io.File;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Mediafileinfo;

public class DownloadMediaFileHandler {
	private boolean m_ok;
	private String m_filepath;
	private String m_errorinfo;
	
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
	
	//�����ļ�id�Լ��ļ�����λ�ļ�λ��
	public boolean checkFileRealPath(int fileid, String fileseg){
		Session sen = HibernateSessionFactory.getSession();
		Query query = sen.createQuery("from Mediafileinfo where id=:id");
		query.setInteger("id", fileid);
		Mediafileinfo fileinfo = (Mediafileinfo) query.uniqueResult();
		if(fileinfo == null){
			setError("��idΪ" + fileid + "�ļ�¼");
			return false;
		}
		
		String filename;
		if(fileseg == null || fileseg.length() == 0 || fileseg.equalsIgnoreCase("init.mpd")){
			filename = fileinfo.getFilename();
		}
		else{
			filename = fileseg;
		}
		
		File targfile = new File(fileinfo.getFilepath() + "/" + filename);
		if(!targfile.exists()){
			setError("�������ļ�������");
			return false;
		}
		
		String rootpath = formatPathString(ServletActionContext.getServletContext().getRealPath(""));
		m_filepath = formatPathString(fileinfo.getFilepath());
		if(!m_filepath.startsWith(rootpath)){
			setError("�������ļ��洢·������");
			return false;
		}
		
		m_filepath = m_filepath.substring(rootpath.length()) + "/" + filename;

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
