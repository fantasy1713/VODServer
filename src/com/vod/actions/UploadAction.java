package com.vod.actions;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

public class UploadAction extends ActionSupport {
	private String uploadContentType;
	private String uploadFileName;
	private File upload;
	
	public String uploadvideoAction(){
		System.out.println("func uploadvideoAction");
		
		return SUCCESS;
	}

}
