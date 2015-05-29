package com.format.json;

public class MediaResponseSegmentJson implements JsonResponseInterface{
	private String type;
	private String status;
	private String bitrate;
	private Float framerate;
	private Float durations;
	private int width;
	private int height;
	private String videocodes;
	private String audiocodes;
	private Long size;
	private int id;

	public String getVideocodes() {
		return videocodes;
	}
	public String getAudiocodes() {
		return audiocodes;
	}
	public void setVideocodes(String videocodes) {
		this.videocodes = videocodes;
	}
	public void setAudiocodes(String audiocodes) {
		this.audiocodes = audiocodes;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getType() {
		return type;
	}
	public String getStatus() {
		return status;
	}
	public String getBitrate() {
		return bitrate;
	}
	public Float getFramerate() {
		return framerate;
	}
	public Float getDurations() {
		return durations;
	}
	public Long getSize() {
		return size;
	}
	public int getId() {
		return id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}
	public void setFramerate(Float framerate) {
		this.framerate = framerate;
	}
	public void setDurations(Float durations) {
		this.durations = durations;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public void setId(int id) {
		this.id = id;
	}
}
