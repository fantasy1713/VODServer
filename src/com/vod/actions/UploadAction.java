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

}
