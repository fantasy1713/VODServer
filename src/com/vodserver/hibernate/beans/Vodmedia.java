package com.vodserver.hibernate.beans;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Vodmedia entity. @author MyEclipse Persistence Tools
 */

public class Vodmedia implements java.io.Serializable {

	// Fields

	private Integer id;
	private Mediagroup mediagroup;
	private String urlstring;
	private Integer views;
	private Float durations;
	private Long mediasize;
	private String status;
	private String owner;
	private String ownersystem;
	private Timestamp createtime;
	private Integer requesttimes;
	private Timestamp lastvisit;
	private Integer priority;
	private Timestamp deletetime;
	private String title;
	private Timestamp updatetime;
	private Integer updatetimes;
	private Set vodmediaparamses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Vodmedia() {
	}

	/** minimal constructor */
	public Vodmedia(String urlstring) {
		this.urlstring = urlstring;
	}

	/** full constructor */
	public Vodmedia(Mediagroup mediagroup, String urlstring, Integer views,
			Float durations, Long mediasize, String status, String owner,
			String ownersystem, Timestamp createtime, Integer requesttimes,
			Timestamp lastvisit, Integer priority, Timestamp deletetime,
			String title, Timestamp updatetime, Integer updatetimes,
			Set vodmediaparamses) {
		this.mediagroup = mediagroup;
		this.urlstring = urlstring;
		this.views = views;
		this.durations = durations;
		this.mediasize = mediasize;
		this.status = status;
		this.owner = owner;
		this.ownersystem = ownersystem;
		this.createtime = createtime;
		this.requesttimes = requesttimes;
		this.lastvisit = lastvisit;
		this.priority = priority;
		this.deletetime = deletetime;
		this.title = title;
		this.updatetime = updatetime;
		this.updatetimes = updatetimes;
		this.vodmediaparamses = vodmediaparamses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Mediagroup getMediagroup() {
		return this.mediagroup;
	}

	public void setMediagroup(Mediagroup mediagroup) {
		this.mediagroup = mediagroup;
	}

	public String getUrlstring() {
		return this.urlstring;
	}

	public void setUrlstring(String urlstring) {
		this.urlstring = urlstring;
	}

	public Integer getViews() {
		return this.views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Float getDurations() {
		return this.durations;
	}

	public void setDurations(Float durations) {
		this.durations = durations;
	}

	public Long getMediasize() {
		return this.mediasize;
	}

	public void setMediasize(Long mediasize) {
		this.mediasize = mediasize;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnersystem() {
		return this.ownersystem;
	}

	public void setOwnersystem(String ownersystem) {
		this.ownersystem = ownersystem;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Integer getRequesttimes() {
		return this.requesttimes;
	}

	public void setRequesttimes(Integer requesttimes) {
		this.requesttimes = requesttimes;
	}

	public Timestamp getLastvisit() {
		return this.lastvisit;
	}

	public void setLastvisit(Timestamp lastvisit) {
		this.lastvisit = lastvisit;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Timestamp getDeletetime() {
		return this.deletetime;
	}

	public void setDeletetime(Timestamp deletetime) {
		this.deletetime = deletetime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Timestamp getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getUpdatetimes() {
		return this.updatetimes;
	}

	public void setUpdatetimes(Integer updatetimes) {
		this.updatetimes = updatetimes;
	}

	public Set getVodmediaparamses() {
		return this.vodmediaparamses;
	}

	public void setVodmediaparamses(Set vodmediaparamses) {
		this.vodmediaparamses = vodmediaparamses;
	}

}