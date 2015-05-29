package com.format.json;

public class MediaTagsJson implements JsonResponseInterface{
	private String major_brand;
	private String minor_version;
	private String compatible_brands;
	private String encoder;
	public String getMajor_brand() {
		return major_brand;
	}
	public String getMinor_version() {
		return minor_version;
	}
	public String getCompatible_brands() {
		return compatible_brands;
	}
	public String getEncoder() {
		return encoder;
	}
	public void setMajor_brand(String major_brand) {
		this.major_brand = major_brand;
	}
	public void setMinor_version(String minor_version) {
		this.minor_version = minor_version;
	}
	public void setCompatible_brands(String compatible_brands) {
		this.compatible_brands = compatible_brands;
	}
	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}
}
