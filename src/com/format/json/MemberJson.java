package com.format.json;

public class MemberJson implements JsonResponseInterface{
	private int id;
	private String name;
	private String signature;
	private String icon;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSignature() {
		return signature;
	}
	public String getIcon() {
		return icon;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}