package com.nsc.dem.bean.archives;

/**
 * TUserQueryId entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class TUserQueryId implements java.io.Serializable {

	// Fields

	private String loginId;
	private String queryName;
	private String queryKey;

	// Constructors

	/** default constructor */
	public TUserQueryId() {
	}

	/** full constructor */
	public TUserQueryId(String loginId, String queryName, String queryKey) {
		this.loginId = loginId;
		this.queryName = queryName;
		this.queryKey = queryKey;
	}

	// Property accessors

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getQueryName() {
		return this.queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getQueryKey() {
		return this.queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TUserQueryId))
			return false;
		TUserQueryId castOther = (TUserQueryId) other;

		return ((this.getLoginId() == castOther.getLoginId()) || (this
				.getLoginId() != null
				&& castOther.getLoginId() != null && this.getLoginId().equals(
				castOther.getLoginId())))
				&& ((this.getQueryName() == castOther.getQueryName()) || (this
						.getQueryName() != null
						&& castOther.getQueryName() != null && this
						.getQueryName().equals(castOther.getQueryName())))
				&& ((this.getQueryKey() == castOther.getQueryKey()) || (this
						.getQueryKey() != null
						&& castOther.getQueryKey() != null && this
						.getQueryKey().equals(castOther.getQueryKey())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getLoginId() == null ? 0 : this.getLoginId().hashCode());
		result = 37 * result
				+ (getQueryName() == null ? 0 : this.getQueryName().hashCode());
		result = 37 * result
				+ (getQueryKey() == null ? 0 : this.getQueryKey().hashCode());
		return result;
	}

}