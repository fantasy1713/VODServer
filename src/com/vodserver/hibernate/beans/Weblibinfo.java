package com.vodserver.hibernate.beans;

/**
 * Weblibinfo entity. @author MyEclipse Persistence Tools
 */

public class Weblibinfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String host;
	private String username;
	private String password;

	// Constructors

	/** default constructor */
	public Weblibinfo() {
	}

	/** full constructor */
	public Weblibinfo(String host, String username, String password) {
		this.host = host;
		this.username = username;
		this.password = password;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}