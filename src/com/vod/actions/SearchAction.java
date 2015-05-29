package com.vod.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.video.manager.VideoSearchHandler;
import com.vodserver.hibernate.beans.Mediafileinfo;

public class SearchAction extends ActionSupport {
	private SearchResult result;
	private VideoSearchHandler handler;
	
	public String getExistVideos(){
		System.out.println("func getExistVideos");
		try{
			handler = new VideoSearchHandler();
			List<Mediafileinfo> cachesList = handler.getExistCache();
			this.result =new SearchResult();
			if(cachesList!=null&&cachesList.size()>0){
				result.caches=cachesList;
				result.tip="FOUND";
			}
			else{
				result.tip="NO CACHE";
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;
	}

	
	
	public SearchResult getResult() {
		return result;
	}



	public void setResult(SearchResult result) {
		this.result = result;
	}



	public class SearchResult{
		List caches;
		String tip;
		public List getCaches() {
			return caches;
		}
		public void setCaches(List caches) {
			this.caches = caches;
		}
		public String getTip() {
			return tip;
		}
		public void setTip(String tip) {
			this.tip = tip;
		}
		
	}

}
