package com.vodserver.hibernate.beans;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Mediagroup entity. @author MyEclipse Persistence Tools
 */

public class Mediagroup implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Timestamp createtime;
	private String owner;
	private Set vodmedias = new HashSet(0);

	// Constructors

	/** default constructor */
	public Mediagroup() {
	}

	/** minimal constructor */
	public Mediagroup(String name) {
		this.name = name;
	}

	/** full constructor */
	public Mediagroup(String name, Timestamp createtime, String owner,
			Set vodmedias) {
		this.name = name;
		this.createtime = createtime;
		this.owner = owner;
		this.vodmedias = vodmedias;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set getVodmedias() {
		return this.vodmedias;
	}

	public void setVodmedias(Set vodmedias) {
		this.vodmedias = vodmedias;
	}

}