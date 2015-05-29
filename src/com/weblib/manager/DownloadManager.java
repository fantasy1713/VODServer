package com.weblib.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.global.defines.ServerSettings;

public class DownloadManager implements Runnable{
	private List<DownloadFile> m_downloadlist;
	private volatile boolean m_start;
	private long m_timetrack;
	private long m_totalspeed;
	private long m_speeddatacount;
	private long m_speedtimetrack;
	private DownloadCompleteInterface m_caller;
	
	private long m_testtime;
	
	DownloadManager(DownloadCompleteInterface caller){
		m_downloadlist = new ArrayList<DownloadFile>();
		m_start = false;
		m_totalspeed = 0;
		m_timetrack = System.currentTimeMillis();
		m_speedtimetrack = System.currentTimeMillis();
		m_caller = caller;
		
		m_testtime = System.currentTimeMillis();
	}
	
	public boolean isFullQueue(){
		return m_downloadlist.size() >= ServerSettings.getDownloadConnects();
	}
	
	public long getTotalSpeed(){
		return m_totalspeed;
	}
	
	private void downloadListFile() throws InterruptedException{
		while(m_downloadlist.size() > 0 && m_start == true){
			
			long interval = System.currentTimeMillis() - m_timetrack;
			if(interval < 20){
				Thread.sleep(20 - interval);
				continue;
			}
			
			if(interval > 1000)
				interval = 1000;

			m_timetrack = System.currentTimeMillis();
			
			long bytestoread = (long) ((double)ServerSettings.getDownloadSpeedLimit() * interval / 1000);
			List<DownloadFile> indexlist = new ArrayList<DownloadFile>();
			for(int i=0; i < m_downloadlist.size(); i++){
				DownloadFile item = m_downloadlist.get(i);
				try {
					if(item.canReadMore()){
						indexlist.add(item);
					}
				} catch (IOException e) {
					item.getFileInfo().setM_statu(FileStatus.ERROR);
					removeDownloadFile(item);
					e.printStackTrace();
				}
			}
			
			long datacount = 0;
			long chunksize;
			boolean isdatareaded = true;
			while(datacount < bytestoread
					&& (System.currentTimeMillis() - m_timetrack) <= 20
					&& isdatareaded == true){
				isdatareaded = false;
				chunksize = (bytestoread - datacount) / indexlist.size();
				for(int index = 0; index < indexlist.size(); index++){
					DownloadFile item = indexlist.get(index);
					if(item.isComplete())
						continue;

					try {
						int bytesreaded = item.processData(chunksize);
						if(bytesreaded > 0){
							datacount += bytesreaded;
							isdatareaded = true;
						}
						if(bytesreaded > chunksize){
							System.out.println("over size:" + bytesreaded);
						}
						
					} catch (IOException e) {
						item.getFileInfo().setM_statu(FileStatus.ERROR);
						removeDownloadFile(item);
						e.printStackTrace();
					}
					
					if(item.isComplete()){
						removeDownloadFile(item);
					}
				}
			}
			
			m_speeddatacount += datacount;
			if(System.currentTimeMillis() - m_speedtimetrack > 1000){
				long sinterval = System.currentTimeMillis() - m_speedtimetrack;
				m_totalspeed = (long) ((double)m_speeddatacount / sinterval * 1000);
				m_speeddatacount = 0;
				m_speedtimetrack = System.currentTimeMillis();
			}
			
			if(System.currentTimeMillis() - m_testtime > 1000){
				System.out.println("Current speed:" + m_totalspeed);
				m_testtime = System.currentTimeMillis();
				
				for(DownloadFile file : m_downloadlist){
					System.out.println("File " + file.getFileInfo().getM_filename() + " speed:" + file.getFileInfo().getM_speed());
				}
			}
		}
	}
	
	public void run() {
		try{
			while(true){
				waitForStart();
				downloadListFile();
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private long m_testend;
	
	private synchronized void waitForStart() throws InterruptedException{
		while(m_start == false){
			wait();
		}
	}
	
	private synchronized void removeDownloadFile(DownloadFile file){
		System.out.println("download finished:" + file.getFileInfo().getM_filename());
		
		m_testend = System.currentTimeMillis() - m_testend;
		System.out.println("time usage:" + m_testend);
		
		m_downloadlist.remove(file);
		if(m_downloadlist.size() == 0){
			m_start = false;
			m_totalspeed = 0;
			m_speeddatacount = 0;
		}
		
		try {
			file.close();
			file.getFileInfo().setM_statu(FileStatus.FINISHED);
		} catch (IOException e) {
			file.getFileInfo().setM_statu(FileStatus.ERROR);
			e.printStackTrace();
		}
		
		m_caller.downloadCallback(file.getFileInfo());
	}
	
	private boolean checkFileContains(DownloadFileInfo file){
		for(DownloadFile info : m_downloadlist){
			if(info.getFileInfo() == file)
				return true;
		}
		return false;
	}
	
	public synchronized boolean addDownloadFile(DownloadFileInfo file){
		if(this.isFullQueue() && this.checkFileContains(file))
			return false;
		
		DownloadFile dfile;
		try {
			dfile = new DownloadFile(file);
			dfile.openFile();
		} catch (Exception e) {
			file.setM_statu(FileStatus.ERROR);
			e.printStackTrace();
			return false;
		}
		
		System.out.println("start download:" + file.getM_filename());
		m_testend = System.currentTimeMillis();
		
		m_downloadlist.add(dfile);
		m_start = true;
		notifyAll();
		return true;
	}
}