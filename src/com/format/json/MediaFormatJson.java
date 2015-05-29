package com.format.json;

public class MediaFormatJson implements JsonResponseInterface{
	private String filename;
	private int nb_streams;
	private int nb_programs;
	private String format_name;
	private String format_long_name;
	private String start_time;
	private String duration;
	private String size;
	private long bit_rate;
	private int probe_score;
	private MediaTagsJson tags;
	public String getFilename() {
		return filename;
	}
	public int getNb_streams() {
		return nb_streams;
	}
	public int getNb_programs() {
		return nb_programs;
	}
	public String getFormat_name() {
		return format_name;
	}
	public String getFormat_long_name() {
		return format_long_name;
	}
	public String getStart_time() {
		return start_time;
	}
	public String getDuration() {
		return duration;
	}
	public String getSize() {
		return size;
	}
	public long getBit_rate() {
		return bit_rate;
	}
	public int getProbe_score() {
		return probe_score;
	}
	public MediaTagsJson getTags() {
		return tags;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setNb_streams(int nb_streams) {
		this.nb_streams = nb_streams;
	}
	public void setNb_programs(int nb_programs) {
		this.nb_programs = nb_programs;
	}
	public void setFormat_name(String format_name) {
		this.format_name = format_name;
	}
	public void setFormat_long_name(String format_long_name) {
		this.format_long_name = format_long_name;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setBit_rate(long bit_rate) {
		this.bit_rate = bit_rate;
	}
	public void setProbe_score(int probe_score) {
		this.probe_score = probe_score;
	}
	public void setTags(MediaTagsJson tags) {
		this.tags = tags;
	}
}
