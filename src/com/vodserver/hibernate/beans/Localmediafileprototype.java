package com.vodserver.hibernate.beans;

import com.sun.jmx.snmp.Timestamp;

/**
 * @author zhangfan
 *
 */
public class Localmediafileprototype {
 
	private Integer id;
	private String sha1value;
	private String filename;
	private Timestamp createtime;
	private Boolean isdeleted;
	private Long size;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSha1value() {
		return sha1value;
	}
	public void setSha1value(String sha1value) {
		this.sha1value = sha1value;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public Boolean getIsdeleted() {
		return isdeleted;
	}
	public void setIsdeleted(Boolean isdeleted) {
		this.isdeleted = isdeleted;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
}
