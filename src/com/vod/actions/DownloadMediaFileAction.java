package com.vod.actions;

import java.io.IOException;
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
			this.setErrorResponse("缺少参数", response);
			return;
		}
		
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			int idnum = 0;
			idnum = Integer.parseInt(this.id);
			DownloadMediaFileHandler handler = new DownloadMediaFileHandler();
			if(handler.checkFileRealPath(idnum, this.seg) == true)
				request.getRequestDispatcher(handler.getFilePath()).forward(request, response);
			else
				this.setErrorResponse(handler.getError(), response);
		} catch (ServletException e1) {
	//		e1.printStackTrace();
		} catch (IOException e1) {
	//		e1.printStackTrace();
		} catch (NumberFormatException e1){
			this.setErrorResponse("目标id格式错误", response);
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
