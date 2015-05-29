package com.format.json;

public class MediaStreamJson implements JsonResponseInterface{
	private int index;
	private String codec_name;
	private String codec_long_name;
	private String profile;
	private String codec_type;
	private String codec_time_base;
	private String codec_tag_string;
	private String codec_tag;
	private int width;
	private int height;
	private int has_b_frames;
	private String sample_aspect_ratio;
	private String display_aspect_ratio;
	private String sample_fmt;
	private String sample_rate;
	private int channels;
	private String channel_layout;
	private int bits_per_sample;
	private String avg_frame_rate;
	private String pix_fmt;
	private int level;
	private String r_frame_rate;
	private String time_base;
	private int start_pts;
	private String start_time;
	private int duration_ts;
	private String duration;
	private String bit_rate;
	private String nb_frames;
	private MediaDispositionJson disposition;
	private MediaStreamTagJson tags;
	public int getIndex() {
		return index;
	}
	public String getCodec_name() {
		return codec_name;
	}
	public String getCodec_long_name() {
		return codec_long_name;
	}
	public String getProfile() {
		return profile;
	}
	public String getCodec_type() {
		return codec_type;
	}
	public String getCodec_time_base() {
		return codec_time_base;
	}
	public String getCodec_tag_string() {
		return codec_tag_string;
	}
	public String getCodec_tag() {
		return codec_tag;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getHas_b_frames() {
		return has_b_frames;
	}
	public String getSample_aspect_ratio() {
		return sample_aspect_ratio;
	}
	public String getDisplay_aspect_ratio() {
		return display_aspect_ratio;
	}
	public String getSample_fmt() {
		return sample_fmt;
	}
	public String getSample_rate() {
		return sample_rate;
	}
	public int getChannels() {
		return channels;
	}
	public String getChannel_layout() {
		return channel_layout;
	}
	public int getBits_per_sample() {
		return bits_per_sample;
	}
	public String getAvg_frame_rate() {
		return avg_frame_rate;
	}
	public String getPix_fmt() {
		return pix_fmt;
	}
	public int getLevel() {
		return level;
	}
	public String getR_frame_rate() {
		return r_frame_rate;
	}
	public String getTime_base() {
		return time_base;
	}
	public int getStart_pts() {
		return start_pts;
	}
	public String getStart_time() {
		return start_time;
	}
	public int getDuration_ts() {
		return duration_ts;
	}
	public String getDuration() {
		return duration;
	}
	public String getBit_rate() {
		return bit_rate;
	}
	public String getNb_frames() {
		return nb_frames;
	}
	public MediaDispositionJson getDisposition() {
		return disposition;
	}
	public MediaStreamTagJson getTags() {
		return tags;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setCodec_name(String codec_name) {
		this.codec_name = codec_name;
	}
	public void setCodec_long_name(String codec_long_name) {
		this.codec_long_name = codec_long_name;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public void setCodec_type(String codec_type) {
		this.codec_type = codec_type;
	}
	public void setCodec_time_base(String codec_time_base) {
		this.codec_time_base = codec_time_base;
	}
	public void setCodec_tag_string(String codec_tag_string) {
		this.codec_tag_string = codec_tag_string;
	}
	public void setCodec_tag(String codec_tag) {
		this.codec_tag = codec_tag;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setHas_b_frames(int has_b_frames) {
		this.has_b_frames = has_b_frames;
	}
	public void setSample_aspect_ratio(String sample_aspect_ratio) {
		this.sample_aspect_ratio = sample_aspect_ratio;
	}
	public void setDisplay_aspect_ratio(String display_aspect_ratio) {
		this.display_aspect_ratio = display_aspect_ratio;
	}
	public void setSample_fmt(String sample_fmt) {
		this.sample_fmt = sample_fmt;
	}
	public void setSample_rate(String sample_rate) {
		this.sample_rate = sample_rate;
	}
	public void setChannels(int channels) {
		this.channels = channels;
	}
	public void setChannel_layout(String channel_layout) {
		this.channel_layout = channel_layout;
	}
	public void setBits_per_sample(int bits_per_sample) {
		this.bits_per_sample = bits_per_sample;
	}
	public void setAvg_frame_rate(String avg_frame_rate) {
		this.avg_frame_rate = avg_frame_rate;
	}
	public void setPix_fmt(String pix_fmt) {
		this.pix_fmt = pix_fmt;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setR_frame_rate(String r_frame_rate) {
		this.r_frame_rate = r_frame_rate;
	}
	public void setTime_base(String time_base) {
		this.time_base = time_base;
	}
	public void setStart_pts(int start_pts) {
		this.start_pts = start_pts;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public void setDuration_ts(int duration_ts) {
		this.duration_ts = duration_ts;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public void setBit_rate(String bit_rate) {
		this.bit_rate = bit_rate;
	}
	public void setNb_frames(String nb_frames) {
		this.nb_frames = nb_frames;
	}
	public void setDisposition(MediaDispositionJson disposition) {
		this.disposition = disposition;
	}
	public void setTags(MediaStreamTagJson tags) {
		this.tags = tags;
	}
}
