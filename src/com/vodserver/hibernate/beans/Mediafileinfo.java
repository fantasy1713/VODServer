package com.vodserver.hibernate.beans;

import java.sql.Timestamp;

/**
 * Mediafileinfo entity. @author MyEclipse Persistence Tools
 */

public class Mediafileinfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private Mediafileprototype mediafileprototype;
	private Localmediafileprototype localmediafileprototype;
	private String filetype;
	private Boolean isdeleted;
	private String status;
	private Integer requesttimes;
	private Timestamp deletetime;
	private String videocodes;
	private String audiocodes;
	private String bitrate;
	private Float framerate;
	private Integer width;
	private Integer height;
	private String ratio;
	private Float durations;
	private String resolution;
	private Long size;
	private String filepath;
	private String filename;
	private String realfilename;

	// Constructors

	/** default constructor */
	public Mediafileinfo() {
	}

	/** full constructor */
	public Mediafileinfo(Integer id, Mediafileprototype mediafileprototype,
			Localmediafileprototype localmediafileprototype, String filetype,
			Boolean isdeleted, String status, Integer requesttimes,
			Timestamp deletetime, String videocodes, String audiocodes,
			String bitrate, Float framerate, Integer width, Integer height,
			String ratio, Float durations, String resolution, Long size,
			String filepath, String filename) {
		super();
		this.id = id;
		this.mediafileprototype = mediafileprototype;
		this.localmediafileprototype = localmediafileprototype;
		this.filetype = filetype;
		this.isdeleted = isdeleted;
		this.status = status;
		this.requesttimes = requesttimes;
		this.deletetime = deletetime;
		this.videocodes = videocodes;
		this.audiocodes = audiocodes;
		this.bitrate = bitrate;
		this.framerate = framerate;
		this.width = width;
		this.height = height;
		this.ratio = ratio;
		this.durations = durations;
		this.resolution = resolution;
		this.size = size;
		this.filepath = filepath;
		this.filename = filename;
	}


	// Property accessors

	public Integer getId() {
		return this.id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	public Mediafileprototype getMediafileprototype() {
		return this.mediafileprototype;
	}

	public void setMediafileprototype(Mediafileprototype mediafileprototype) {
		this.mediafileprototype = mediafileprototype;
	}

	public Localmediafileprototype getLocalmediafileprototype() {
		return localmediafileprototype;
	}

	public void setLocalmediafileprototype(
			Localmediafileprototype localmediafileprototype) {
		this.localmediafileprototype = localmediafileprototype;
	}

	public String getFiletype() {
		return this.filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public Boolean getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(Boolean isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getRequesttimes() {
		return this.requesttimes;
	}

	public void setRequesttimes(Integer requesttimes) {
		this.requesttimes = requesttimes;
	}

	public Timestamp getDeletetime() {
		return this.deletetime;
	}

	public void setDeletetime(Timestamp deletetime) {
		this.deletetime = deletetime;
	}

	public String getVideocodes() {
		return this.videocodes;
	}

	public void setVideocodes(String videocodes) {
		this.videocodes = videocodes;
	}

	public String getAudiocodes() {
		return this.audiocodes;
	}

	public void setAudiocodes(String audiocodes) {
		this.audiocodes = audiocodes;
	}

	public String getBitrate() {
		return this.bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public Float getFramerate() {
		return this.framerate;
	}

	public void setFramerate(Float framerate) {
		this.framerate = framerate;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getRatio() {
		return this.ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public Float getDurations() {
		return this.durations;
	}

	public void setDurations(Float durations) {
		this.durations = durations;
	}

	public String getResolution() {
		return this.resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRealfilename() {
		return realfilename;
	}

	public void setRealfilename(String realfilename) {
		this.realfilename = realfilename;
	}
	

}