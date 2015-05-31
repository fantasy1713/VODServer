package com.video.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.media.pipeline.MediaPipelineClass;
import com.media.pipeline.MediaPipelineTaskClass;

public class UploadManager extends MediaPipelineClass {
	

	@Override
	protected boolean addPipeTask(MediaPipelineTaskClass task) {
		if(!task.getIslocalupload()){
			//���Ǳ����ϴ�����ˮ��ֹ
			return false;
		}
		try{
			File file = task.getUploadfile();
			String filename = task.getFilename();
			String savepath = task.getFilepath();
			FileOutputStream fos = new FileOutputStream(savepath + "/" + filename);
			FileInputStream fis = new FileInputStream(file);
			byte [] buffer = new byte[4096];
			int len=0;
			while((len= fis.read(buffer))>0){
				System.out.println(len);
				fos.write(buffer,0,len);
			}
			fis.close();fos.close();
			/*//��localmediaprototype ���в�����Ӧ�ֶ�
			this.insertNewLocalPrototype(task);*/
			//task���
			this.taskFinished(false, task);
			
			
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	private void insertNewLocalPrototype(MediaPipelineTaskClass task){
		
	}

}
