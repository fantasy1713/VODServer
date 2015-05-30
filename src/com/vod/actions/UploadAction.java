package com.vod.actions;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;
import com.video.manager.UploadVideoHandler;

public class UploadAction extends ActionSupport {
	private String uploadContentType;
	private String uploadFileName;
	private File upload;

	public String uploadvideoAction() {
		System.out.println("func uploadvideoAction");

		UploadVideoHandler handler = new UploadVideoHandler(upload,
				uploadContentType, uploadFileName);
		if (handler.upload())
			return SUCCESS;
		else
			return ERROR;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}
	

}
