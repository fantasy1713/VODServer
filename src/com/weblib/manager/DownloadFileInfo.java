package com.weblib.manager;

import com.global.defines.MediaFileStatusDefine;
import com.media.pipeline.MediaPipelineTaskClass;

public class DownloadFileInfo {
	private MediaPipelineTaskClass m_task;
	private String m_cookie;
	private int m_lefttime;
	private long m_byteswritten;
	private long m_size;
	private float m_pecent;
	private long m_speed;
	private long m_speedcounter;
	private long m_pretime;
	private FileStatus m_statu;
	
	DownloadFileInfo(MediaPipelineTaskClass task, String cookie){
		m_task = task;
		m_byteswritten = 0;
		m_speed = 0;
		m_speedcounter = 0;
		m_pecent = 0;
		m_cookie = cookie;
		m_size = 0;
		m_statu = FileStatus.PAUSE;
		
		m_pretime = System.currentTimeMillis();
		
		m_task.setStatus(MediaFileStatusDefine.DOWNLOADING);
	}
	
	public MediaPipelineTaskClass getTask(){
		return m_task;
	}

	public int getM_prototypeid() {
		return m_task.getPrototypeid();
	}

	public FileStatus getM_statu() {
		return m_statu;
	}

	public void setM_statu(FileStatus m_statu) {
		this.m_statu = m_statu;
	}

	public String getM_host() {
		return m_task.getWeblibhost();
	}

	public int getM_id() {
		return m_task.getWeblibid();
	}

	public String getM_cookie() {
		return m_cookie;
	}

	public String getM_filename() {
		return m_task.getFilename();
	}

	public String getM_dirpath() {
		return m_task.getFilepath();
	}

	public long getM_byteswritten() {
		return m_byteswritten;
	}

	public long getM_size() {
		return m_size;
	}

	public float getM_pecent() {
		return m_pecent;
	}
	
	public int getM_lefttime(){
		return m_lefttime;
	}

	public long getM_speed() {
		if(System.currentTimeMillis() - m_pretime > 1000)
			return 0;
		
		return m_speed;
	}

	public void addBytesWritten(int inc){
		m_byteswritten += inc;
		m_speedcounter += inc;
		
		if(m_size > 0){
			m_pecent = m_byteswritten / m_size;
			if(m_speed > 0)
				m_lefttime = (int) ((m_size - m_byteswritten) / m_speed);
			else
				m_lefttime = -1;
		}
		
		//隔1秒刷新一次速度
		long interval = System.currentTimeMillis() - m_pretime;
		if(interval < 1000)
			return;

		m_speed = (long) ((double)m_speedcounter / interval * 1000);
		m_speedcounter = 0;
		m_pretime = m_pretime + interval;
	}

	public void setM_size(long m_size) {
		this.m_size = m_size;
	}

	public void setM_pecent(float m_pecent) {
		this.m_pecent = m_pecent;
	}

	public void setM_speed(long m_speed) {
		this.m_speed = m_speed;
	}
}
