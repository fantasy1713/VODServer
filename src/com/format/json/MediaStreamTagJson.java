package com.format.json;

public class MediaStreamTagJson implements JsonResponseInterface{
	private String language;
	private String handler_name;
	public String getLanguage() {
		return language;
	}
	public String getHandler_name() {
		return handler_name;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public void setHandler_name(String handler_name) {
		this.handler_name = handler_name;
	}
}
