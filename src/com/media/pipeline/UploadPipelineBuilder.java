package com.media.pipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.global.defines.MediaFileStatusDefine;
import com.media.converter.MediaConverter;
import com.media.hash.VideoHasher;
import com.video.manager.UploadManager;
import com.video.manager.VideoManager;

/**
 * @author zhangfan
 *
 */
public class UploadPipelineBuilder  extends MediaPipelineClass {
	
	private static UploadPipelineBuilder g_instance = new UploadPipelineBuilder();
	private List<MediaPipelineClass> m_pipeline;
	private List<MediaPipelineTaskClass> m_tasks;
	private VideoManager m_manager; 
	
	private UploadPipelineBuilder(){
		m_pipeline = new ArrayList<MediaPipelineClass>();
		m_tasks = new ArrayList<MediaPipelineTaskClass>();
		m_manager = new VideoManager();

		this.addPipeline(new UploadManager());
		this.addPipeline(new VideoHasher());
		this.addPipeline(new MediaConverter());
	}
	private void addPipeline(MediaPipelineClass worker){
		if(worker == null)
			return;
		
		m_pipeline.add(worker);
		//流水线最后一站将返回到自己
		worker.setNextPipe(this);
		if(m_pipeline.size() > 1){
			m_pipeline.get(m_pipeline.size() - 2).setNextPipe(m_pipeline.get(m_pipeline.size() - 1));
		}
	}


	/**
	 * @author zhangfan
	 * 添加视频上传流水线任务
	 * @param file
	 * @param contentType
	 * @param fileName
	 * @param uploadPath
	 * @param UUID
	 * @return
	 */
	public static boolean addUploadTask(File file, String contentType,
			String fileName, String uploadPath,int prototypeid,int mediaid, String UUID) {
		MediaPipelineTaskClass task = new MediaPipelineTaskClass();
//		task.setFilename(fileName);
		task.setFilename(UUID);
		task.setRealfilename(fileName);
		task.setFilepath(uploadPath);
		
		task.setPrototypeid(prototypeid);
		task.setMediaid(mediaid);
		task.setStatus(MediaFileStatusDefine.CREATED);
		task.setIslocalupload(true);
		task.setUploadfile(file);
		task.setContentType(contentType);
		
		
		return g_instance.startTask(task);
	}
	private boolean startTask(MediaPipelineTaskClass task){
		if(m_pipeline.size() == 0)
			return false;
		m_tasks.add(task);
		return m_pipeline.get(0).addPipeTask(task);
	}
	@Override
	protected boolean addPipeTask(MediaPipelineTaskClass task) {
		// 对于流水线本身而言，收到来自流水线的任务则表示该任务已结束
		System.out.println("task finished at state:" + task.getStatus());
		return this.taskFinished(task);
	}
	
	@Override
	protected void taskTruncate(MediaPipelineTaskClass task){
		//由于是流水线最后一站，需要重写任务截断方法
		System.out.println("task truncate at state:" + task.getStatus());
		this.taskFinished(task);
	}
	private boolean taskFinished(MediaPipelineTaskClass task){
		m_tasks.remove(task);
		if(!this.checkMediaTask(task.getMediaid()))
			m_manager.refreshMediaStatus(task.getMediaid(), "ready");
		return true;
	}
	private boolean checkMediaTask(int mediaid){
		for(MediaPipelineTaskClass task : m_tasks){
			if(task.getMediaid() == mediaid)
				return true;
		}
		return false;
	}

}
