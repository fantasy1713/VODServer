package com.vod.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.video.manager.VideoResponseHandler;

public class VideoResponseAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mediaid;
	private String id;//封装playback请求的参数
	
	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String VodResponseAction() {
		if(mediaid == null)
		{
			return ERROR;
		}
		
		try{
			VideoResponseHandler handler = new VideoResponseHandler();
			String params = handler.getMediaContent(mediaid);
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("mediainfo", params);
		}
		catch(Exception e){
			return ERROR;
		}
	
		return SUCCESS;
	}
	
	/**
	 * @author zhangfan
	 * 把查询到的本地缓存播放
	 * @return
	 */
	public String playbackAction(){
		if(id == null)
		{
			return ERROR;
		}
		
		try{
			
			VideoResponseHandler handler = new VideoResponseHandler();
			
			String attrCode = handler.getAttrCode(id);//获得缓存对应的点播特征码
			if(attrCode==null){
				return ERROR;
			}
			else{
				this.mediaid = attrCode;
				
			}
			
			/*String params = handler.getMediaContent(attrCode);
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("mediainfo", params);*/
		}
		catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
	
		return SUCCESS;
	}
	/**
	 * @author zhangfan
	 * 作为组件时，第三方平台异步请求播放
	 * 返回json
	 */
	public void ajaxplaybackAction(){
		if(id == null)
		{
			return ;
		}
		
		try{
			VideoResponseHandler handler = new VideoResponseHandler();
			
			String attrCode = handler.getAttrCode(id);//获得缓存对应的点播特征码
			if(attrCode==null){
				return ;
			}
			else{
				this.mediaid = attrCode;
				String params = handler.getMediaContent(mediaid);
				HttpServletResponse response = ServletActionContext.getResponse();
				PrintWriter writer = response.getWriter();
				writer.write(params);
				writer.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return ;
		}
	}
}
