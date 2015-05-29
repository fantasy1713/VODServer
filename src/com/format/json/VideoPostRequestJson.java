package com.format.json;

public class VideoPostRequestJson {
	private String host;
	private int id;
	private int view;
	private int order;
	private int toffset;
	public String getHost() {
		return host;
	}
	public int getId() {
		return id;
	}
	public int getView() {
		return view;
	}
	public int getOrder() {
		return order;
	}
	public int getToffset() {
		return toffset;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setView(int view) {
		this.view = view;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setToffset(int toffset) {
		this.toffset = toffset;
	}
}
