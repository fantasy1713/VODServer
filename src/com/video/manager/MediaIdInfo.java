package com.video.manager;

public class MediaIdInfo{
	private boolean available;
	private String content;
	
	public boolean isAvailable() {
		return available;
	}
	public String getContent() {
		return content;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public void setContent(String content) {
		this.content = content;
	}
}