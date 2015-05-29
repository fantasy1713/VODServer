package com.video.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UploadVideoHandler {
	private File file;
	private String contentType;
	private String fileName;
	public UploadVideoHandler(File file, String contentType, String fileName) {
		this.file = file;
		this.contentType = contentType;
		this.fileName = fileName;
	}
	
	public boolean upload(){
		try{
			VideoManager manager = new VideoManager();
			manager.createMediaUpload(file,contentType,fileName);
			
			return true ;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
