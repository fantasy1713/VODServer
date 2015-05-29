package com.media.converter;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

import com.global.defines.MediaFileStatusDefine;
import com.media.pipeline.MediaPipelineTaskClass;
import com.vodserver.hibernate.HibernateSessionFactory;
import com.vodserver.hibernate.beans.Mediafileinfo;

public class ConverVideo implements Runnable{
	private VideoConverterInterface m_caller;
	private MediaPipelineTaskClass m_task;
	
	private boolean m_issuccess;
	private boolean m_stop;
	
	public ConverVideo(VideoConverterInterface caller, MediaPipelineTaskClass task){
		m_caller = caller;
		m_task = task;
		m_stop = true;
		m_issuccess = false;
		m_task.setStatus(MediaFileStatusDefine.ENCODING);
	}
	
	public MediaPipelineTaskClass getTask(){
		return m_task;
	}
	
	public boolean isSuccess(){
		return m_issuccess;
	}
	
	public boolean isStop(){
		return m_stop;
	}
	
	public void setStart(){
		m_stop = false;
	}

	@Override
	public void run() {
		m_stop = false;
		
		Session sen = HibernateSessionFactory.getSession();
		//找到所有已有的对应媒体信息，作为参数传入转码过程，并对其进行更新
		Query query = sen.createQuery("from Mediafileinfo where srcfileid=:prototypeid");
		query.setInteger("prototypeid", m_task.getPrototypeid());
		List<Mediafileinfo> list = query.list();
		ConverDirector director = new ConverDirector();
		director.startConverter(m_task, list);
		
		/////////////////
		//for test
	//	ConverDirector director = new ConverDirector();
	//	director.test(m_task.getFilename(), m_task.getFilepath(), m_task.getFilename()+ ".mp4", "d:\\2\\1", 9, true);
		//////////
		
		m_issuccess = true;
		m_caller.ConverterFinishCallBack(this);
	}
}
