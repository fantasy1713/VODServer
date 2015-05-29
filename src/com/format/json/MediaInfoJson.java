package com.format.json;

public class MediaInfoJson implements JsonResponseInterface{
	private MediaFormatJson format;
	private MediaStreamJson streams[];
	public MediaFormatJson getFormat() {
		return format;
	}
	public MediaStreamJson[] getStreams() {
		return streams;
	}
	public void setFormat(MediaFormatJson format) {
		this.format = format;
	}
	public void setStreams(MediaStreamJson[] streams) {
		this.streams = streams;
	}
}
