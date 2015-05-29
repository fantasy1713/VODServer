package com.media.pipeline;

public class MediaPipelineTaskClass {
	private int prototypeid;
	private int mediaid;
	private String filepath;
	private String filename;
	private String weblibhost;
	private int weblibid;
	private String sha1value;
	private String status;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPrototypeid() {
		return prototypeid;
	}
	public int getMediaid() {
		return mediaid;
	}
	public String getFilepath() {
		return filepath;
	}
	public String getFilename() {
		return filename;
	}
	public String getWeblibhost() {
		return weblibhost;
	}
	public int getWeblibid() {
		return weblibid;
	}
	public String getSha1value() {
		return sha1value;
	}
	public void setPrototypeid(int prototypeid) {
		this.prototypeid = prototypeid;
	}
	public void setMediaid(int mediaid) {
		this.mediaid = mediaid;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setWeblibhost(String weblibhost) {
		this.weblibhost = weblibhost;
	}
	public void setWeblibid(int weblibid) {
		this.weblibid = weblibid;
	}
	public void setSha1value(String sha1value) {
		this.sha1value = sha1value;
	}
}
