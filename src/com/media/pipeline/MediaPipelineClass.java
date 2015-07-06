package com.media.pipeline;

import org.apache.log4j.Logger;

public abstract class MediaPipelineClass {
	private MediaPipelineClass m_nextpipe;

	private void taskOperate(boolean istruncate, MediaPipelineTaskClass task){
		if(istruncate == true)
			this.taskTruncate(task);
		else
			this.addPipeTask(task);
	}
	
	public void setNextPipe(MediaPipelineClass pipe){
		m_nextpipe = pipe;
	}
	
	protected abstract boolean addPipeTask(MediaPipelineTaskClass task);
	protected void taskTruncate(MediaPipelineTaskClass task){
		//Ĭ���յ��ض�����ʱֱ������������
		this.taskFinished(true, task);
	}
	protected void taskFinished(boolean istruncate, MediaPipelineTaskClass task){
		if(m_nextpipe != null){
			Logger logger = Logger.getLogger("pay-log");
			logger.info("����"+m_nextpipe.getClass()+"��ִ�нڵ㹤��");
			m_nextpipe.taskOperate(istruncate, task);
		}
	}
}
