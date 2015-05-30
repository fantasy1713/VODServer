package com.vod.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.format.json.ErrorInfoFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.video.manager.DownloadMediaFileHandler;

public class DownloadMediaFileAction extends ActionSupport{
	private static final long serialVersionUID = -6066278157279798906L;
	private String id;
	private String seg;

	public void setId(String id) {
		this.id = id;
	}
	
	public void setSeg(String seg){
		this.seg = seg;
	}
	
	public void DownloadAction(){
		HttpServletResponse response = ServletActionContext.getResponse();

	//	ServerSettings.getDashFilePath();

		if(this.id == null){
			this.setErrorResponse("ȱ�ٲ���", response);
			return;
		}
		
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			int idnum = 0;
			idnum = Integer.parseInt(this.id);
			DownloadMediaFileHandler handler = new DownloadMediaFileHandler();
			if(handler.checkFileRealPath(idnum, this.seg) == true){
				//request.getRequestDispatcher(handler.getFilePath()).forward(request, response);
				String path = handler.getFilePath();
				File file = new File(path);
				OutputStream os = response.getOutputStream();
				if(file.exists()){
					FileInputStream fis = new FileInputStream(file);
					byte [] buf = new byte[4096];
					int len=0;
					while((len = fis.read(buf))>0){
						os.write(buf, 0, len);
					}
				}
			}
			else
				this.setErrorResponse(handler.getError(), response);
		} catch (IOException e1) {
	//		e1.printStackTrace();
		} catch (NumberFormatException e1){
			this.setErrorResponse("Ŀ��id��ʽ����", response);
			return;
		}
	}
	
	private void setErrorResponse(String info, HttpServletResponse response){
		try {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			OutputStreamWriter bw = new OutputStreamWriter(response.getOutputStream());
			JSONObject obj = JSONObject.fromObject(ErrorInfoFactory.requestError(info));
			bw.write(obj.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
