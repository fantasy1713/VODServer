package com.vodserver.hibernate.beans;

/**
 * Vodmediaparams entity. @author MyEclipse Persistence Tools
 */

public class Vodmediaparams implements java.io.Serializable {

	// Fields

	private VodmediaparamsId id;
	private Mediafileprototype mediafileprototype;
	private String weblibhost;
	private Integer weblibid;
	private String weblibhash;
	private String orignalurl;
	private Integer starttimeoffset;

	// Constructors

	/** default constructor */
	public Vodmediaparams() {
	}

	/** minimal constructor */
	public Vodmediaparams(VodmediaparamsId id) {
		this.id = id;
	}

	/** full constructor */
	public Vodmediaparams(VodmediaparamsId id,
			Mediafileprototype mediafileprototype, String weblibhost,
			Integer weblibid, String weblibhash, String orignalurl,
			Integer starttimeoffset) {
		this.id = id;
		this.mediafileprototype = mediafileprototype;
		this.weblibhost = weblibhost;
		this.weblibid = weblibid;
		this.weblibhash = weblibhash;
		this.orignalurl = orignalurl;
		this.starttimeoffset = starttimeoffset;
	}

	// Property accessors

	public VodmediaparamsId getId() {
		return this.id;
	}

	public void setId(VodmediaparamsId id) {
		this.id = id;
	}

	public Mediafileprototype getMediafileprototype() {
		return this.mediafileprototype;
	}

	public void setMediafileprototype(Mediafileprototype mediafileprototype) {
		this.mediafileprototype = mediafileprototype;
	}

	public String getWeblibhost() {
		return this.weblibhost;
	}

	public void setWeblibhost(String weblibhost) {
		this.weblibhost = weblibhost;
	}

	public Integer getWeblibid() {
		return this.weblibid;
	}

	public void setWeblibid(Integer weblibid) {
		this.weblibid = weblibid;
	}

	public String getWeblibhash() {
		return this.weblibhash;
	}

	public void setWeblibhash(String weblibhash) {
		this.weblibhash = weblibhash;
	}

	public String getOrignalurl() {
		return this.orignalurl;
	}

	public void setOrignalurl(String orignalurl) {
		this.orignalurl = orignalurl;
	}

	public Integer getStarttimeoffset() {
		return this.starttimeoffset;
	}

	public void setStarttimeoffset(Integer starttimeoffset) {
		this.starttimeoffset = starttimeoffset;
	}

}