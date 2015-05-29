package com.weblib.manager;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.global.defines.ServerSettings;
import com.media.pipeline.MediaPipelineClass;
import com.media.pipeline.MediaPipelineTaskClass;
import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Weblibinfo;

public class WeblibManager extends MediaPipelineClass implements DownloadCompleteInterface{
	private Map<String, WeblibServerInfo> m_webliblist;
	private DownloadManager m_downloadmanager;
	private Thread m_downloadthread;
	private List<DownloadFileInfo> m_infolist;
	
	public WeblibManager(){
		m_webliblist = new HashMap<String, WeblibServerInfo>();
		
		m_downloadmanager = new DownloadManager(this);
		m_downloadthread = new Thread(m_downloadmanager);
		m_downloadthread.start();
		m_infolist = new ArrayList<DownloadFileInfo>();
		
		this.initializeWeblib();
	}
	
	@Override
	public synchronized void downloadCallback(DownloadFileInfo info) {
		updateDownloadQueue();
		
		if(info.getM_statu() == FileStatus.FINISHED){
			m_infolist.remove(info);
			this.taskFinished(false, info.getTask());
		}
	}
	
	public boolean registWeblibServer(String url, String account, String password){
		WeblibServerInfo info;
		try {
			info = new WeblibServerInfo(url, account, password);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}

		if(!info.login())
			return false;
		
		m_webliblist.put(info.getHost(), info);
		return true;
	}
	
	public boolean isWeblibExists(String host){
		if(m_webliblist.containsKey(host))
			return true;
		else
			return false;
	}
	
	private void initializeWeblib(){
		Session sen = HibernateSessionFactory.getSession();
		Query query = sen.createQuery("from Weblibinfo");
		List<Weblibinfo> list = query.list();
		for(Weblibinfo info : list){
			this.registWeblibServer(info.getHost(), info.getUsername(), info.getPassword());
		}
		
		//清除残留的下载文件
		this.cleanFailedFiles();
	}
	
	private void cleanFailedFiles(){
		File downdir = new File(ServerSettings.getDownloadPath());
		if(!downdir.exists())
			return;
		if(!downdir.isDirectory())
			return;
		
		String []filelist = downdir.list();
		for (int i = 0; i < filelist.length; i++) {
		     File readfile = new File(downdir.getAbsolutePath() + "/" + filelist[i]);
		     if(!readfile.isDirectory())
		    	 readfile.delete();
		}
	}
	
	@Override
	public boolean addPipeTask(MediaPipelineTaskClass task) {
		if(!m_webliblist.containsKey(task.getWeblibhost()))
			return false;

		WeblibServerInfo info = m_webliblist.get(task.getWeblibhost());
		if(!info.isLogin())
			return false;
		
		this.addFile(task, info.getCookie());
		System.out.println("add weblib task:" + task.getWeblibid());
		return true;
	}
	
	private synchronized void addFile(MediaPipelineTaskClass task, String cookie){
		for(DownloadFileInfo info : m_infolist){
			if(info.getM_host().equalsIgnoreCase(task.getWeblibhost()) && info.getM_id() == task.getWeblibid()){
				System.out.println("Task already on Downloading");
				this.taskFinished(true, task);
				return;
			}
		}
		
		DownloadFileInfo info = new DownloadFileInfo(task, cookie);
		m_infolist.add(info);
		updateDownloadQueue();
	}
	
	private synchronized void updateDownloadQueue(){
		for(DownloadFileInfo info : m_infolist){
			if(m_downloadmanager.isFullQueue())
				return;
			
			if(info.getM_statu() == FileStatus.PAUSE){
				m_downloadmanager.addDownloadFile(info);
			}
		}
	}
}
