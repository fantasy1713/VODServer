package com.format.json;

public class ErrorInfoJson implements JsonResponseInterface{
	private String type;
	private int code;
	private String detail;
	
	public ErrorInfoJson(int code, String type, String detail){
		this.code = code;
		this.type = type;
		this.detail = detail;
	}
	
	public String getType() {
		return type;
	}
	public int getCode() {
		return code;
	}
	public String getDetail() {
		return detail;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
