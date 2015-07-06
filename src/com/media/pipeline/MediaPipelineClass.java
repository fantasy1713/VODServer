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
		//默认收到截断任务时直接向下流传送
		this.taskFinished(true, task);
	}
	protected void taskFinished(boolean istruncate, MediaPipelineTaskClass task){
		if(m_nextpipe != null){
			Logger logger = Logger.getLogger("pay-log");
			logger.info("触发"+m_nextpipe.getClass()+"类执行节点工作");
			m_nextpipe.taskOperate(istruncate, task);
		}
	}
}
