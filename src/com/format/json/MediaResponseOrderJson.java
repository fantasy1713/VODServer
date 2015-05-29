package com.format.json;

import java.util.List;

public class MediaResponseOrderJson implements JsonResponseInterface{
	private int orderindex;
	private int durations;
	private List<MediaResponseSegmentJson> segments;
	public int getOrderindex() {
		return orderindex;
	}
	public int getDurations() {
		return durations;
	}
	public void setOrderindex(int orderindex) {
		this.orderindex = orderindex;
	}
	public void setDurations(int durations) {
		this.durations = durations;
	}
	public int getSegmentNum(){
		if(segments == null)
			return 0;
		else
			return segments.size();
	}
	public List<MediaResponseSegmentJson> getSegments() {
		return segments;
	}
	public void setSegments(List<MediaResponseSegmentJson> segments) {
		this.segments = segments;
	}
}
