package com.vod.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.format.json.VideoPostRequestJson;
import com.opensymphony.xwork2.ActionSupport;
import com.video.manager.MediaIdInfo;
import com.video.manager.VideoRequestHandler;

public class VideoRequestAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] urls;
	private String errorinfo;
	private String mediaid;
	private String mediainfo;
	
	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}

	public String getMediaid() {
		return mediaid;
	}

	public String VodReqeuestAction() {
		if(urls == null && mediainfo == null){
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("errorinfo", "²ÎÊýÎª¿Õ");
			return ERROR;
		}
		
		VideoRequestHandler test2 = new VideoRequestHandler();
		String method = ServletActionContext.getRequest().getMethod();
		if(method.equals("POST")){
			JSONArray array = JSONArray.fromObject(mediainfo);
			List<VideoPostRequestJson> itemlist = JSONArray.toList(array, VideoPostRequestJson.class);
			test2.setJsonList(itemlist);
		}
		else if(method.equals("GET")){
			test2.setUrlList(urls);
		}
		
		try{
			MediaIdInfo info = test2.getMediaId();
			if(info.isAvailable() == true)
				this.mediaid = test2.getMediaId().getContent();
			else{
				errorinfo = info.getContent();
				HttpServletRequest request = ServletActionContext.getRequest();
				request.setAttribute("errorinfo", errorinfo);
				return ERROR;
			}
		}
		catch(Exception e){
			errorinfo = e.getMessage();
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("errorinfo", errorinfo);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public static void main(String []args){
		String test = "[{\"host\":\"weblib.scn.cn\",\"id\":123,\"view\":1,\"order\":3,\"offset\":999},{\"host\":\"weblib2.scn.cn\",\"id\":111,\"view\":1,\"order\":3,\"offset\":999}]";
		JSONArray array = JSONArray.fromObject(test);
		List<VideoPostRequestJson> itemlist = JSONArray.toList(array, VideoPostRequestJson.class);
		System.out.println(itemlist.get(0).getHost());
	}
}
