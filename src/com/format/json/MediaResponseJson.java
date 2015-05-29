package com.format.json;

import java.util.List;

public class MediaResponseJson implements JsonResponseInterface{
	private int id;
	private String idstring;
	private Float durations;
	private String owner;
	private String status;
	private String title;
	private int viewnum;
	private List<MediaResponseViewJson> views;
	
	public int getId() {
		return id;
	}

	public String getIdstring() {
		return idstring;
	}

	public Float getDurations() {
		return durations;
	}

	public String getOwner() {
		return owner;
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}
	
	public void setViewnum(int viewnum){
		this.viewnum = viewnum;
	}

	public int getViewnum() {
		return viewnum;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdstring(String idstring) {
		this.idstring = idstring;
	}

	public void setDurations(Float durations) {
		this.durations = durations;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<MediaResponseViewJson> getViews() {
		return views;
	}

	public void setViews(List<MediaResponseViewJson> views) {
		this.views = views;
	}
}
