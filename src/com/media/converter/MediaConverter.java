package com.media.converter;

import java.util.ArrayList;
import java.util.List;

import com.media.pipeline.MediaPipelineClass;
import com.media.pipeline.MediaPipelineTaskClass;

public class MediaConverter extends MediaPipelineClass implements VideoConverterInterface{
	private List<ConverVideo> m_tasklist;
	private List<ConverVideo> m_runninglist;
	private final int m_maxthreadnum = 6;
	
	/*
	public static void main(String []args){
		MediaConverter mv = new MediaConverter();
		MediaPipelineTaskClass task1 = new MediaPipelineTaskClass();
		MediaPipelineTaskClass task2 = new MediaPipelineTaskClass();
		MediaPipelineTaskClass task3 = new MediaPipelineTaskClass();
		MediaPipelineTaskClass task4 = new MediaPipelineTaskClass();
		MediaPipelineTaskClass task5 = new MediaPipelineTaskClass();
		MediaPipelineTaskClass task6 = new MediaPipelineTaskClass();
		
		task1.setFilename("1.mp4");
		task2.setFilename("2.webm");
		task3.setFilename("3.rmvb");
		task4.setFilename("4.ogg");
		task5.setFilename("5.avi");
		task6.setFilename("6.ts");
		
		task1.setFilepath("d:\\test");
		task2.setFilepath("d:\\test");
		task3.setFilepath("d:\\test");
		task4.setFilepath("d:\\test");
		task5.setFilepath("d:\\test");
		task6.setFilepath("d:\\test");
		
		task1.setSha1value("1");
		task2.setSha1value("2");
		task3.setSha1value("3");
		task4.setSha1value("4");
		task5.setSha1value("5");
		task6.setSha1value("6");
		
		mv.addPipeTask(task1);
		mv.addPipeTask(task2);
		mv.addPipeTask(task3);
		mv.addPipeTask(task4);
		mv.addPipeTask(task5);
		mv.addPipeTask(task6);
	}*/
	
	public MediaConverter(){
		m_tasklist = new ArrayList<ConverVideo>();
		m_runninglist = new ArrayList<ConverVideo>();
	}
	
	@Override
	protected boolean addPipeTask(MediaPipelineTaskClass task) {
		if(task.getSha1value() == null)
			return false;
		
		for(ConverVideo cv : m_tasklist){
			if(cv.getTask().getSha1value().equals(task.getSha1value()))
				return true;
		}
		
		ConverVideo cv = new ConverVideo(this, task);
		m_tasklist.add(cv);
		checkStartNextConvert();
		return true;
	}
	
	private synchronized void checkStartNextConvert(){
		if(m_runninglist.size() < m_maxthreadnum){
			for(int i=0; i<m_tasklist.size(); i++){
				ConverVideo cv = m_tasklist.get(i);
				if(cv.isStop() == true){
					cv.setStart();
					Thread cvthread = new Thread(cv);
					cvthread.start();
					m_runninglist.add(cv);
					
					if(m_runninglist.size() >= m_maxthreadnum)
						return;
				}
			}
		}
	}

	@Override
	public void ConverterFinishCallBack(ConverVideo cv) {
		MediaPipelineTaskClass task = cv.getTask();
		if(cv.isSuccess()){
			System.out.println("Encoding Finished");
			this.taskFinished(false, task);
		}
		else{
			System.out.println("Encoding Failed");
			this.taskFinished(true, task);
		}

		m_tasklist.remove(cv);
		m_runninglist.remove(cv);
		checkStartNextConvert();
	}
}
