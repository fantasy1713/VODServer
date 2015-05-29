package com.vodserver.hibernate.beans;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Mediafileprototype entity. @author MyEclipse Persistence Tools
 */

public class Mediafileprototype implements java.io.Serializable {

	// Fields

	private Integer id;
	private String sha1value;
	private String filename;
	private String srcweblibhost;
	private Integer srcweblibid;
	private Timestamp createtime;
	private Long size;
	private Set mediafileinfos = new HashSet(0);
	private Set vodmediaparamses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Mediafileprototype() {
	}

	/** full constructor */
	public Mediafileprototype(String sha1value, String filename,
			String srcweblibhost, Integer srcweblibid, Timestamp createtime,
			Long size, Set mediafileinfos, Set vodmediaparamses) {
		this.sha1value = sha1value;
		this.filename = filename;
		this.srcweblibhost = srcweblibhost;
		this.srcweblibid = srcweblibid;
		this.createtime = createtime;
		this.size = size;
		this.mediafileinfos = mediafileinfos;
		this.vodmediaparamses = vodmediaparamses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSha1value() {
		return this.sha1value;
	}

	public void setSha1value(String sha1value) {
		this.sha1value = sha1value;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSrcweblibhost() {
		return this.srcweblibhost;
	}

	public void setSrcweblibhost(String srcweblibhost) {
		this.srcweblibhost = srcweblibhost;
	}

	public Integer getSrcweblibid() {
		return this.srcweblibid;
	}

	public void setSrcweblibid(Integer srcweblibid) {
		this.srcweblibid = srcweblibid;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Set getMediafileinfos() {
		return this.mediafileinfos;
	}

	public void setMediafileinfos(Set mediafileinfos) {
		this.mediafileinfos = mediafileinfos;
	}

	public Set getVodmediaparamses() {
		return this.vodmediaparamses;
	}

	public void setVodmediaparamses(Set vodmediaparamses) {
		this.vodmediaparamses = vodmediaparamses;
	}

}