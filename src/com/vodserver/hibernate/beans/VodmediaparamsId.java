package com.vodserver.hibernate.beans;

/**
 * VodmediaparamsId entity. @author MyEclipse Persistence Tools
 */

public class VodmediaparamsId implements java.io.Serializable {

	// Fields

	private Vodmedia vodmedia;
	private Integer viewindex;
	private Integer orderindex;

	// Constructors

	/** default constructor */
	public VodmediaparamsId() {
	}

	/** full constructor */
	public VodmediaparamsId(Vodmedia vodmedia, Integer viewindex,
			Integer orderindex) {
		this.vodmedia = vodmedia;
		this.viewindex = viewindex;
		this.orderindex = orderindex;
	}

	// Property accessors

	public Vodmedia getVodmedia() {
		return this.vodmedia;
	}

	public void setVodmedia(Vodmedia vodmedia) {
		this.vodmedia = vodmedia;
	}

	public Integer getViewindex() {
		return this.viewindex;
	}

	public void setViewindex(Integer viewindex) {
		this.viewindex = viewindex;
	}

	public Integer getOrderindex() {
		return this.orderindex;
	}

	public void setOrderindex(Integer orderindex) {
		this.orderindex = orderindex;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VodmediaparamsId))
			return false;
		VodmediaparamsId castOther = (VodmediaparamsId) other;

		return ((this.getVodmedia() == castOther.getVodmedia()) || (this
				.getVodmedia() != null && castOther.getVodmedia() != null && this
				.getVodmedia().equals(castOther.getVodmedia())))
				&& ((this.getViewindex() == castOther.getViewindex()) || (this
						.getViewindex() != null
						&& castOther.getViewindex() != null && this
						.getViewindex().equals(castOther.getViewindex())))
				&& ((this.getOrderindex() == castOther.getOrderindex()) || (this
						.getOrderindex() != null
						&& castOther.getOrderindex() != null && this
						.getOrderindex().equals(castOther.getOrderindex())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getVodmedia() == null ? 0 : this.getVodmedia().hashCode());
		result = 37 * result
				+ (getViewindex() == null ? 0 : this.getViewindex().hashCode());
		result = 37
				* result
				+ (getOrderindex() == null ? 0 : this.getOrderindex()
						.hashCode());
		return result;
	}

}